package com.srnpr.xmassystem.enumer;

/**
 * 惠豆定时执行类型枚举
 */
public enum HjyBeanExecType {
	
	/** 订单签收送惠豆 */
	SUCCESS("449747930001"),
	/** 订单取消返还使用的惠豆 (未生成退款单情况) */
	CANCEL("449747930002"),
	/** 订单退款返还惠豆 */
	RETURN_MONEY("449747930003"),
	/** 订单退货返还惠豆 (0元单且未生成退款单情况) */
	RETURN_GOODS("449747930004");
	
	private String type;
	
	HjyBeanExecType(String type){
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
