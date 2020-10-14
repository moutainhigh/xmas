package com.srnpr.xmassystem.duohuozhu.model;

public class RequestHeadModel {

	/**
	 * uuid
	 */
	private String trade_id = "";
	
	/**
	 * 接口方法编号
	 */
	private String function_id = "";
	
	/**
	 * 版本号
	 */
	private String version = "1.0";
	
	/**
	 * 车队唯一标识
	 */
	private String cp_id;
	
	/**
	 * 字符集
	 */
	private String charset = "utf-8";
	
	/**
	 * 签名方式
	 */
	private String sign_type = "md5";
	
	/**
	 * 签名
	 */
	private String signed = "";
	
	/**
	 * 应用参数数据类型
	 */
	private String data_type = "xml";
	
	/**
	 * 请求时间 格式：yyyyMMddHHmmss
	 */
	private String request_time = "";

	public String getFunction_id() {
		return function_id;
	}

	public void setFunction_id(String function_id) {
		this.function_id = function_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCp_id() {
		return cp_id;
	}

	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
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

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	
}
