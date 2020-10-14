package com.srnpr.xmassystem.modelorder;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderAddress {
	@ZapcomApi(value = "订单编号")
	private String orderCode;

	@ZapcomApi(value = "区域编号")
	private String areaCode;

	@ZapcomApi(value = "地址")
	private String address;

	@ZapcomApi(value = "邮编")
	private String postcode;

	@ZapcomApi(value = "手机号码")
	private String mobilephone;

	@ZapcomApi(value = "电话号码")
	private String telephone;

	@ZapcomApi(value = "收件人")
	private String receivePerson;

	@ZapcomApi(value = "邮箱")
	private String email;

	@ZapcomApi(value = "发票抬头")
	private String invoiceTitle;

	@ZapcomApi(value = "发票类型")
	private String invoiceType;

	@ZapcomApi(value = "发票内容")
	private String invoiceContent;

	@ZapcomApi(value = "是否开发票")
	private Integer flagInvoice;

	@ZapcomApi(value = "备注")
	private String remark;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getReceivePerson() {
		return receivePerson;
	}

	public void setReceivePerson(String receivePerson) {
		this.receivePerson = receivePerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public Integer getFlagInvoice() {
		return flagInvoice;
	}

	public void setFlagInvoice(Integer flagInvoice) {
		this.flagInvoice = flagInvoice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
