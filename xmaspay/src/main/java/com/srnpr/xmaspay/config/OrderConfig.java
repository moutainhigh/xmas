package com.srnpr.xmaspay.config;

public class OrderConfig {

	/** 订单状态 - 下单成功 - 未支付 */
	public static final String ORDER_STATUS_1 = "4497153900010001";
	/** 订单状态 - 已支付-未发货 */
	public static final String ORDER_STATUS_2 = "4497153900010002";
	/** 订单状态 - 已发货 */
	public static final String ORDER_STATUS_3 = "4497153900010003";
	/** 订单状态 - 交易成功 */
	public static final String ORDER_STATUS_5 = "4497153900010005";
	/** 订单状态 - 交易失败 */
	public static final String ORDER_STATUS_6 = "4497153900010006";
	
	/** 货到付款 */
	public static final String PAY_TYPE_2 = "449716200002";
	
	/** 支付宝 */
	public static final String PAY_TYPE_3 = "449746280003";
	/** 微信支付 */
	public static final String PAY_TYPE_5 = "449746280005";
	/** 苹果支付 */
	public static final String PAY_TYPE_13 = "449746280013";
	/** 银联支付 */
	public static final String PAY_TYPE_14 = "449746280014";
	
	/** 跨境通 */
	public static final String SELL_CODE_KJT = "SF03KJT";
	/** 惠家有 */
	public static final String SELL_CODE_FAMILYHAS = "SI2003";
	/** 家有汇 */
	public static final String SELL_CODE_HPOOL = "SI2009";
}
