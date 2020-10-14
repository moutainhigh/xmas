package com.srnpr.xmasorder.channel.model;

import java.math.BigDecimal;

/**
 * 订单详情
 * @remark
 * @author 任宏斌
 * @date 2019年12月2日
 */
public class PorscheModelOrderDetail {

	private String uid = "";

	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 产品编号
	 */
	private String skuCode = "";
	
	/**
	 * 商品编号
	 */
	private String productCode = "";

	/**
	 * 产品名称
	 */
	private String skuName = "";

	/**
	 * 产品价格
	 */
	private BigDecimal skuPrice = BigDecimal.ZERO;

	/**
	 * 产品价格（所有优惠前的商品售价）
	 */
	private BigDecimal sellPrice = BigDecimal.ZERO;

	/**
	 * 产品数量
	 */
	private int skuNum = 1;

	/**
	 * 商品的主图url
	 */
	private String productPicUrl = "";

	/** 
	 * 仓库代码 
	 */
	private String storeCode = "";

	/** 
	 * 赠品标示 1:不是赠品 0：是赠品 
	 */
	private String giftFlag = "1";

	/** 
	 * 赠品类别 
	 */
	private String giftCd = "";

	/**
	 * 成本价
	 */
	private BigDecimal costPrice = BigDecimal.ZERO;

	/**
	 * 显示
	 */
	private BigDecimal showPrice = BigDecimal.ZERO;

	/** 
	 * 外部商品编号 
	 */
	private String productCodeOut = "";

	/**
	 * 税率
	 */
	private BigDecimal taxRate = BigDecimal.ZERO;

	/**
	 * 商户编号
	 */
	private String smallSellerCode = "";

	/**
	 * 是否原价购买
	 */
	private String isSkuPriceToBuy = "0";

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

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(String giftFlag) {
		this.giftFlag = giftFlag;
	}

	public String getGiftCd() {
		return giftCd;
	}

	public void setGiftCd(String giftCd) {
		this.giftCd = giftCd;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}

	public String getProductCodeOut() {
		return productCodeOut;
	}

	public void setProductCodeOut(String productCodeOut) {
		this.productCodeOut = productCodeOut;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getIsSkuPriceToBuy() {
		return isSkuPriceToBuy;
	}

	public void setIsSkuPriceToBuy(String isSkuPriceToBuy) {
		this.isSkuPriceToBuy = isSkuPriceToBuy;
	}

}
