package com.srnpr.xmaspay.common;

/**
 * 微信报关枚举
 * @author pang_jhui
 *
 */
public enum WechatCustomEnum {
	
	/**无需上报海关*/
	NO(""),
	/**广州*/
	GUANGZHOU(""),
	/**杭州*/
	HANGZHOU(""),
	/**宁波*/
	NINGBO("3302462055"),
	/**郑州（保税物流中心）*/
	ZHENGZHOU_BS(""),
	/**重庆*/
	CHONGQING(""),
	/**西安*/
	XIAN(""),
	/**上海*/
	SHANGHAI(""),
	/**郑州（综保区）*/
	ZHENGZHOU_ZH("");
	
	/*海关备案编号*/
	private String code = "";
	
	WechatCustomEnum(String code) {
		
		this.code = code;
		
	}
	
	/**
	 * 获取海关备案编号
	 * @return
	 */
	public String getCode() {
		return code;
	}

}
