package com.srnpr.xmassystem.modelbean;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CouponGetUserInfo implements IPlusModel{
	
	@ZapcomApi(value = "优惠券类型编号")
	private String couponTypeCode = "";

	@ZapcomApi(value = "优惠券UID")
	private String memberCode = "";
	
	@ZapcomApi(value = "开始时间")
	private String startTime = "";
	
	@ZapcomApi(value = "结束时间")
	private String endTime = "";

	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	

}
