package com.srnpr.xmassystem.duohuozhu.model;

public class ResponseHeaderModel {

	/**
	 * uuid
	 */
	private String trade_id = "";
	
	/**
	 * 接口方法编号
	 */
	private String function_id = "";
	
	/**
	 * 车队唯一标致
	 */
	private String cp_id = "";
	
	/**
	 * 签名
	 */
	private String signed = "";
	
	/**
	 * 响应时间，格式：yyyyMMddHHmmss
	 */
	private String resp_time = "";
	
	/**
	 * 请求时间，格式：yyyyMMddHHmmss
	 */
	private String request_time = "";
	
	/**
	 * 响应编码
	 */
	private String resp_code = "";
	
	/**
	 * 响应信息
	 */
	private String resp_msg = "";

	public String getFunction_id() {
		return function_id;
	}

	public void setFunction_id(String function_id) {
		this.function_id = function_id;
	}

	public String getCp_id() {
		return cp_id;
	}

	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getResp_time() {
		return resp_time;
	}

	public void setResp_time(String resp_time) {
		this.resp_time = resp_time;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	
}
