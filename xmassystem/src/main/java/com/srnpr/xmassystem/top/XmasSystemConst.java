package com.srnpr.xmassystem.top;

public class XmasSystemConst {

	/**
	 * 活动明细起始编号
	 */
	public final static String EVENT_ITEM_START = "IC";

	/**
	 * 特定活动分隔符 分隔规则为IC-活动类型-实际编号
	 */
	public final static String EVENT_SPLIT_LINE = "_";

	/**
	 * 生效的活动的状态值 002为发布状态
	 */
	public final static String ACTIVE_EVENT_STATUS = "4497472700020002";
	
	/**
	 * 生效的活动的状态值 004为暂停状态  暂停状态等同于发布状态
	 */
	public final static String ACTIVE_EVENT_STATUS_STOP = "4497472700020004";

	/**
	 * 锁定库存的截止时间
	 */
	public final static String LOCK_STOCK_TIME = "locak_stock_time";

	/**
	 * 锁定库存的开始时间
	 */
	public final static String LOCK_START_TIME = "locak_start_time";

	/**
	 * 锁定库存的锁定数量
	 */
	public final static String LOCK_STOCK_NUM = "lock_stock_num";

	/**
	 * 锁定库存的来源 一般为活动明细编号
	 */
	public final static String LOCK_STOCK_SOURCE = "lock_stock_source";

	/**
	 * SKU的库存的过期时间 过期后从数据库初始化
	 */
	public final static int STOCK_TTL_TIME = 600;

	/**
	 * SKU的价格校验 如果价格小于0 则强制赋为这个猥琐的价格
	 */
	public final static String DEFAULT_PRICE_BASE = "987986981.02";

	/*
	 * SKU库存缓存中用于复制商品的标记位
	 */
	public final static String CONCAT_STOCK_START = "++";

	/**
	 * 订单取消时间(秒),默认24小时
	 */
	//public final static long CANCEL_ORDER_TIME = 7200;
	public final static long CANCEL_ORDER_TIME = 86400;
	
	/**
	 * 订单取消时间(秒),默认20分钟
	 */
	public final static long CANCEL_ORDER_KAOLA_TIME = 1200;
	
	/**
	 * 扫码购编号
	 */
	public final static String SMG_CODE = "4497472600010004";
	
	/**
	 * 型录编号
	 */
	public final static String DM_CODE = "4497472600010009";
	
	/**
	 * 生鲜编号
	 */
	public final static String BJTV_CODE = "4497472600010011";
	
	/**
	 * 渠道合并
	 */
	public final static String QD_CODE = "4497472600010012";
	
	
	/**
	 * APP扫码
	 */
	public final static String APPSMG_CODE = "4497472600010015";
	
	/**
	 * 满折活动 (满减子活动)
	 */
	public final static String[] MJ_MANZHE = {"449747630006","449747630007"};
	
	/**
	 * 默认家有客户等级： 10 顾客
	 */
	public final static String CUST_LVL_CD = "10";
}
