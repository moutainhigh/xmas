package com.srnpr.xmassystem.modelbean;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CouponExistInfo implements IPlusModel{
	
	@ZapcomApi(value = "优惠券类型编号")
	private String couponTypeCode = "";

	@ZapcomApi(value = "优惠券UID")
	private String uid = "";

	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
