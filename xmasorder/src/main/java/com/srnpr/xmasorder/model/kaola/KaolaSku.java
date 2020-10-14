package com.srnpr.xmasorder.model.kaola;

import java.math.BigDecimal;

public class KaolaSku {
	/**
	 * skuId
	 */
	private String skuId;
	
	/**
	 * 考拉售价
	 */
	private BigDecimal actualCurrentPrice;
	
	/**
	 * 行邮税，忽略
	 */
	private BigDecimal xyTaxRate;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getActualCurrentPrice() {
		return actualCurrentPrice;
	}

	public void setActualCurrentPrice(BigDecimal actualCurrentPrice) {
		this.actualCurrentPrice = actualCurrentPrice;
	}

	public BigDecimal getXyTaxRate() {
		return xyTaxRate;
	}

	public void setXyTaxRate(BigDecimal xyTaxRate) {
		this.xyTaxRate = xyTaxRate;
	}
}
