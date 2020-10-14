package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

/**
 * 内购商品过滤对象
 * @author zhouguohui
 *
 */
public class PlusModelNgProduct {
	/**product编号**/
	private String productCode="";
	/**sku编号**/
	private String skuCode="";
	
	/**活动编号**/
	private String eventCode="";
	/**系统唯一编号**/
	private String ItemCode="";
	
	/**sku成本价**/
	private BigDecimal skuCostPrice=BigDecimal.ZERO;
	/**商品销售价格   过滤除内购以外的活动价格**/
	private BigDecimal skuPrice = BigDecimal.ZERO;
	
	/****一下字段是返回用****/
	/**内购活动   是否为内购活动   true为满足内购**/
	private  boolean isTrue = false;

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
	 * @return the skuPrice
	 */
	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	/**
	 * @param skuPrice the skuPrice to set
	 */
	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}



	/**
	 * @return the isTrue
	 */
	public boolean isTrue() {
		return isTrue;
	}

	/**
	 * @param isTrue the isTrue to set
	 */
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}


	/**
	 * @return the skuCostPrice
	 */
	public BigDecimal getSkuCostPrice() {
		return skuCostPrice;
	}

	/**
	 * @param skuCostPrice the skuCostPrice to set
	 */
	public void setSkuCostPrice(BigDecimal skuCostPrice) {
		this.skuCostPrice = skuCostPrice;
	}

	/**
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}

	/**
	 * @param eventCode the eventCode to set
	 */
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return ItemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}

	
	
	
}
