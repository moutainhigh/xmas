package com.srnpr.xmassystem.modelorder;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderEvent {

	@ZapcomApi(value = "订单编号")
	private String orderCode;

	@ZapcomApi(value = "商品编号")
	private String productCode;

	@ZapcomApi(value = "SKU编号")
	private String skuCode;

	@ZapcomApi(value = "优惠金额")
	private BigDecimal preferentialMoney;

	@ZapcomApi(value = "活动编号")
	private String activityCode;

	@ZapcomApi(value = "活动类型")
	private String activityType;

	@ZapcomApi(value = "外部活动编号")
	private String outActiveCode;

	@ZapcomApi(value = "第三方编码")
	private String ticketCode;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getPreferentialMoney() {
		return preferentialMoney;
	}

	public void setPreferentialMoney(BigDecimal preferentialMoney) {
		this.preferentialMoney = preferentialMoney;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getOutActiveCode() {
		return outActiveCode;
	}

	public void setOutActiveCode(String outActiveCode) {
		this.outActiveCode = outActiveCode;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

}
