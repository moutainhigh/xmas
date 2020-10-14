package com.srnpr.xmasorder.channel.model;

public class PorscheModelChannelOrder {

	private String uid = "";
	
	/**
	 * 渠道商编号
	 */
	private String channelSellerCode = "";
	
	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 第三方订单编号
	 */
	private String outOrderCode = "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getChannelSellerCode() {
		return channelSellerCode;
	}

	public void setChannelSellerCode(String channelSellerCode) {
		this.channelSellerCode = channelSellerCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOutOrderCode() {
		return outOrderCode;
	}

	public void setOutOrderCode(String outOrderCode) {
		this.outOrderCode = outOrderCode;
	}
}
