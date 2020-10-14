package com.srnpr.xmasorder.channel.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PorscheModelSubRole {

	/**
	 * 拆单规则
	 */
	private String subRole = "";
	
	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 本订单下的skuCode
	 */
	private List<String>  skus= new ArrayList<String>();

	/**
	 * 本单运费
	 */
	private BigDecimal  transportMoney = BigDecimal.ZERO;
	
	public String getSubRole() {
		return subRole;
	}

	public void setSubRole(String subRole) {
		this.subRole = subRole;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	public BigDecimal getTransportMoney() {
		return transportMoney;
	}

	public void setTransportMoney(BigDecimal transportMoney) {
		this.transportMoney = transportMoney;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
}
