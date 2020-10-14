package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class TeslaModelJJG {

	@ZapcomApi(value = "记录加价购活动编号")
	private String eventCode = "";
	
	@ZapcomApi(value = "该加价购活动下选择的sku编号,如果多个sku则逗号拼接")
	private String skuCodes = "";

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(String skuCodes) {
		this.skuCodes = skuCodes;
	}


}
