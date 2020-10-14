package com.srnpr.xmaspay.request;

/**
 * 微信通关请求信息
 * @author pang_jhui
 *
 */
public class WechatCustomRequest extends WechatRequest {
	
	/*微信支付流水号*/
	private String transaction_id = "";
	
	/*海关*/
	private String customs = "";
	
	/*子订单号*/
	private String sub_order_no = "";
	
	/*商品金额*/
	private String product_fee = "";
	
	/*物流费*/
	private String transport_fee = "";
	
	/*订单金额*/
	private String order_fee = "";
	
	/*币种*/
	private String fee_type = "";
	
	/*商户海关备案号*/
	private String mch_customs_no = "";
	
	/*证件号码*/
	private String cert_id = "";

	/*证件类型*/
	private String cert_type = "";
	
	/*用户姓名*/
	private String name = "";
	
	/*关税*/
	private String duty = "";

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getCustoms() {
		return customs;
	}

	public void setCustoms(String customs) {
		this.customs = customs;
	}

	public String getSub_order_no() {
		return sub_order_no;
	}

	public void setSub_order_no(String sub_order_no) {
		this.sub_order_no = sub_order_no;
	}

	public String getProduct_fee() {
		return product_fee;
	}

	public void setProduct_fee(String product_fee) {
		this.product_fee = product_fee;
	}

	public String getTransport_fee() {
		return transport_fee;
	}

	public void setTransport_fee(String transport_fee) {
		this.transport_fee = transport_fee;
	}

	public String getOrder_fee() {
		return order_fee;
	}

	public void setOrder_fee(String order_fee) {
		this.order_fee = order_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getMch_customs_no() {
		return mch_customs_no;
	}

	public void setMch_customs_no(String mch_customs_no) {
		this.mch_customs_no = mch_customs_no;
	}

	public String getCert_id() {
		return cert_id;
	}

	public void setCert_id(String cert_id) {
		this.cert_id = cert_id;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}

}
