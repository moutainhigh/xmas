package com.srnpr.xmaspay.common;

/**
 * 支付宝业务错误码（API1.0）
 * @author pang_jhui
 *
 */
public enum AlipayDetailErrorCodeEnum {
	
	/**交易不存在*/
	TRADE_NOT_EXIST("交易不存在"),
	/**交易状态不合法*/
	TRADE_STATUS_ERROR("交易状态不合法"),
	/**参数不合法*/
	INVALID_PARAMETER("参数不合法");
	
	/*错误代码信息*/
	private String msg = "";
	
	AlipayDetailErrorCodeEnum(String msg){
		
		this.msg = msg;
		
	}
	
	/**
	 * 获取错误信息
	 * @return 错误信息
	 */
	public String getMsg() {
		return msg;
	}

}
