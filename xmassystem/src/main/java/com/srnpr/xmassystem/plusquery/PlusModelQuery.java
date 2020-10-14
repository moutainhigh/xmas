package com.srnpr.xmassystem.plusquery;

import com.srnpr.xmassystem.face.IPlusQuery;

public class PlusModelQuery implements IPlusQuery {

	public PlusModelQuery(String code) {
		super();
		this.code = code;
	}

	private String code="";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	

}
