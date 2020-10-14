package com.srnpr.xmaspay.service.impl;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IPayGateWayCallBackConfig;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.response.PayGateWayCallBackResponse;
import com.srnpr.xmaspay.service.IPayGateWayCallBackService;

/**
 * 支付网关回调
 * @author pang_jhui
 *
 */
public class PayGateWayCallBackServiceImpl implements IPayGateWayCallBackService {


	@Override
	public PayGateWayCallBackResponse doProcess(PayGateWayCallBackRequest payGateWayCallBackRequest, IPayGateWayCallBackConfig payGateWayCallBackConfig) {
		
		PayGateWayCallBackResponse payGateWayCallBackResponse = new PayGateWayCallBackResponse();
		
		payGateWayCallBackResponse.setResult(Constants.PAYGATEWAY_CALLBACK_RESULT_SUCESS);
		
		String returnUrl = payGateWayCallBackConfig.getReturnUrl(payGateWayCallBackRequest.getC_paygate());
		
		payGateWayCallBackResponse.setReURL(returnUrl);
		
		// 支持自定义的跳转地址
		if(StringUtils.isNotBlank(payGateWayCallBackRequest.getC_memo1())){
			payGateWayCallBackResponse.setReURL(payGateWayCallBackRequest.getC_memo1().trim());
		}
		
		return payGateWayCallBackResponse;
		
	}

}
