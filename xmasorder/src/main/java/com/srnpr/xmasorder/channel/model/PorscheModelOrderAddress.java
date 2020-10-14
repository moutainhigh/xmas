package com.srnpr.xmasorder.channel.model;

/**
 * 订单收货地址
 * @remark
 * @author 任宏斌
 * @date 2019年12月2日
 */
public class PorscheModelOrderAddress {

	private String uid = "";
	/**
	 * 订单编号
	 */
	private String orderCode = "";

	/**
	 * 地区编码
	 */
	private String areaCode = "";

	/**
	 * 地址信息
	 */
	private String address = "";

	/**
	 * 邮政编码
	 */
	private String postcode = "";

	/**
	 * 电话
	 */
	private String mobilephone = "";

	/**
	 * 固定电话
	 */
	private String telephone = "";

	/**
	 * 收货人
	 */
	private String receivePerson = "";

	/**
	 * 订单备注
	 */
	private String remark = "";

	/**
	 * 地址编号
	 */
	private String addressCode = "";
	
	/**
	 * 是否开发票 1 开， 0 不开 
	 * 默认不开票
	 */
	private int flagInvoice = 0;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public int getFlagInvoice() {
		return flagInvoice;
	}

	public void setFlagInvoice(int flagInvoice) {
		this.flagInvoice = flagInvoice;
	}

	
}
