package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.UnionPayCallBackRequest;
import com.srnpr.xmaspay.response.UnionPayCallBackResponse;

/**
 * 银联支付回调业务实现
 * @author pang_jhui
 *
 */
public interface IUnionPayCallBackService extends IPayService {
	
	/**
	 * 银联支付回调业务处理
	 * 
	 * @param callBackRequest
	 *            输入参数
	 * @return 响应结果
	 */
	public UnionPayCallBackResponse doProcess(UnionPayCallBackRequest callBackRequest,IUnionPayConfig unionPayConfig);

}
