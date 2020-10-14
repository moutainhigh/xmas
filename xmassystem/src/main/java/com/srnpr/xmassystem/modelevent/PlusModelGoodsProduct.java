package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 拼好货基础实体类
 * @author zhouguohui
 *
 */
public class PlusModelGoodsProduct extends PlusModelEventInfo {

	@ZapcomApi(value="商品编号")
	private String productCode="";
	
	@ZapcomApi(value="sku编号")
	private String skuCode="";
	
	@ZapcomApi(value="sku名称")
	private String skuName="";
	
	@ZapcomApi(value="优惠价")
	private String favorablePrice="";
	
	@ZapcomApi(value="销售价",remark="无优惠的价格")
	private String sellingPrice="";
	
	@ZapcomApi(value="成团人数")
	private Integer purchaseNum=0;

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

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
	 * @return the sellingPrice
	 */
	public String getSellingPrice() {
		return sellingPrice;
	}

	/**
	 * @param sellingPrice the sellingPrice to set
	 */
	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * @return the purchaseNum
	 */
	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	/**
	 * @param purchaseNum the purchaseNum to set
	 */
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	
	
	
}
