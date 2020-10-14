package com.srnpr.xmassystem.modelbean;

import java.util.HashMap;
import java.util.Map;

/**
 * 分销优惠券活动实体类
 */
public class ActivityAgent {

	private String activityCode = "";
	private String beginTime = "";
	private String endTime = "";

	/** 商品编号对应的优惠券类型 */
	private Map<String,String> couponTypeMap = new HashMap<String,String>();

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
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

	public Map<String, String> getCouponTypeMap() {
		return couponTypeMap;
	}

	public void setCouponTypeMap(Map<String, String> couponTypeMap) {
		this.couponTypeMap = couponTypeMap;
	}

}
