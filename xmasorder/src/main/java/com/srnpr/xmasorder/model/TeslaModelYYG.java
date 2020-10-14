package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class TeslaModelYYG {

	@ZapcomApi(value = "一元购订单编号",remark = "为微信商城单号")
	private String yygOrderNo = "";
	
	@ZapcomApi(value = "一元购加密认证")
	private String yygMac = "";
	
	@ZapcomApi(value = "一元购支付金额")
	private String yygPayMoney = "";

	public String getYygOrderNo() {
		return yygOrderNo;
	}

	public void setYygOrderNo(String yygOrderNo) {
		this.yygOrderNo = yygOrderNo;
	}

	public String getYygMac() {
		return yygMac;
	}

	public void setYygMac(String yygMac) {
		this.yygMac = yygMac;
	}

	public String getYygPayMoney() {
		return yygPayMoney;
	}

	public void setYygPayMoney(String yygPayMoney) {
		this.yygPayMoney = yygPayMoney;
	}
	
	
	
}
