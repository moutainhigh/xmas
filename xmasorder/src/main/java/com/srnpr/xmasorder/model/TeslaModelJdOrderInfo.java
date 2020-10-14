package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

/**
 * 京东订单信息
 * @remark 
 * @author 任宏斌
 * @date 2019年5月15日
 */
public class TeslaModelJdOrderInfo {

	private String uid = "";
	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 京东订单编号
	 */
	private String jdOrderId = "";
	
	/**
	 * 京东订单状态
	 */
	private int orderState = -1;
	
	/**
	 * 京东订单状态
	 */
	private int jdOrderState = 0;
	
	/**
	 * 京东订单物流状态
	 */
	private int state = 0;
	
	/**
	 * 惠家有sku编号
	 */
	private String skuCode = "";
	
	/**
	 * 京东sku编号
	 */
	private String skuId = "";
	
	/**
	 * sku数量
	 */
	private int skuNum = 0;
	
	/**
	 * 京东订单金额
	 */
	private BigDecimal orderPrice = BigDecimal.ZERO;
	
	/**
	 * 京东订单裸价
	 */
	private BigDecimal orderNakedPrice = BigDecimal.ZERO;
	
	/**
	 * 京东订单税额
	 */
	private BigDecimal orderTaxPrice = BigDecimal.ZERO;
	
	/**
	 * 京东订单运费
	 */
	private BigDecimal freight = BigDecimal.ZERO;
	
	/**
	 * 税率
	 */
	private int tax = 0;
	
	/**
	 * 京东一级地址编号
	 */
	private String province = "0";
	
	/**
	 * 京东二级地址编号
	 */
	private String city = "0";
	
	/**
	 * 京东三级地址编号
	 */
	private String county = "0";
	
	/**
	 * 京东四级地址编号
	 */
	private String town = "0";
	
	/**
	 * 创建时间
	 */
	private String createTime = "";
	
	/**
	 * 修改时间
	 */
	private String updateTime = "";

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

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public int getJdOrderState() {
		return jdOrderState;
	}

	public void setJdOrderState(int jdOrderState) {
		this.jdOrderState = jdOrderState;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderNakedPrice() {
		return orderNakedPrice;
	}

	public void setOrderNakedPrice(BigDecimal orderNakedPrice) {
		this.orderNakedPrice = orderNakedPrice;
	}

	public BigDecimal getOrderTaxPrice() {
		return orderTaxPrice;
	}

	public void setOrderTaxPrice(BigDecimal orderTaxPrice) {
		this.orderTaxPrice = orderTaxPrice;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
