package com.srnpr.xmassystem.modelorder;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderInfo {

	@ZapcomApi(value = "订单编号")
	private String orderCode;

	@ZapcomApi(value = "订单来源")
	private String orderSource;
	@ZapcomApi(value = "订单类型")
	private String orderType;

	@ZapcomApi(value = "订单状态")
	private String orderStatus;

	@ZapcomApi(value = "系统编号")
	private String sellerCode;

	@ZapcomApi(value = "买家编号")
	private String buyerCode;

	@ZapcomApi(value = "支付方式")
	private String payType;

	@ZapcomApi(value = "配送方式 ")
	private String sendType;

	@ZapcomApi(value = " 商品金额")
	private BigDecimal productMoney;

	@ZapcomApi(value = "配送金额")
	private BigDecimal transportMoney;

	@ZapcomApi(value = "活动金额")
	private BigDecimal promotionMoney;

	@ZapcomApi(value = "订单金额")
	private BigDecimal orderMoney;

	@ZapcomApi(value = "已支付金额")
	private BigDecimal payedMoney;

	@ZapcomApi(value = "创建时间")
	private String createTime;

	@ZapcomApi(value = "更新时间")
	private String updateTime;

	@ZapcomApi(value = "商品名称")
	private String productName;

	@ZapcomApi(value = "免运费金额")
	private BigDecimal freeTransportMoney;

	@ZapcomApi(value = "应付金额")
	private BigDecimal dueMoney;

	@ZapcomApi(value = "订单渠道编号")
	private String orderChannel;

	@ZapcomApi(value = "APP版本")
	private String appVersion;

	@ZapcomApi(value = "删除标记")
	private String deleteFlag;

	@ZapcomApi(value = "外部订单编号")
	private String outOrderCode;

	@ZapcomApi(value = "合并订单号")
	private String bigOrderCode;

	@ZapcomApi(value = "扩展状态")
	private String orderStatusExt;

	@ZapcomApi(value = "子商家编号")
	private String smallSellerCode;

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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public BigDecimal getProductMoney() {
		return productMoney;
	}

	public void setProductMoney(BigDecimal productMoney) {
		this.productMoney = productMoney;
	}

	public BigDecimal getTransportMoney() {
		return transportMoney;
	}

	public void setTransportMoney(BigDecimal transportMoney) {
		this.transportMoney = transportMoney;
	}

	public BigDecimal getPromotionMoney() {
		return promotionMoney;
	}

	public void setPromotionMoney(BigDecimal promotionMoney) {
		this.promotionMoney = promotionMoney;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(BigDecimal payedMoney) {
		this.payedMoney = payedMoney;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getFreeTransportMoney() {
		return freeTransportMoney;
	}

	public void setFreeTransportMoney(BigDecimal freeTransportMoney) {
		this.freeTransportMoney = freeTransportMoney;
	}

	public BigDecimal getDueMoney() {
		return dueMoney;
	}

	public void setDueMoney(BigDecimal dueMoney) {
		this.dueMoney = dueMoney;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getOutOrderCode() {
		return outOrderCode;
	}

	public void setOutOrderCode(String outOrderCode) {
		this.outOrderCode = outOrderCode;
	}

	public String getBigOrderCode() {
		return bigOrderCode;
	}

	public void setBigOrderCode(String bigOrderCode) {
		this.bigOrderCode = bigOrderCode;
	}

	public String getOrderStatusExt() {
		return orderStatusExt;
	}

	public void setOrderStatusExt(String orderStatusExt) {
		this.orderStatusExt = orderStatusExt;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
