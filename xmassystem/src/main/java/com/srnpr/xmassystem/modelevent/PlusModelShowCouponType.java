package com.srnpr.xmassystem.modelevent;


import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/***
 * 可在详情页展示的优惠券类型编号列表
 */
public class PlusModelShowCouponType extends IPlusAbstractModel implements IPlusModel {

	/** 当前版本号 */
	public static final int _VERSION = 0;

	@Override
	public int getCurrentVersion() {
		return _VERSION;
	}
	
	private List<String> couponTypeList = new ArrayList<String>();

	public List<String> getCouponTypeList() {
		return couponTypeList;
	}

	public void setCouponTypeList(List<String> couponTypeList) {
		this.couponTypeList = couponTypeList;
	}
	
}
