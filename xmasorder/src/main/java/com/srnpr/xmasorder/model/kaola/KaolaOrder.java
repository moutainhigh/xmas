package com.srnpr.xmasorder.model.kaola;

import java.math.BigDecimal;

public class KaolaOrder {

	/**
	 * 大订单id
	 */
	private String id;
	
	/**
	 * 大订单金额，单位元	商品总售价
	 */
	private BigDecimal gpayAmount;
	
	/**
	 * 大订单原始应付金额 ，单位元	gorderAmount减去商品活动价后的总价
	 */
	private BigDecimal originalGpayAmount;
	
	/**
	 * 大订单实际金额，单位元	originalGpayAmount减去优惠券后的总价
	 */
	private BigDecimal gorderAmount;
	
	/**
	 * 大订单状态
	 */
	private int gorderStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getGpayAmount() {
		return gpayAmount;
	}

	public void setGpayAmount(BigDecimal gpayAmount) {
		this.gpayAmount = gpayAmount;
	}

	public BigDecimal getOriginalGpayAmount() {
		return originalGpayAmount;
	}

	public void setOriginalGpayAmount(BigDecimal originalGpayAmount) {
		this.originalGpayAmount = originalGpayAmount;
	}

	public BigDecimal getGorderAmount() {
		return gorderAmount;
	}

	public void setGorderAmount(BigDecimal gorderAmount) {
		this.gorderAmount = gorderAmount;
	}

	public int getGorderStatus() {
		return gorderStatus;
	}

	public void setGorderStatus(int gorderStatus) {
		this.gorderStatus = gorderStatus;
	}
	
	
}
