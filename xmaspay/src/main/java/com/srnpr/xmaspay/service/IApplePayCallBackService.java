package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.ApplePayCallBackRequest;
import com.srnpr.xmaspay.response.ApplePayCallBackResponse;

/**
 * applePay支付回调业务实现类
 * @author pangjh
 *
 */
public interface IApplePayCallBackService extends IPayService {
	
	/**
	 * applePay回调业务处理
	 * 
	 * @param callBackRequest
	 *            输入参数
	 * @return 响应结果
	 */
	public ApplePayCallBackResponse doProcess(ApplePayCallBackRequest callBackRequest,IApplePayConfig payConfig);

}
