package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.response.PayGateWayResponse;

/**
 * 支付网关解析
 * @author pang_jhui
 *
 */
public interface IPayGateWayProcess extends IPayProcess {

	/**
	 * 支付相关统一入口
	 * @param orderCode
	 * 		请求参数
	 * @param contextPath
	 * 		web服务器目录（用户组装回调地址）例如：http://127.0.0.1:8080/cfamily
	 * @return IPayResponse
	 */
	public PayGateWayResponse process(String orderCode, String payType,String contextPath);
	
}
