package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IPayGateWayCallBackConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.response.PayGateWayCallBackResponse;

public interface IPayGateWayCallBackService extends IPayService {

	/**
	 * 支付网关回调处理
	 * 
	 * @param mDataMap
	 *            输入参数
	 * @param payGateWayCallBackConfig
	 *            支付网关回调支付信息
	 * @return 响应结果
	 */
	public PayGateWayCallBackResponse doProcess(PayGateWayCallBackRequest payGateWayCallBackRequest,
			IPayGateWayCallBackConfig payGateWayCallBackConfig);

}
