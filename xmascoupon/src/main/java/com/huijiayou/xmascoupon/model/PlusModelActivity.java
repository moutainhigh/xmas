package com.huijiayou.xmascoupon.model;

import com.srnpr.xmassystem.face.IPlusModel;

public class PlusModelActivity implements IPlusModel {
	private String activityCode;
	private String activityName;
	private String activityType;
	private String sellerCode;
	private String beginTime;
	private String endTime;
	private Double assignLine;
	private String assignTrigger;
	private String breakBlocked;
	private String blocked;
	private Integer provideNum;
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
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
	public Double getAssignLine() {
		return assignLine;
	}
	public void setAssignLine(Double assignLine) {
		this.assignLine = assignLine;
	}
	public String getAssignTrigger() {
		return assignTrigger;
	}
	public void setAssignTrigger(String assignTrigger) {
		this.assignTrigger = assignTrigger;
	}
	public String getBreakBlocked() {
		return breakBlocked;
	}
	public void setBreakBlocked(String breakBlocked) {
		this.breakBlocked = breakBlocked;
	}
	public String getBlocked() {
		return blocked;
	}
	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}
	public Integer getProvideNum() {
		return provideNum;
	}
	public void setProvideNum(Integer provideNum) {
		this.provideNum = provideNum;
	}
}
