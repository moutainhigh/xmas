package com.srnpr.xmassystem.modelorder;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ActivityInfoQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "活动编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public ActivityInfoQuery(String code) {
		super();
		this.code = code;
	}
	
}