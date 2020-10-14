package com.srnpr.xmassystem.enumer;

public enum EPlusScheduler {

	/**
	 * 库存变动日志
	 */
	StockChangeLog,

	/**
	 * 创建订单
	 */
	CreateOrder,
	
	/**
	 * 微公社余额支付变动日志
	 */
	GroupMoneyChangeLog,
	
	/**
	 * 刷新活动商品solr索引库
	 */
	UpdateEventsGoods,
	
	/**
	 * 加载商品图片宽高到缓存
	 */
	ProductImageWidth,
	
	/**
	 * 刷新优惠券限定商品solr索引库
	 */
	UpdateCouponGoods

}
