package com.srnpr.xmaspay.response;

/**
 * 微信通关响应信息
 * @author pang_jhui
 *
 */
public class WechatCustomResponse extends WechatResponse {
	
	/*签名类型*/
	private String sign_type = "";
	
	/*状态码*/
	private String state = "";
	
	/*微信支付返回的订单号*/
	private String transaction_id = "";
	
	/*业务结果*/	
	private String result_code = "";
	
	/*最近更新时间*/
	private String modify_time = "";
	
	/*子订单编号*/
	private String sub_order_no = "";
	
	/*微信子订单编号*/
	private String sub_order_id = "";

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getSub_order_no() {
		return sub_order_no;
	}

	public void setSub_order_no(String sub_order_no) {
		this.sub_order_no = sub_order_no;
	}

	public String getSub_order_id() {
		return sub_order_id;
	}

	public void setSub_order_id(String sub_order_id) {
		this.sub_order_id = sub_order_id;
	}

}
