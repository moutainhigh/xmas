package com.srnpr.xmasproduct.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProductActivity {

	@ZapcomApi(value="活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场)，(4497472600010008为满减), (4497472600010024为拼团), (4497472600010030为打折促销（自定义名称）)")
	private String eventType="";
	@ZapcomApi(value="活动名称", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场)，(4497472600010008为满减), (4497472600010024为拼团), (4497472600010030为打折促销（自定义名称）)")
	private String eventName="";

	@ZapcomApi(value="广告图")
	private String productEventPic="";
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getProductEventPic() {
		return productEventPic;
	}

	public void setProductEventPic(String productEventPic) {
		this.productEventPic = productEventPic;
	}

	
	
}
