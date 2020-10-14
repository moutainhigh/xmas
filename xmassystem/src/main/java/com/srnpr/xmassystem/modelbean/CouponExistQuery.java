package com.srnpr.xmassystem.modelbean;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CouponExistQuery implements IPlusQuery{

	@ZapcomApi(value = "优惠券类型UID+优惠券类型编号", remark = "a7f6727fda7c4b63a0864983cf5e902b-CT190425100001")
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	

}
