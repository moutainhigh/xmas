package com.srnpr.xmaspay.common;

/**
 * 支付解析名称枚举
 * @author pang_jhui
 *
 */
public enum PayProcessEnum {
	/**支付网关解析*/
	payGateWayProcess,
	/**支付网关回调解析*/
	payGateWayCallBackProcess,
	/**支付网关订单支付解析*/
	orderPayService,
	/**交易取消日志记录*/
	tradeCancelLogService,
	/**微信交易取消解析*/
	wechatTradeCancelProcess,
	/**支付宝交易取消解析*/
	alipayTradeCancelProcess,
	/**银联支付解析*/
	unionPayProcess,
	/**applePay解析*/
	applePayProcess,
	/**银联支付回调*/
	unionPayCallBackProcess,
	/**apple支付回调*/
	applePayCallBackProcess,	
	/**微信报关*/
	wechatCustomProcess,
	/**支付宝报关*/
	alipayCustomsProcess,
	/**订单报关统一入口*/
	payCustomsProcess,
	/**订单报关记录*/
	orderCustomsService;

}
