package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

/***
 * 用于价格过滤实体类
 * @author zhouguohui
 *
 */
public class PlusModelSku {
	private String productCode="";
	
	private String skuCode = "";

	private int skuNum=0;
	
    private BigDecimal skuPrice=BigDecimal.ZERO;
    
    private String choose_flag="0";
    
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
	 * @return the skuNum
	 */
	public int getSkuNum() {
		return skuNum;
	}

	/**
	 * @param skuNum the skuNum to set
	 */
	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
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

	public String getChoose_flag() {
		return choose_flag;
	}

	public void setChoose_flag(String choose_flag) {
		this.choose_flag = choose_flag;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	

}
