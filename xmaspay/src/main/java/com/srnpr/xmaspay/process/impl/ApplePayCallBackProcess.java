package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.common.ApplePayEnum;
import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.process.IApplePayCallBackProcess;
import com.srnpr.xmaspay.request.ApplePayCallBackRequest;
import com.srnpr.xmaspay.response.ApplePayCallBackResponse;
import com.srnpr.xmaspay.service.IApplePayCallBackService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * applePay支付回调解析
 * @author pang_jhui
 *
 */
public class ApplePayCallBackProcess extends BasePayProcess implements IApplePayCallBackProcess {

	@Override
	public String process(MDataMap mDataMap) {
		
		ApplePayCallBackRequest request = null;
		
		ApplePayCallBackResponse response = new ApplePayCallBackResponse();
		
		JsonHelper<ApplePayCallBackResponse> jsonHelper = new JsonHelper<ApplePayCallBackResponse>();
		
		String returnStr = "";
		
		try {
			
			request = BeanComponent.getInstance().invoke(ApplePayCallBackRequest.class, mDataMap,false);
			
		} catch (Exception e) {
			
			response.setRet_code(ApplePayEnum.FAILURE.getCode());
			
			response.setRet_msg("请求参数转换失败");
			
			return jsonHelper.ObjToString(response);
			
		}
		
		IApplePayCallBackService service = (IApplePayCallBackService) getPayService();
		
		IApplePayConfig applePayConfig = (IApplePayConfig) getPayConfig();
		
		response = service.doProcess(request, applePayConfig);		
		
		returnStr = jsonHelper.ObjToString(response);
		
		return returnStr;
	}

}
