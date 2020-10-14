package com.srnpr.xmassystem.modelorder;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderDetail {

	@ZapcomApi(value = "订单编号")
	private String orderCode;

	@ZapcomApi(value = "SKU编号")
	private String skuCode;

	@ZapcomApi(value = "商品编号")
	private String productCode;

	@ZapcomApi(value = "SKU名称")
	private String skuName;

	@ZapcomApi(value = "SKU价格")
	private BigDecimal skuPrice;

	@ZapcomApi(value = "购买数量")
	private Integer skuNum;

	@ZapcomApi(value = "商品图片")
	private String productPicurl;

	@ZapcomApi(value = "明细编号")
	private String detailCode;

	@ZapcomApi(value = "仓库编号")
	private String storeCode;

	@ZapcomApi(value = "赠品标记")
	private String giftFlag;

	@ZapcomApi(value = "赠品类型")
	private String giftCd;

	@ZapcomApi(value = "优惠金额")
	private BigDecimal saveAmt;

	@ZapcomApi(value = "成本价")
	private BigDecimal costPrice;

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

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public String getProductPicurl() {
		return productPicurl;
	}

	public void setProductPicurl(String productPicurl) {
		this.productPicurl = productPicurl;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
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

	public BigDecimal getSaveAmt() {
		return saveAmt;
	}

	public void setSaveAmt(BigDecimal saveAmt) {
		this.saveAmt = saveAmt;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

}
