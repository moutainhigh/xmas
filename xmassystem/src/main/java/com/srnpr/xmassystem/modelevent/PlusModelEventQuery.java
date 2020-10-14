package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelEventQuery implements IPlusQuery {

	@ZapcomApi(value = "活动编号", remark = "", require = 1)
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
