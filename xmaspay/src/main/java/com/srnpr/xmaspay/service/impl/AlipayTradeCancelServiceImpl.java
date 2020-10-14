package com.srnpr.xmaspay.service.impl;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.common.AlipayResponseNodeEnum;
import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.config.face.IAlipayTradeCancelConfig;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayTradeCancelResponse;
import com.srnpr.xmaspay.response.AlipayUnifyErrorResponse;
import com.srnpr.xmaspay.response.AlipayUnifyResponse;
import com.srnpr.xmaspay.service.IAlipayTradeCancelService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.XmlUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 支付宝交易取消业务实现
 * @author pang_jhui
 *
 */
public class AlipayTradeCancelServiceImpl implements IAlipayTradeCancelService {

	@Override
	public AlipayUnifyResponse doProcess(AlipayUnifyRequest request, IAlipayTradeCancelConfig config) {
		
		String returnStr = "";
		
		AlipayUnifyErrorResponse errorResponse = new AlipayUnifyErrorResponse();
		
		AlipayUnifyResponse unifyResponse = new AlipayUnifyResponse();
		
		
		try {
			
			MDataMap mDataMap = BeanComponent.getInstance().objectToMap(request,AlipayUnifyRequest.class,true);
			
			returnStr = WebClientSupport.upPost(config.getRequestUrl(), mDataMap);
			
		} catch (Exception e) {
			
			errorResponse.setIs_success(AlipaySuccessEnum.F.name());
			
			errorResponse.setError(e.getMessage());
			
			return errorResponse;
			
		}
		
		try {
			
			MDataMap mDataMap = XmlUtil.getInstance().xmlToMDataMap(returnStr);
			
			if(mDataMap != null){
				
				String flag = mDataMap.get(AlipayResponseNodeEnum.is_success.name());
				
				if(StringUtils.equals(flag, AlipaySuccessEnum.F.name())){
					
					unifyResponse = BeanComponent.getInstance().invoke(AlipayUnifyErrorResponse.class, mDataMap,false);
					
				}else{
					
					String responseXmlStr = mDataMap.get(AlipayResponseNodeEnum.response.name());
					
					if(StringUtils.isNotBlank(responseXmlStr)){
						
						MDataMap responseMap = XmlUtil.getInstance().xmlToMDataMap(responseXmlStr);
						
						unifyResponse = BeanComponent.getInstance().invoke(AlipayTradeCancelResponse.class, responseMap,false);
						
					}
					
					
					
				}
				
			}else{
				
				errorResponse.setIs_success(AlipaySuccessEnum.F.name());
				
				errorResponse.setError(returnStr);
				
				return errorResponse;
				
			}
			
			
		} catch (Exception e) {
			
			errorResponse.setIs_success(AlipaySuccessEnum.F.name());
			
			errorResponse.setError(e.getMessage());
			
			return errorResponse;
			
		}
		
		return unifyResponse;	
		
	}

}
