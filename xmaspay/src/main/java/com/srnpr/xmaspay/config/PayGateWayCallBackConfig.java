package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IPayGateWayCallBackConfig;
import com.srnpr.xmaspay.util.DictManager;
import com.srnpr.zapcom.basemodel.MDataMap;

public class PayGateWayCallBackConfig extends BasePayGateWayConfig implements IPayGateWayCallBackConfig{

	@Override
	public String getSign(MDataMap mDataMap) {
		
		mDataMap.put("c_pass", getMerchantPwd());
		
		String[] fieldNames = {"c_mid","c_order","c_orderamount","c_ymd","c_transnum","c_succmark","c_moneytype","dealtime","c_memo1","c_memo2","c_version","c_pass"};
		
		return calPayGateWaySign(mDataMap, fieldNames);
		
	}

	@Override
	public String getReturnUrl(int payGate) {

		/* 根据支付方式获取，支付回调成功的页面 */
		return DictManager.getPayGateChildInfo(Integer.toString(payGate), Constants.ZW_DEFINE_RETURN_URL);

	}
	
	@Override
	public String getMerchantPwd() {
		
		return bConfig("xmaspay.merchant_pwd");
		
	}

}
