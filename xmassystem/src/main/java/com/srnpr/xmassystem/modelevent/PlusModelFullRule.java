package com.srnpr.xmassystem.modelevent;

import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



public class PlusModelFullRule {

	
	@ZapcomApi(value="限制类型" ,remark="取值：不限，仅包含，以下除外")
	private String limitType="4497476400020001";
	@ZapcomApi(value="当非不限时的编号")
	private List<String> limitCode=null;
	public String getLimitType() {
		return limitType;
	}
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
	public List<String> getLimitCode() {
		return limitCode;
	}
	public void setLimitCode(List<String> limitCode) {
		this.limitCode = limitCode;
	}
	
	
	
}
