package com.srnpr.xmasorder.model.jingdong;

import java.math.BigDecimal;

/**
 * 下单成功 响应的商品信息
 * @remark 
 * @author 任宏斌
 * @date 2019年5月23日
 */
public class OrderDetailSkuRepVO {

    /**
     * 货品编号 
     */
	private String skuId = "";

	/**
	 * 数量
	 */
	private int num = 0;
	
	/**
	 * 类别
	 */
	private String category = "";
	
	/**
	 * 价格
	 */
	private BigDecimal price = BigDecimal.ZERO;
	
	/**
	 * 名称
	 */
	private String name = "";
	
	/**
	 * 税种
	 */
	private int tax = 0;
	
	/**
	 * 税额
	 */
	private BigDecimal taxPrice = BigDecimal.ZERO;
	
	/**
	 * 裸价
	 */
	private BigDecimal nakedPrice = BigDecimal.ZERO;
	
	/**
	 * 类别
	 */
	private int type = 0;
	
	/**
	 * 父商品ID
	 */
	private String oid = "";

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

	public BigDecimal getNakedPrice() {
		return nakedPrice;
	}

	public void setNakedPrice(BigDecimal nakedPrice) {
		this.nakedPrice = nakedPrice;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
	
	
}
