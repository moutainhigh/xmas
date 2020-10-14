package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

/**
 * 大订单信息
 * @author jlin
 *
 */
public class TeslaModelOrderInfoUpper {

	
	private String uid = "";

	/** 大订单编号 */
	private String bigOrderCode = "";
	/** 商家编码 */
	private String sellerCode = "";
	/** 买家编号 */
	private String buyerCode = "";
	/** 买家手机号 */
	private String buyerMobile = "";
	/** 订单金额 */
	private BigDecimal orderMoney = BigDecimal.ZERO;
	/** 应付款 */
	private BigDecimal dueMoney = BigDecimal.ZERO;
	/** 提交订单的app的版本 */
	private String appVersion = "";
	/** 已支付金额 */
	private BigDecimal payedMoney = BigDecimal.ZERO;
	/** 小订单个数 */
	private Integer orderNum = 0;

	/** 是否删除订单 0未删除 1删除 */
	private String deleteFlag = "0";
	/** 创建时间 */
	private String createTime = "";
	/** 更新时间 */
	private String updateTime = "";
	/** 付款方式 */
	private String payType = "";
	/** 总的总金额 */
	private BigDecimal allMoney = BigDecimal.ZERO;

	/**
	 * 订单来源       值				说明
	 *          449715190001	正常订单
	 *          449715190002	android订单
	 *          449715190003	ios订单
	 *          449715190004	网站手机订单
	 *          449715190009	百度外卖来源
	 */
	private String orderSource = "";
	
	/**
	 * 订单类型	值				说明
	 * 			449715200001	商城订单
	 * 			449715200002	好物产订单
	 * 			449715200003	试用订单
	 * 			449715200004	闪购订单
	 * 			449715200005	普通订单
	 *    		449715200015	百度外卖订单
	 */
	private String orderType = "";
	
	/**
	 * 商品总金额（确认订单展示使用）
	 * */
	private BigDecimal productMoney=BigDecimal.ZERO;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBigOrderCode() {
		return bigOrderCode;
	}

	public void setBigOrderCode(String bigOrderCode) {
		this.bigOrderCode = bigOrderCode;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getDueMoney() {
		return dueMoney;
	}

	public void setDueMoney(BigDecimal dueMoney) {
		this.dueMoney = dueMoney;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public BigDecimal getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(BigDecimal payedMoney) {
		this.payedMoney = payedMoney;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(BigDecimal allMoney) {
		this.allMoney = allMoney;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getProductMoney() {
		return productMoney;
	}

	public void setProductMoney(BigDecimal productMoney) {
		this.productMoney = productMoney;
	}

	public String getBuyerMobile() {
		return buyerMobile;
	}

	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}
	
}
