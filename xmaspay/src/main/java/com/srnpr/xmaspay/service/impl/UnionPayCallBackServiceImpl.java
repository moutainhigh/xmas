package com.srnpr.xmaspay.service.impl;

import com.srnpr.xmaspay.common.UnionPayEnum;
import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.request.UnionPayCallBackRequest;
import com.srnpr.xmaspay.response.UnionPayCallBackResponse;
import com.srnpr.xmaspay.service.IUnionPayCallBackService;

/**
 * 银联支付回调业务实现
 * @author pangjh
 *
 */
public class UnionPayCallBackServiceImpl implements IUnionPayCallBackService {

	@Override
	public UnionPayCallBackResponse doProcess(UnionPayCallBackRequest callBackRequest, IUnionPayConfig unionPayConfig) {
		
		UnionPayCallBackResponse callBackResponse = new UnionPayCallBackResponse();

		callBackResponse.setResultmsg(UnionPayEnum.OK.name());
		
		return callBackResponse;
	}

}
