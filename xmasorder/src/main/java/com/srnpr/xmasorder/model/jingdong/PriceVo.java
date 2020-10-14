package com.srnpr.xmasorder.model.jingdong;

import java.math.BigDecimal;

/**
 * 1.2.2	批量查询价格 响应VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class PriceVo {

	private String skuId;
	
	/**
	 * 商品京东价格（无实际参考意义）
	 */
	private BigDecimal jdPrice;
	
	/**
	 * 商品协议价格（开普勒与客户签订的商品协议价）
	 */
	private BigDecimal price;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getJdPrice() {
		return jdPrice;
	}

	public void setJdPrice(BigDecimal jdPrice) {
		this.jdPrice = jdPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
