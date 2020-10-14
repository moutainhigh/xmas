package com.srnpr.xmasorder.model.jingdong;

public class StockNewResultVo {

	private String skuId;
	
	/**
	 * 地址编号 格式：一级_二级_三级_四级
	 */
	private String areaId;
	
	/**
	 * 库存状态编号 33,39,40,36,34
	 */
	private int stockStateId;
	
	/**
	 * 库存状态描述： 
	 *  33 有货 现货-下单立即发货
	 *	34 无货
	 *	39 有货 在途-正在内部配货，预计2~6天到达本仓库 
	 *	40 有货 可配货-下单后从有货仓库配货 
	 *	36 预订
	 */
	private String stockStateDesc;
	
	/**
	 *  剩余数量
	 *  当库存小于 50 时展示真实库存数量(如果商品是 厂商直送商品，无论是否有货都返回-1)；
	 *	当库存大于等于 50 且小于100时，剩余数量 -1 未知；
	 *	当库存在100以上不允许查，无论入参的num传多少，系统都直接返回有货，且remainNum=入参的skuNums
	 */
	private int remainNum;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public int getStockStateId() {
		return stockStateId;
	}

	public void setStockStateId(int stockStateId) {
		this.stockStateId = stockStateId;
	}

	public String getStockStateDesc() {
		return stockStateDesc;
	}

	public void setStockStateDesc(String stockStateDesc) {
		this.stockStateDesc = stockStateDesc;
	}

	public int getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}
	
}
