package com.srnpr.xmaspay.common;

/**
 * 支付交易状态
 * @author pang_jhui
 *
 */
public enum TradeStatusEnum {
	
	/**支付交易成功*/
	TRADE_SUCCESS("Y","1"),
	
	/**支付交易失败*/
	TRADE_FAILURE("N","0");
	
	/*支付网关传回的标志*/
	private String success_mark = "";
	
	/*商户系统本身记录的标识*/
	private String flag_success = "";
	
	TradeStatusEnum(String success_mark, String flag_success) {
		
		this.success_mark = success_mark;
		
		this.flag_success = flag_success;
	
	}

	/**
	 * 获取支付网关返回的是否支付成功标识
	 * @return
	 */
	public String getSuccess_mark() {
		return success_mark;
	}

	/**
	 * 获取系统记录是否支付成功标识
	 * @return
	 */
	public String getFlag_success() {
		return flag_success;
	}

}
