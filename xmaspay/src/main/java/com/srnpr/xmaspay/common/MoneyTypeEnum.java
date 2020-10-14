package com.srnpr.xmaspay.common;

/**
 * 金额类型
 * @author pang_jhui
 *
 */
public enum MoneyTypeEnum {
	
	RMB("0","人民币");	
	
	/*编码*/
	private String code = "";
	
	/*币种*/
	private String name = "";
	
	MoneyTypeEnum(String code, String name){
		
		this.code = code;
		
		this.name = name;
		
	}

	/**
	 * 获取编码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}


}
