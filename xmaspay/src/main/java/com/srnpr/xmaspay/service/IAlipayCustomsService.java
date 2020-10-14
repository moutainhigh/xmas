package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IAlipayCustomsConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.AlipayCustomsRequest;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;

/**
 * 支付宝报关状态业务实现
 * @author pang_jhui
 *
 */
public interface IAlipayCustomsService extends IPayService {
	
	/**
	 * 支付宝报关实现
	 * @param request
	 * 		请求信息
	 * @param config
	 * 		配置信息
	 * @return 接口响应信息
	 */
	public AlipayCustomsResponse doProcess(AlipayCustomsRequest request, IAlipayCustomsConfig customConfig);



}
