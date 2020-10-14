package com.srnpr.xmasproduct.paygateway.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestModel {

	/**
	 * 账户接入码
	 */
	private String app_id = "";
	
	/**
	 * 接口唯一编码
	 */
	private String method = "";
	
	/**
	 * 编码格式：UTF-8或GBK，二选一
	 */
	private String charset = "UTF-8";
	
	/**
	 * 签名方式：MD5或RSA，二选一
	 */
	private String sign_type = "";
	
	/**
	 * 签名
	 */
	private String sign = "";
	
	/**
	 * 请求时间，yyyy-MM-dd HH:mm:ss
	 */
	private String timestamp = "";
	
	/**
	 * 版本
	 */
	private String version = "";
	
	private Map<String, Object> body = new LinkedHashMap<String, Object>();

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

}
