package com.srnpr.xmasorder.channel.model;

import java.math.BigDecimal;

/**
 * 小单信息
 * @remark
 * @author 任宏斌
 * @date 2019年12月2日
 */
public class PorscheModelOrderInfo {

	private String uid = "";
	
	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 订单来源
	 * 449715190001 正常订单 
	 * 449715190002 android订单 
	 * 449715190003 ios订单
	 * 449715190004 网站手机订单
	 */
	private String orderSource = "";
	
	/**
	 * 订单类型
	 * 449715200001 商城订单 
	 * 449715200002 好物产订单 
	 * 449715200003 试用订单 
	 * 449715200004 闪购订单 
	 * 449715200005 普通订单
	 */
	private String orderType = "";
	
	/**
	 * 
	 * 订单状态
	 * 4497153900010001 下单成功-未付款 
	 * 4497153900010002 下单成功-未发货
	 * 4497153900010003 已发货 
	 * 4497153900010004 已收货 
	 * 4497153900010005 交易成功
	 * 4497153900010006 交易失败
	 * 
	 */
	private String orderStatus = "4497153900010001";
	
	/**
	 * 商家编码
	 */
	private String sellerCode = "";
	
	/**
	 * 买家编号
	 */
	private String buyerCode = "";
	
	/**
	 * 支付方式 
	 * 449716200001 在线支付 
	 * 449716200002 货到付款
	 */
	private String payType = "";
	
	/**
	 * 配送方式  
	 * 449715210001 快递 
	 * 449715210002 邮局
	 */
	private String sendType = "";
	
	/**
	 * 商品运费(实际运费)
	 */
	private BigDecimal transportMoney = BigDecimal.ZERO;
	
	/**
	 * 商品活动金额
	 */
	private BigDecimal promotionMoney = BigDecimal.ZERO;
	
	/**
	 * 商品总金额
	 */
	private BigDecimal productMoney = BigDecimal.ZERO;
	
	/**
	 * 订单金额=商品金额+商品运费-商品活动金额-
	 */
	private BigDecimal orderMoney = BigDecimal.ZERO;
	
	/**
	 * 已支付金额
	 */
	private BigDecimal payedMoney = BigDecimal.ZERO;
	
	/**
	 * 应付款
	 */
	private BigDecimal dueMoney = BigDecimal.ZERO;
	
	/**
	 * 创建时间
	 */
	private String createTime = "";
	
	/**
	 * 更新时间
	 */
	private String updateTime = "";
	
	/**
	 * 小卖家编号
	 */
	private String smallSellerCode = "";
	
	/**
	 * 所有商品名字
	 */
	private String productName = "";
	
	/**
	 * 免运费金额（原始运费）
	 */
	private BigDecimal freeTransportMoney = BigDecimal.ZERO;
	
	/**
	 * 订单来源频道
	 */
	private String orderChannel = "";
	
	/**
	 * 外部订单编号
	 */
	private String outOrderCode = "";
	
	/**
	 * 大订单编号
	 */
	private String bigOrderCode = "";
	
	/**
	 * 是否删除订单
	 */
	private String deleteFlag = "1";
	
	/**
	 * 订单辅助状态
	 */
	private String orderStatusExt = "4497153900140002";
	
	/**
	 * 序列号
	 */
	private String orderSeq = "";

	/**
	 * 抄低价订单 
	 * 449747110001 否  
	 * 449747110002 是
	 */
	private String lowOrder = "";
	
	/**
	 * 发货仓 目前全是商家发货
	 */
	private String delivery_store_type = "4497471600430001";

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

	public BigDecimal getProductMoney() {
		return productMoney;
	}

	public void setProductMoney(BigDecimal productMoney) {
		this.productMoney = productMoney;
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

	public BigDecimal getDueMoney() {
		return dueMoney;
	}

	public void setDueMoney(BigDecimal dueMoney) {
		this.dueMoney = dueMoney;
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

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
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

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
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

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getOrderStatusExt() {
		return orderStatusExt;
	}

	public void setOrderStatusExt(String orderStatusExt) {
		this.orderStatusExt = orderStatusExt;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getLowOrder() {
		return lowOrder;
	}

	public void setLowOrder(String lowOrder) {
		this.lowOrder = lowOrder;
	}

}
