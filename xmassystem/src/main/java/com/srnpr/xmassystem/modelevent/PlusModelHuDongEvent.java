package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/***
 * 发布的分销优惠券活动列表
 */
public class PlusModelHuDongEvent extends IPlusAbstractModel implements IPlusModel {

	@Override
	public int getCurrentVersion() {
		return 1;
	}
	
	// 互动活动编号
	private String hdEventCode;
	// 促销活动编号
	private String cxEventCode;
	// 互动活动状态
	private String eventStatus;
	// 开始时间
	private String beginTime;
	// 结束凥
	private String endTime;
	// 下单限制
	private String orderLimit;
	// 活动售价
	private Map<String,BigDecimal> skuPriceMap = new HashMap<String, BigDecimal>();

	public String getHdEventCode() {
		return hdEventCode;
	}

	public void setHdEventCode(String hdEventCode) {
		this.hdEventCode = hdEventCode;
	}

	public String getCxEventCode() {
		return cxEventCode;
	}

	public void setCxEventCode(String cxEventCode) {
		this.cxEventCode = cxEventCode;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOrderLimit() {
		return orderLimit;
	}

	public void setOrderLimit(String orderLimit) {
		this.orderLimit = orderLimit;
	}

	public Map<String, BigDecimal> getSkuPriceMap() {
		return skuPriceMap;
	}

	public void setSkuPriceMap(Map<String, BigDecimal> skuPriceMap) {
		this.skuPriceMap = skuPriceMap;
	}
	
}
