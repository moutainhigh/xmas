package com.srnpr.xmassystem.modelwebtemplete;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelWebTempleteQuery extends QueryTop{

	@ZapcomApi(value = "web专题页面编号", remark = "PNM编号", require = 1)
	private String code = "";
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

}
