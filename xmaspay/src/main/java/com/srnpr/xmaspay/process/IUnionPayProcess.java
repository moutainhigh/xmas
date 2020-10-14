package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.response.UnionPayResponse;

public interface IUnionPayProcess extends IPayProcess {
	
	/**
	 * 银联支付解析
	 * @param orderCode
	 * 		订单编号
	 * @return IPayResponse
	 */
	public UnionPayResponse process(String orderCode);

}
