package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelMemberLevelQuery implements IPlusQuery {
	
	@ZapcomApi(value = "用户编号", remark = "惠家有用户编号", require = 1)
	private String code = "";

	public PlusModelMemberLevelQuery() {
		super();
	}

	public PlusModelMemberLevelQuery(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
