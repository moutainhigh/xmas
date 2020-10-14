package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IPayGateWayConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.PayGateWayRequest;
import com.srnpr.xmaspay.response.PayGateWayResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关业务实现
 * @author pang_jhui
 *
 */
public interface IPayGateWayService extends IPayService {
	
	/**
	 * 支付相关业务处理
	 * @param payGateWayRequest
	 * 		支付网关请求信息
	 * @param payConfig
	 * 		支付配置信息
	 * @param extendMap
	 * 		扩展参数集合
	 * @return PayGateWayResponse
	 */
	public PayGateWayResponse doProcess(PayGateWayRequest payGateWayRequest,IPayGateWayConfig payConfig,MDataMap extendMap);

}
