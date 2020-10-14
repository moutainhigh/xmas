package com.srnpr.xmaspay.process.impl;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateEnum;
import com.srnpr.xmaspay.common.PayGateTypeEnum;
import com.srnpr.xmaspay.config.face.IPayGateWayConfig;
import com.srnpr.xmaspay.process.IPayGateWayProcess;
import com.srnpr.xmaspay.request.PayGateWayRequest;
import com.srnpr.xmaspay.response.PayGateWayResponse;
import com.srnpr.xmaspay.service.IPayGateWayService;
import com.srnpr.xmaspay.util.DictManager;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关解析
 * @author pang_jhui
 *
 */
public class PayGateWayProcess extends BasePayProcess implements IPayGateWayProcess {

	@Override
	public PayGateWayResponse process(String orderCode, String payType,String url) {		
		
		
		IPayGateWayService payGateWayService = (IPayGateWayService) getPayService();
		
		IPayGateWayConfig payGateWayConfig = (IPayGateWayConfig) getPayConfig();
		
		PayGateWayRequest payGateWayRequest = new PayGateWayRequest();
		
		/*获取支付网关编码*/
		String payGateName = PayGateEnum.getEnumNameByTypeCode(payType);
		
		if(StringUtils.containsIgnoreCase(orderCode, "wap")){
			
			payGateWayRequest.setC_paygate_type(PayGateTypeEnum.ALIPAY_WAP.getCode());
			
			payGateWayRequest.setC_paygate_account(PayGateTypeEnum.ALIPAY_WAP.getAccount());
			
		}else{
			
			payGateWayRequest.setC_paygate_type(PayGateTypeEnum.ALIPAY_WEB.getCode());
			
			payGateWayRequest.setC_paygate_account(PayGateTypeEnum.ALIPAY_WEB.getAccount());
			
		}
		
		int payGate =  DictManager.getPayGateCode(payGateName);
		
		/*设置订单编号*/
		payGateWayRequest.setC_order(orderCode);
		
		/*设置支付网关*/
		payGateWayRequest.setC_paygate(payGate);
		
		MDataMap extendMap = new MDataMap();
		
		String[] contextPaths = url.split("/");
		
		String contextPath = contextPaths[0]+"//"+contextPaths[2]+"/"+contextPaths[3];
		
		extendMap.put(Constants.SERVER_CONTEXT_PATH, contextPath);
		
		return payGateWayService.doProcess(payGateWayRequest, payGateWayConfig, extendMap);
		
	}

}
