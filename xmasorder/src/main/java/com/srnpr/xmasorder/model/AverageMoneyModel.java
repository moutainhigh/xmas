package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 平摊商品金额所用model
 * 
 * @author fq
 *
 */
public class AverageMoneyModel {

	@ZapcomApi(value = "sku编号")
	private String skuCode = "";
	
	@ZapcomApi(value = "sku金额")
	private Double skuPrice = 0.00;
	
	@ZapcomApi(value = "sku数量")
	private Integer skuNum = 0;
	
	@ZapcomApi(value = "所属商户编号")
	private String smallSellerCode = "";

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Double getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(Double skuPrice) {
		this.skuPrice = skuPrice;
	}

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}
	
	

}
