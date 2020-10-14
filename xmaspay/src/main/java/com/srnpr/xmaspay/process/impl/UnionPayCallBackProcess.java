package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.process.IUnionPayCallBackProcess;
import com.srnpr.xmaspay.request.UnionPayCallBackRequest;
import com.srnpr.xmaspay.response.UnionPayCallBackResponse;
import com.srnpr.xmaspay.service.IUnionPayCallBackService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 银联支付回调解析
 * @author pang_jhui
 *
 */
public class UnionPayCallBackProcess extends BasePayProcess implements IUnionPayCallBackProcess {

	@Override
	public String process(MDataMap mDataMap) {
		
		String returnStr = "";
		
		IUnionPayCallBackService service = (IUnionPayCallBackService) getPayService();
		
		IUnionPayConfig unionPayConfig = (IUnionPayConfig) getPayConfig();
		
		UnionPayCallBackRequest callBackRequest = new UnionPayCallBackRequest();
		
		UnionPayCallBackResponse callBackResponse = new UnionPayCallBackResponse();
		
		try {
			
			callBackRequest = BeanComponent.getInstance().invoke(UnionPayCallBackRequest.class, mDataMap,false);
		
			callBackResponse = service.doProcess(callBackRequest,unionPayConfig);
			
		} catch (Exception e) {
			
			callBackResponse.setResultcode(-1);
			
			callBackResponse.setResultmsg(e.getMessage());
			
		}		
		
		if(callBackResponse.upFlagTrue()){
			
			returnStr = callBackResponse.getResultmsg();
			
		}
		
		return returnStr;
	}

}
