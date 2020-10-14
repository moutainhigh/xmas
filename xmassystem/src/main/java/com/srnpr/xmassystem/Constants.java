package com.srnpr.xmassystem;

public class Constants {
	
	/**京东特定商户编号*/
	public final static String SMALL_SELLER_CODE_JD = "SF031JDSC";
	
	/**执行类型：同步售后申请到京东*/
	public final static String ZA_EXEC_TYPE_JD_AFTER_SALE_CREATE = "449746990015";
	
	/**调用京东接口成功响应码*/
	public final static String SUCCESS_RESULT_CODE = "0000";
	/**下单成功响应码*/
	public final static String SUCCESS_CREATE_ORDER_CODE = "0001";
	/**重复下单响应码*/
	public final static String REPETI_CREATE_ORDER_CODE = "0008";
	
	/**接口地址*/
	/**查询商品是否可售接口*/
	public final static String GET_SALE_STATE = "biz.product.sku.check";
	/**查询商品价格接口*/
	public final static String GET_SKU_PRICE = "biz.price.sellPrice.get";
	/**查询商品库存接口*/
	public final static String GET_SKU_STOCK = "jd.kpl.open.getnewstockbyid.query";
	/**查询商品区域限制接口*/
	public final static String GET_SKU_AREA_LIMIT = "biz.product.checkAreaLimit.query";
	/**查询商品赠品信息接口*/
	public final static String GET_SKU_GIFT = "biz.product.skuGift.query";
	/**京东统一下单接口*/
	public final static String CREATE_JD_ORDER = "biz.order.unite.submit";
	
	/**订单操作锁定前缀*/
	public static final String LOCK_ORDER_UPDATE = "LockOrderUpdate-";
	
	/**分销利润锁定前缀*/
	public static final String LOCK_AGENT = "LockAgent-";
	
	/**用户提现锁定前缀*/
	public static final String LOCK_CASH = "LockCash-";
	
	/**适用于大图展示，最大宽度1080*/
	public static final int IMG_WIDTH_SP00 = 1080;
	/**适用于1栏模版，最大宽度750*/
	public static final int IMG_WIDTH_SP01 = 750;
	/**适用于2栏模版，最大宽度570*/
	public static final int IMG_WIDTH_SP02 = 570;
	/**适用于3栏模版，最大宽度400*/
	public static final int IMG_WIDTH_SP03 = 400;
	/**适用于4栏模版，最大宽度248*/
	public static final int IMG_WIDTH_SP04 = 248;
	/**适用于5栏模版，最大宽度200*/
	public static final int IMG_WIDTH_SP05 = 200;
	
	
	/**惠惠农场相关操作的锁前缀*/
	/**水滴锁*/
	public static final String WATER_PREFIX = "Water-";
	/**果树锁*/
	public static final String TREE_PREFIX = "Tree-";
	/**水壶锁*/
	public static final String KETTLE_PREFIX = "Kettle-";
	/**生成任务锁*/
	public static final String TASK_PREFIX = "Task-";
}
