package com.srnpr.xmaspay.common;

/**
 * applePay响应结果枚举
 * @author pang_jhui
 *
 */
public enum ApplePayEnum {
	
	/**回调处理成功*/
	SUCCESS("0000"),
	
	/**回调处理失败*/
	FAILURE("9999");
	
	private String code = "";
	
	ApplePayEnum(String value) {
		
		this.code = value;
		
	}

	/**
	 * 获取处理结果代码
	 * @return
	 */
	public String getCode() {
		return code;
	}

}
