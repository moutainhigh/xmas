package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.ApplePayRequest;
import com.srnpr.xmaspay.response.ApplePayResponse;

/**
 * applepay业务处理接口
 * @author pang_jhui
 *
 */
public interface IApplePayService extends IPayService {
	
	/**
	 * applePay相关业务处理
	 * @param applePayRequest
	 * 		applePay请求信息
	 * @param payConfig
	 * 		applepay配置信息
	 * @return ApplePayResponse
	 */
	public ApplePayResponse doProcess(ApplePayRequest applePayRequest,IApplePayConfig payConfig);

}
