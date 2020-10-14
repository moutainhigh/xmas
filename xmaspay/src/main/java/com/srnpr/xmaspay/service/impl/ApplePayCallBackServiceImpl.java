package com.srnpr.xmaspay.service.impl;

import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.request.ApplePayCallBackRequest;
import com.srnpr.xmaspay.response.ApplePayCallBackResponse;
import com.srnpr.xmaspay.service.IApplePayCallBackService;

/**
 * applePay回调业务实现类
 * @author pangjh
 *
 */
public class ApplePayCallBackServiceImpl implements IApplePayCallBackService {

	@Override
	public ApplePayCallBackResponse doProcess(ApplePayCallBackRequest callBackRequest,IApplePayConfig payConfig) {
		
		return new ApplePayCallBackResponse();
	}

}
