package com.srnpr.xmassystem.load;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelbean.CouponExistInfo;
import com.srnpr.xmassystem.modelbean.CouponExistQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigCouponExist;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadCouponExist extends LoadTopMain<CouponExistInfo, CouponExistQuery>{

	@Override
	public CouponExistInfo topInitInfoMain(CouponExistQuery tQuery) {
		String code = tQuery.getCode();
		String uid = "";
		String couponTypeCode = "";
		if(!StringUtils.isEmpty(code)) {
			uid = code.split("-")[0];
			couponTypeCode = code.split("-")[1];
		}
		MDataMap mDataMap = DbUp.upTable("oc_coupon_type").onePriLib("uid",
				uid,"coupon_type_code",couponTypeCode);

		if(mDataMap !=null &&!mDataMap.isEmpty()) {
			CouponExistInfo couponExistInfo = new CouponExistInfo();
			couponExistInfo.setCouponTypeCode(mDataMap.get("coupon_type_code"));
			couponExistInfo.setUid(mDataMap.get("uid"));
			return couponExistInfo;
		}
		return null;
	}

	@Override
	public CouponExistInfo topInitInfo(CouponExistQuery tQuery) {
		String code = tQuery.getCode();
		String uid = "";
		String couponTypeCode = "";
		if(!StringUtils.isEmpty(code)) {
			uid = code.split("-")[0];
			couponTypeCode = code.split("-")[1];
		}
		MDataMap mDataMap = DbUp.upTable("oc_coupon_type").one("uid",
				uid,"coupon_type_code",couponTypeCode);
		if(mDataMap !=null &&!mDataMap.isEmpty()) {
			CouponExistInfo couponExistInfo = new CouponExistInfo();
			couponExistInfo.setCouponTypeCode(mDataMap.get("coupon_type_code"));
			couponExistInfo.setUid(mDataMap.get("uid"));
			return couponExistInfo;
		}
		return null;
	}

	private final static PlusConfigCouponExist PLUS_CONFIG = new PlusConfigCouponExist();
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

}
