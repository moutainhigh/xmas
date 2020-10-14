package com.srnpr.xmasorder.model.kaola;

import java.math.BigDecimal;


public class KaolaGoods {
	/**
	 * 商品ID
	 */
	private Long goodsId;
	
	/**
	 * 商品SKUID
	 */
	private String skuId;
	
	/**
	 * 商品SKU
	 */
	private KaolaSku sku = new KaolaSku();
	
	/**
	 * 仓库ID
	 */
	private int warehouseId;
	
	/**
	 * 商品单个金额(不含税)
	 */
	private BigDecimal goodsUnitPriceWithoutTax;
	
	/**
	 * 商品税（含运费税）（同一个包裹中多个商品，按比例分摊运费）
	 * 运费分摊：（该商品的总价格/该订单税前商品总价格）*总运费
	 * 比如一个商品A是40块，税率是11.9%，一个商品B是60块，税率是5%，两个商品一起买的话，
	 * 总的运费是10块，然后计算运费的税的时候，把这10块摊到每个商品上，
	 * 比如A商品摊4块，A商品运费税4*11.9%，B商品摊6块，B商品运费税6*5%
	 */
	private BigDecimal goodsTaxAmount;
	
	/**
	 * 商品总金额（不含税）
	 */
	private BigDecimal goodsPayAmount;
	
	/**
	 * 商品数量
	 */
	private int goodsBuyNumber;
	
	/**
	 * 商品图片
	 */
	private String imageUrl;
	
	/**
	 * 综合税率，即下单的真实税率
	 */
	private BigDecimal composeTaxRate;
	
	

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public KaolaSku getSku() {
		return sku;
	}

	public void setSku(KaolaSku sku) {
		this.sku = sku;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public BigDecimal getGoodsUnitPriceWithoutTax() {
		return goodsUnitPriceWithoutTax;
	}

	public void setGoodsUnitPriceWithoutTax(BigDecimal goodsUnitPriceWithoutTax) {
		this.goodsUnitPriceWithoutTax = goodsUnitPriceWithoutTax;
	}

	public BigDecimal getGoodsTaxAmount() {
		return goodsTaxAmount;
	}

	public void setGoodsTaxAmount(BigDecimal goodsTaxAmount) {
		this.goodsTaxAmount = goodsTaxAmount;
	}

	public BigDecimal getGoodsPayAmount() {
		return goodsPayAmount;
	}

	public void setGoodsPayAmount(BigDecimal goodsPayAmount) {
		this.goodsPayAmount = goodsPayAmount;
	}

	public int getGoodsBuyNumber() {
		return goodsBuyNumber;
	}

	public void setGoodsBuyNumber(int goodsBuyNumber) {
		this.goodsBuyNumber = goodsBuyNumber;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BigDecimal getComposeTaxRate() {
		return composeTaxRate;
	}

	public void setComposeTaxRate(BigDecimal composeTaxRate) {
		this.composeTaxRate = composeTaxRate;
	}
}
