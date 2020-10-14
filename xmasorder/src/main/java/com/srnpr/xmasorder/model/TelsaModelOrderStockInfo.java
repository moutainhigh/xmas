package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 确认订单，如果是单件商品订单，返回库存信息
 * @author cc
 *
 */
public class TelsaModelOrderStockInfo {

	@ZapcomApi(value = "库存", remark = "0")
	private Integer stockNumSum = 0;
	
	@ZapcomApi(value = "购买下限", remark = "0")
	private Integer miniOrder = 0;
	
	@ZapcomApi(value = "购买上限", remark = "0")
	private Integer maxBuyCount = 0;

	@ZapcomApi(value = "每用户限购数",remark="")
    private Integer limitBuy = 99;
	
	@ZapcomApi(value = "是否显示限购数，0：不显示，1显示")
	private int showLimitNum=0;
	
	public Integer getStockNumSum() {
		return stockNumSum;
	}

	public void setStockNumSum(Integer stockNumSum) {
		this.stockNumSum = stockNumSum;
	}

	public Integer getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(Integer miniOrder) {
		this.miniOrder = miniOrder;
	}

	public Integer getMaxBuyCount() {
		return maxBuyCount;
	}

	public void setMaxBuyCount(Integer maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}

	public Integer getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(Integer limitBuy) {
		this.limitBuy = limitBuy;
	}

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}
	
}
