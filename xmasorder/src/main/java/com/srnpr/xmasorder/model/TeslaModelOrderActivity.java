package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 订单活动信息
 * @author jlin
 *
 */
public class TeslaModelOrderActivity {

	private String uid  = ""  ;
    /**
     * 订单编号
     */
    private String orderCode  = ""  ;
    /**
     * 商品编号
     */
    private String productCode  = ""  ;
    /**
     * sku商品编号
     */
    private String skuCode  = ""  ;
    /**
     * 优惠的金钱
     */
    private BigDecimal preferentialMoney = BigDecimal.ZERO   ;
    /**
     * 活动编号
     */
    private String activityCode  = ""  ;
    /**
     * 活动类型
     */
    private String activityType  = ""  ;
    
    /**活动券码*/
    private String ticketCode = "";
    
    /**
     * 活动名称
     */
    private String activityName = "";
    
    /**
     * 外部活动号
     */
    private String outActiveCode="";
    
    @ZapcomApi(value="加价购商品标识 0否 1是")
	private String jjgFlag = "0";
    
    
	public String getJjgFlag() {
		return jjgFlag;
	}

	public void setJjgFlag(String jjgFlag) {
		this.jjgFlag = jjgFlag;
	}

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

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getOutActiveCode() {
		return outActiveCode;
	}

	public void setOutActiveCode(String outActiveCode) {
		this.outActiveCode = outActiveCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public BigDecimal getPreferentialMoney() {
		return preferentialMoney;
	}

	public void setPreferentialMoney(BigDecimal preferentialMoney) {
		this.preferentialMoney = preferentialMoney;
	}
	
}
