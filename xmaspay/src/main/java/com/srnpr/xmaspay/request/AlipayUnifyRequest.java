package com.srnpr.xmaspay.request;

import com.srnpr.xmaspay.face.IPayRequest;

/**
 * 支付宝支付公共请求参数（api 1.0）
 * @author pang_jhui
 *
 */
public class AlipayUnifyRequest implements IPayRequest {
	
	
	/*接口名称*/
	private String service = "";
	
	/*签约的支付宝账号对应的支付宝唯一用户号*/
	private String partner = "";
	
	/*参数字符编码*/
	private String _input_charset = "";
	
	/*签名类型*/
	private String sign_type = "";
	
	/*签名*/
	private String sign = "";

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	


}
