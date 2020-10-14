package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelPurchase extends PlusModelEventInfo {
	@ZapcomApi(value="内购描述")
	private String skuName="";
	
	@ZapcomApi(value="内购价格",remark="在skuCode 的成本价基础上加多少钱")
	private String favorablePrice="";
	
	@ZapcomApi(value="IC编号")
	private String itemCode="";
	
	/**
	 * @return the skuName
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * @param skuName the skuName to set
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * @return the favorablePrice
	 */
	public String getFavorablePrice() {
		return favorablePrice;
	}

	/**
	 * @param favorablePrice the favorablePrice to set
	 */
	public void setFavorablePrice(String favorablePrice) {
		this.favorablePrice = favorablePrice;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	
}
