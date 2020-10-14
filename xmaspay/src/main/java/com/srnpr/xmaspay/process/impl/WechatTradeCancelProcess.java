package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.face.IWechatTradeCancelConfig;
import com.srnpr.xmaspay.process.IWechatTradeCancelProcess;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatUnifyResponse;
import com.srnpr.xmaspay.service.IWechatTradeCancelService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmaspay.util.WechatUtil;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信交易查询相关业务处理
 * @author pang_jhui
 * @author zhaojunling 关闭待支付订单逻辑暂时废弃 
 */
@Deprecated
public class WechatTradeCancelProcess extends BasePayProcess implements IWechatTradeCancelProcess {

	@Override
	public WechatUnifyResponse process(String orderCode) {
		return new WechatUnifyResponse();
		/*
		WechatUnifyResponse wechatUnifyResponse = new WechatUnifyResponse();
		
		IWechatTradeCancelService tradeCancelService = (IWechatTradeCancelService) getPayService();
		
		IWechatTradeCancelConfig tradeCancelConfig = (IWechatTradeCancelConfig) getPayConfig();
		
		try {
			
			 tradeCancelService.doProcess(initRequest(orderCode, tradeCancelConfig), tradeCancelConfig);
			 
		} catch (Exception e) {
			
			wechatUnifyResponse.setReturn_code(WechatUnifyResultCodeEnum.FAIL.name());
			
			wechatUnifyResponse.setReturn_msg(e.getMessage());
			
		}
		
		return wechatUnifyResponse;
		*/
	}

	@Override
	public WechatRequest initRequest(String orderCode, IWechatTradeCancelConfig config) throws Exception {
		
		WechatRequest wechatRequest = new WechatRequest();
		
		MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		
		if(orderMap != null){
			
			String manageCode = orderMap.get("seller_code");
			
			wechatRequest.setAppid(config.getMerchantAppId(manageCode));
			
			wechatRequest.setMch_id(config.getMechantId(manageCode));
			
			wechatRequest.setNonce_str(WechatUtil.getNonceStr());
			
			wechatRequest.setOut_trade_no(orderCode);
			
			MDataMap mDataMap = BeanComponent.getInstance().objectToMap(wechatRequest,null,false);
			
			String sign = config.getSign(manageCode, mDataMap);
			
			wechatRequest.setSign(sign);
			
		}
		
		return wechatRequest;
	}
	
	

}
