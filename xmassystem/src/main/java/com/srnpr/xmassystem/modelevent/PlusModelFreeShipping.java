package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelFreeShipping extends PlusModelEventInfo{

	@ZapcomApi(value="首单满减",remark="是否首单满减： 449747710001 为无限制  449747710002为首单满减")
	private String firstOrder="";
	@ZapcomApi(value="满X金额",remark="满多少钱")
	private int fullPrice=0;
	@ZapcomApi(value="邮费为",remark="满足订单价格后的运费价")
	private int cutPrice=0;
	/**
	 * @return the firstOrder
	 */
	public String getFirstOrder() {
		return firstOrder;
	}
	/**
	 * @param firstOrder the firstOrder to set
	 */
	public void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}
	/**
	 * @return the fullPrice
	 */
	public int getFullPrice() {
		return fullPrice;
	}
	/**
	 * @param fullPrice the fullPrice to set
	 */
	public void setFullPrice(int fullPrice) {
		this.fullPrice = fullPrice;
	}
	/**
	 * @return the cutPrice
	 */
	public int getCutPrice() {
		return cutPrice;
	}
	/**
	 * @param cutPrice the cutPrice to set
	 */
	public void setCutPrice(int cutPrice) {
		this.cutPrice = cutPrice;
	}
	
	
	
}
