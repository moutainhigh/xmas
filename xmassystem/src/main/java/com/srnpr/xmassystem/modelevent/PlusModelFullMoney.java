package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelFullMoney {

	/*
	 * 满减类:  满 多少 元（件），减多少 元(件),共用 fullMoney、cutMoney 这两个字段
	 */
	@ZapcomApi(value="满金额",remark="限制规则必须满足这个条件，才减钱")
	private BigDecimal fullMoney=BigDecimal.ZERO;
	@ZapcomApi(value="减金额")
	private BigDecimal cutMoney=BigDecimal.ZERO;
	@ZapcomApi(value="广告语")
	private String advTitle="";
	@ZapcomApi(value="满减类型")
	private String fullCutType = "";
	@ZapcomApi(value="满减类型编号")
	private String fullCutCode = "";
	
	public BigDecimal getFullMoney() {
		return fullMoney;
	}
	public void setFullMoney(BigDecimal fullMoney) {
		this.fullMoney = fullMoney;
	}
	public BigDecimal getCutMoney() {
		return cutMoney;
	}
	public void setCutMoney(BigDecimal cutMoney) {
		this.cutMoney = cutMoney;
	}
	public String getAdvTitle() {
		return advTitle;
	}
	public void setAdvTitle(String advTitle) {
		this.advTitle = advTitle;
	}
	public String getFullCutType() {
		return fullCutType;
	}
	public void setFullCutType(String fullCutType) {
		this.fullCutType = fullCutType;
	}
	public String getFullCutCode() {
		return fullCutCode;
	}
	public void setFullCutCode(String fullCutCode) {
		this.fullCutCode = fullCutCode;
	}
	
}
