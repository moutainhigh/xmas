package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelCouponListInfo implements IPlusModel{
	
	@ZapcomApi(value = "可领取优惠券列表")
	List<ModelCouponForGetInfo> couponList = new ArrayList<ModelCouponForGetInfo>();

	public List<ModelCouponForGetInfo> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<ModelCouponForGetInfo> couponList) {
		this.couponList = couponList;
	}

	
}
