package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

/**
 * 调价明细实体类
 */
public class PlusModelSkuPriceFlow {

	private String flowCode;
	private BigDecimal costPriceOld;
	private BigDecimal costPrice;
	private BigDecimal sellPriceOld;
	private BigDecimal sellPrice;
	private String startTime;
	private String endTime;
	
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public BigDecimal getCostPriceOld() {
		return costPriceOld;
	}
	public void setCostPriceOld(BigDecimal costPriceOld) {
		this.costPriceOld = costPriceOld;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getSellPriceOld() {
		return sellPriceOld;
	}
	public void setSellPriceOld(BigDecimal sellPriceOld) {
		this.sellPriceOld = sellPriceOld;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
