package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelVipDiscount extends PlusModelEventInfo {
	
	@ZapcomApi(value = "等级编码")
	private String vipLevel = "";
	@ZapcomApi(value = "等级名称")
	private String vipName = "";
	@ZapcomApi(value = "折扣", remark = "0 - 100之间的整数:  85 表示 85折")
	private int disount = 100;
	@ZapcomApi(value = "明细编号")
	private String itemCode = "";
	
	public String getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getVipName() {
		return vipName;
	}
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	public int getDisount() {
		return disount;
	}
	public void setDisount(int disount) {
		this.disount = disount;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
