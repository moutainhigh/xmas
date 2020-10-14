package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IPayGateWayCallBackConfig;
import com.srnpr.xmaspay.process.IPayGateWayCallBackProcess;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.response.PayGateWayCallBackResponse;
import com.srnpr.xmaspay.service.IPayGateWayCallBackService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关回调解析实现
 * @author pang_jhui
 *
 */
public class PayGateWayCallBackProcess extends BasePayProcess implements IPayGateWayCallBackProcess {

	@Override
	public String process(MDataMap mDataMap) {

		IPayGateWayCallBackService payGateWayCallBackService = (IPayGateWayCallBackService) getPayService();

		IPayGateWayCallBackConfig payGateWayCallBackConfig = (IPayGateWayCallBackConfig) getPayConfig();		

		PayGateWayCallBackResponse payGateWayCallBackResponse = new PayGateWayCallBackResponse();
		try {
			
			payGateWayCallBackResponse = payGateWayCallBackService.doProcess(initRquest(mDataMap),
					payGateWayCallBackConfig);
			
		} catch (Exception e) {
			
			payGateWayCallBackResponse.setResult(Constants.PAYGATEWAY_CALLBACK_RESULT_SUCESS);
			
			payGateWayCallBackResponse.setReURL(e.getMessage());
			
		}
		
		return parseXml(payGateWayCallBackResponse);

	}
	
	/**
	 * 生成支付网关回调字符串
	 * @param response
	 * 		响应信息
	 * @return 生成返回串
	 */
	public String parseXml(PayGateWayCallBackResponse response){
		
		StringBuffer xmlBuffer = new StringBuffer();
		
		xmlBuffer.append("<result>").append(response.getResult()).append("</result>");
		
		xmlBuffer.append("<reURL>").append(response.getReURL()).append("</reURL>");
		
		return xmlBuffer.toString();
		
	}
	
	/**
	 * 初始化支付网关请求信息
	 * @param mDataMap
	 * 		请求参数
	 * @return 支付网关请求对象
	 * @throws Exception 
	 */
	public PayGateWayCallBackRequest initRquest(MDataMap mDataMap) throws Exception {

		PayGateWayCallBackRequest payGateWayCallBackRequest =  BeanComponent.getInstance()
				.invoke(PayGateWayCallBackRequest.class, mDataMap,false);

		return payGateWayCallBackRequest;

	}

}
