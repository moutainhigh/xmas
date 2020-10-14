package com.srnpr.xmaspay.common;

/**
 * 调用支付宝接口接口接入错误代码（api1.0）
 * @author pang_jhui
 *
 */
public enum AlipayErrorCodeEnum {
	
	/**签名不正确*/
	ILLEGAL_SIGN("签名不正确"),
	/**动态密钥信息错误*/
	ILLEGAL_DYN_MD5_KEY("动态密钥信息错误"),
	/**加密不正确*/
	ILLEGAL_ENCRYPT("加密不正确"),
	/**参数不正确*/
	ILLEGAL_ARGUMENT("参数不正确"),
	/**Service 参数不正确*/
	ILLEGAL_SERVICE("Service 参数不正确"),
	/**用户 ID 不正确*/
	ILLEGAL_USER("用户 ID 不正确"),
	/**合作伙伴 ID 不正确*/
	ILLEGAL_PARTNER("合作伙伴 ID 不正确"),
	/**接口配置不正确*/
	ILLEGAL_EXTERFACE("接口配置不正确"),
	/**合作伙伴接口信息不正确*/
	ILLEGAL_PARTNER_EXTERFACE("合作伙伴接口信息不正确"),
	/**未找到匹配的密钥配置*/
	ILLEGAL_SECURITY_PROFILE("未找到匹配的密钥配置"),
	/**代理 ID 不正确*/
	ILLEGAL_AGENT("代理 ID 不正确"),
	/**签名类型不正确*/
	ILLEGAL_SIGN_TYPE("签名类型不正确"),
	/**字符集不合法*/
	ILLEGAL_CHARSET("字符集不合法"),
	/**无权访问*/
	HAS_NO_PRIVILEGE("无权访问"),
	/**字符集无效*/
	INVALID_CHARACTER_SET("字符集无效"),
	/**支付宝系统错误*/
	SYSTEM_ERROR("支付宝系统错误"),
	/**session 超时*/
	SESSION_TIMEOUT("session 超时"),	
	/**错误的 target_service*/
	ILLEGAL_TARGET_SERVICE("错误的 target_service"),
	/**partner 不允许访问该类型的系统*/
	ILLEGAL_ACCESS_SWITCH_SYSTEM("partner 不允许访问该类型的系统"),
	/**接口已关闭*/
	EXTERFACE_IS_CLOSED("接口已关闭");
	
	/*错误代码信息*/
	private String msg = "";
	
	AlipayErrorCodeEnum(String msg){
		
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
