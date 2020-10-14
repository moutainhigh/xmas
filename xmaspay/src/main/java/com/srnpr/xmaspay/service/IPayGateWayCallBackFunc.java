package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.face.IPayFunc;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 支付回调成功后相关业务处理
 * @author pang_jhui
 *
 */
public interface IPayGateWayCallBackFunc extends IPayFunc {
	
	/**
	 * 处理支付回调相关业务
	 * @param response
	 * 		响应信息
	 * @return 处理结果
	 */
	public RootResultWeb doAfter(PayGateWayCallBackRequest request);

}
