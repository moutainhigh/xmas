package com.srnpr.xmaspay.common;

/**
 * 银联支付枚举
 * @author pang_jhui
 *
 */
public enum UnionPayEnum {
	
	OK(""),
	
	/**支付成功*/
	SUCCESS("00");
	
	private String code = "";
	
	UnionPayEnum(String value) {
		
		this.code = value;
		
	}

	/**
	 * 获取响应代码
	 * @return
	 */
	public String getCode() {
		return code;
	}

}
