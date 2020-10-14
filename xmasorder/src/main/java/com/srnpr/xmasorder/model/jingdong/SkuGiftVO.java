package com.srnpr.xmasorder.model.jingdong;

public class SkuGiftVO {

	/**
	 * 购买主商品最大数量（为0表示没配置）
	 * （若购买数量大于赠品要求最多购买数量，则不提供赠品）
	 */
	private int maxNum;
	
	/**
	 * 购买主商品最小数量（为0表示没配置）
	 * （若购买数量小于赠品要求最少购买数量，则不提供赠品）
	 */
	private int minNum;
	
	/**
	 * 促销开始时间（若下单时间不在促销时间范围内，则不提供赠品）
	 */
	private long promoStartTime;
	
	/**
	 * 促销结束时间（若下单时间不在促销时间范围内，则不提供赠品）
	 */
	private long promoEndTime;
	
	/**
	 * 商品编号
	 */
	private String skuId;
	
	/**
	 * 数量
	 */
	private int num; 
	
	/**
	 * 1：附件 2：赠品
	 */
	private int giftType;

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public long getPromoStartTime() {
		return promoStartTime;
	}

	public void setPromoStartTime(long promoStartTime) {
		this.promoStartTime = promoStartTime;
	}

	public long getPromoEndTime() {
		return promoEndTime;
	}

	public void setPromoEndTime(long promoEndTime) {
		this.promoEndTime = promoEndTime;
	}

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

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	
}
