package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.config.face.IAlipayTradeCancelConfig;

/**
 * 支付宝交易取消配置信息
 * @author pang_jhui
 *
 */
public class AlipayTradeCancelConfig extends AlipayConfig implements IAlipayTradeCancelConfig {
	
	
	public String getMethod(){
		
		return bConfig("xmaspay.alipay_method_trade_cancel");
		
	}


}
