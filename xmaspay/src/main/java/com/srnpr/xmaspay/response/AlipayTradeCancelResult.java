package com.srnpr.xmaspay.response;

import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝响应结果
 * @author pang_jhui
 *
 */
public class AlipayTradeCancelResult {

	/* 支付宝响应接口 */
	private MDataMap alipay_trade_cancel_response = new MDataMap();

	public MDataMap getAlipay_trade_cancel_response() {
		return alipay_trade_cancel_response;
	}

	public void setAlipay_trade_cancel_response(MDataMap alipay_trade_cancel_response) {
		this.alipay_trade_cancel_response = alipay_trade_cancel_response;
	}


	

}
