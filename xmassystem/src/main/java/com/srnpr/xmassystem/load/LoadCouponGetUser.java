package com.srnpr.xmassystem.load;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelbean.CouponGetUserInfo;
import com.srnpr.xmassystem.modelbean.CouponGetUserQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigCouponGetUser;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadCouponGetUser extends LoadTopMain<CouponGetUserInfo, CouponGetUserQuery>{

	@Override
	public CouponGetUserInfo topInitInfoMain(CouponGetUserQuery tQuery) {
		String code = tQuery.getCode();
		String memberCode = "";
		String couponTypeCode = "";
		if(!StringUtils.isEmpty(code)) {
			memberCode = code.split("-")[0];
			couponTypeCode = code.split("-")[1];
		}
		MDataMap info = DbUp.upTable("oc_coupon_info").onePriLib("member_code",memberCode,"coupon_type_code",couponTypeCode);
		//Map<String, Object> info = DbUp.upTable("oc_coupon_info").dataSqlOne("select * from oc_coupon_info where member_code=:member_code and coupon_type_code=:coupon_type_code and status!=3 ", new MDataMap("member_code",memberCode,"coupon_type_code",couponTypeCode));
		CouponGetUserInfo result = new CouponGetUserInfo();
		if(info != null && !info.isEmpty()) {
			result.setCouponTypeCode(info.get("coupon_type_code").toString());
			result.setMemberCode(memberCode);
			result.setStartTime(info.get("start_time").toString());
			result.setEndTime(info.get("end_time").toString());
			return result;
		}
		return null;
	}

	@Override
	public CouponGetUserInfo topInitInfo(CouponGetUserQuery tQuery) {
		String code = tQuery.getCode();
		String memberCode = "";
		String couponTypeCode = "";
		if(!StringUtils.isEmpty(code)) {
			memberCode = code.split("-")[0];
			couponTypeCode = code.split("-")[1];
		}
		MDataMap info = DbUp.upTable("oc_coupon_info").one("member_code",memberCode,"coupon_type_code",couponTypeCode);
		//Map<String, Object> info = DbUp.upTable("oc_coupon_info").dataSqlOne("select * from oc_coupon_info where member_code=:member_code and coupon_type_code=:coupon_type_code and status!=3 ", new MDataMap("member_code",memberCode,"coupon_type_code",couponTypeCode));
		CouponGetUserInfo result = new CouponGetUserInfo();
		if(info != null && !info.isEmpty()) {
			result.setCouponTypeCode(info.get("coupon_type_code").toString());
			result.setMemberCode(memberCode);
			result.setStartTime(info.get("start_time").toString());
			result.setEndTime(info.get("end_time").toString());
			return result;
		}
		return null;
	}

	private final static PlusConfigCouponGetUser PLUS_CONFIG = new PlusConfigCouponGetUser();
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

}
