package com.srnpr.xmaspay.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.face.IWechatTradeCancelConfig;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatResponse;
import com.srnpr.xmaspay.response.WechatUnifyResponse;
import com.srnpr.xmaspay.service.IWechatTradeCancelService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.XmlUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 微信交易取消业务实现
 * @author pang_jhui
 *
 */
public class WechatTradeCancelServiceImpl implements IWechatTradeCancelService {

	@Override
	public WechatUnifyResponse doProcess(WechatRequest request, IWechatTradeCancelConfig config) {
		
		WechatUnifyResponse wechatUnifyResponse = new WechatUnifyResponse();
		
		MDataMap headerDataMap = new MDataMap();
		
		headerDataMap.put("Pragma:", "no-cache");
		
		headerDataMap.put("Cache-Control", "no-cache");
		
		headerDataMap.put("Content-Type", "text/xml");
		
		try {
			
			MDataMap requestMap = BeanComponent.getInstance().objectToMap(request,null,false);
			
			String requestXml = XmlUtil.getInstance().mDataMapToXml(requestMap);
			
			HttpEntity httpEntity = new StringEntity(requestXml);
			
			String responseMsg = WebClientSupport.poolRequest(config.getRequestUrl(), httpEntity, headerDataMap);
			
			MDataMap mDataMap = XmlUtil.getInstance().xmlToMDataMap(responseMsg);			
			
			if(mDataMap != null){
				
				String resultCode = mDataMap.get("return_code");
				
				if(StringUtils.equals(resultCode, WechatUnifyResultCodeEnum.SUCCESS.name())){
					
					wechatUnifyResponse = BeanComponent.getInstance().invoke(WechatResponse.class, mDataMap,false);
					
				}else{
					
					wechatUnifyResponse = BeanComponent.getInstance().invoke(WechatUnifyResponse.class, mDataMap,false);
					
				}
				
				
			}
			
		} catch(Exception ex) {
			
			wechatUnifyResponse.setReturn_code(WechatUnifyResultCodeEnum.FAIL.name());
			
			wechatUnifyResponse.setReturn_msg(ex.getMessage());
		
		}
		
		return wechatUnifyResponse;
		
		
	}

}
