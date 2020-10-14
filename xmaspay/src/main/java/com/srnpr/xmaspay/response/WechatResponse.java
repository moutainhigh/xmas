package com.srnpr.xmaspay.response;

/**
 * 微信响应信息基类
 * @author pang_jhui
 *
 */
public class WechatResponse extends WechatUnifyResponse {
	
	/*公众账号ID*/
	private String appid = "";
	
	/*商户号*/
	private String mch_id = "";

	/*随机字符串*/
	private String nonce_str = "";

	/*签名*/
	private String sign = "";

	/*错误代码*/
	private String err_code = "";

	/*错误代码描述*/
	private String err_code_des = "";

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	
}
