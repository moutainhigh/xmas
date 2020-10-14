package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.process.IApplePayProcess;
import com.srnpr.xmaspay.request.ApplePayRequest;
import com.srnpr.xmaspay.response.ApplePayResponse;
import com.srnpr.xmaspay.service.IApplePayService;

/**
 * applePay支付业务实现类
 * 
 * @author pangjh
 *
 */
public class ApplePayProcess extends BasePayProcess implements IApplePayProcess {

	@Override
	public ApplePayResponse process(String orderCode) {
		
		IApplePayConfig applePayConfig = (IApplePayConfig) getPayConfig();
		
		IApplePayService applePayService = (IApplePayService) getPayService();
		
		ApplePayRequest applePayRequest = new ApplePayRequest();
		applePayRequest.setOrderCode(orderCode);
		
		ApplePayResponse applePayResponse = applePayService.doProcess(applePayRequest, applePayConfig);
		
		return applePayResponse;
		
	}

}
