package com.huijiayou.xmascoupon.model;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelActivityQuery implements IPlusQuery {

	@ZapcomApi(value = "活动编号", remark = "", require = 1)
	private String code="";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
