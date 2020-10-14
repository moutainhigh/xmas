package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelFreeQuery implements IPlusQuery {
	
	@ZapcomApi(value = "系统编号", remark = "SI2003之类的", require = 1)
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
