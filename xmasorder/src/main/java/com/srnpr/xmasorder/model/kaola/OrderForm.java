package com.srnpr.xmasorder.model.kaola;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderForm {
	/**
	 * 商品总金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 总支付金额（含税含运）
	 */
	private BigDecimal payAmount;

	/**
	 * 总税费
	 */
	private BigDecimal taxPayAmount;

	/**
	 * 总运费
	 */
	private BigDecimal logisticsPayAmount;
	
	private int needVerifyLevel;
	
	/**
	 * 预计关单时间（正式下单未支付保留的时间）,单位：秒
	 */
	private int orderCloseTime;
	
	private List<Packages> packageList = new ArrayList<Packages>();

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getTaxPayAmount() {
		return taxPayAmount;
	}

	public void setTaxPayAmount(BigDecimal taxPayAmount) {
		this.taxPayAmount = taxPayAmount;
	}

	public BigDecimal getLogisticsPayAmount() {
		return logisticsPayAmount;
	}

	public void setLogisticsPayAmount(BigDecimal logisticsPayAmount) {
		this.logisticsPayAmount = logisticsPayAmount;
	}

	public int getNeedVerifyLevel() {
		return needVerifyLevel;
	}

	public void setNeedVerifyLevel(int needVerifyLevel) {
		this.needVerifyLevel = needVerifyLevel;
	}

	public int getOrderCloseTime() {
		return orderCloseTime;
	}

	public void setOrderCloseTime(int orderCloseTime) {
		this.orderCloseTime = orderCloseTime;
	}

	public List<Packages> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<Packages> packageList) {
		this.packageList = packageList;
	}
}
