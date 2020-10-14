package com.srnpr.xmaspay.common;

/**
 * 支付网关类型
 * @author pang_jhui
 *
 */
public enum PayGateTypeEnum {
	
	ALIPAY(0,0),

	ALIPAY_WEB(1, 65),
	
	ALIPAY_WAP(2, 651),
	
	WECHAT(3,0);	
	
	/*编码*/
	private int code ;
	
	/*币种*/
	private int account ;
	
	PayGateTypeEnum(int code, int account){
		
		this.code = code;
		
		this.account = account;
		
	}

	/**
	 * 获取编码
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 获取账户编号
	 * @return
	 */
	public int getAccount() {
		return account;
	}




	
}
