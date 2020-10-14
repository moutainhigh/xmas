package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.config.face.IPayGateWayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关信息配置类
 * @author pang_jhui
 *
 */
public class PayGateWayConfig extends BasePayGateWayConfig implements IPayGateWayConfig {

	@Override
	public String getRquestUrl() {
		
		return bConfig("xmaspay.pay_gateway_url");
		
	}

	@Override
	public String getSign(MDataMap mDataMap) {
		
		mDataMap.put("c_pass", getMerchantPwd());

		String[] filedNames = { "c_mid", "c_order", "c_orderamount", "c_ymd", "c_moneytype", "c_retflag", "c_returl",
				"c_paygate", "c_memo1", "c_memo2", "notifytype", "c_language", "c_version", "c_pass" };

		return calPayGateWaySign(mDataMap, filedNames);
	}

	@Override
	public String getVersion() {
		
		return bConfig("xmaspay.pay_gateway_version");
		
	}

	@Override
	public String getProtocol() {
		
		return bConfig("xmaspay.pay_gateway_protocol");
		
	}

	@Override
	public String getRequestIp() {
		
		return bConfig("xmaspay.pay_gate_way_ip");
		
	}

	@Override
	public String getRequestPath() {
		
		String path = getProtocol()+"://"+getRequestIp()+"/"+getRquestUrl();
		
		return path;
	}

	@Override
	public String getMerchantCode() {
		
		return bConfig("xmaspay.merchant_code");
		
	}

	@Override
	public String getMerchantPwd() {
		
		return bConfig("xmaspay.merchant_pwd");
		
	}

	@Override
	public String getPayCallBackUrl(String contextPath) {
		
		return contextPath+bConfig("xmaspay.paygate_callback_url");
		
	}
	
	

}
