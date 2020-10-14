package com.srnpr.xmaspay;

/**
 * 支付渠道
 * 
 * @author zhaojunling
 */
public enum PaymentChannel {
	
	/**
	 * APP原生支付
	 */
	APP,
	/**
	 * PC网站渠道支付
	 */
	WEB,
	/**
	 * WAP网站渠道支付
	 */
	WAP,
	/**
	 * 微信短信渠道支付
	 */
	WAPSMS,
	/**
	 * 微信小程序支付
	 */
	JSAPI_WXSS,
	/**
	 * 微信JSAPI支付
	 */
	JSAPI,
	/**
	 * 微信二维码支付
	 */
	BARCODE,
	/**
	 * 网页版APP支付
	 */
	WAPAPP
}
