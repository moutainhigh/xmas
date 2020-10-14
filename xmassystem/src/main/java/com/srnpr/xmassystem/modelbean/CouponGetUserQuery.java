package com.srnpr.xmassystem.modelbean;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CouponGetUserQuery implements IPlusQuery{

	@ZapcomApi(value = "用户编号+类型编号", remark = "MI141224100004-CT150428100005")
	private String code = "";
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


}
