package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 在线支付减金额活动信息
 */
public class PlusModelEventOnlinePay {

	@ZapcomApi(value = "活动编号")
	private String eventCode = "";

	@ZapcomApi(value = "活动名称")
	private String eventName = "";

	@ZapcomApi(value = "活动开始时间")
	private String beginTime = "";

	@ZapcomApi(value = "活动结束时间")
	private String endTime = "";

	@ZapcomApi(value = "活动类型")
	private String eventType = "";
	
	@ZapcomApi(value = "类型中文", remark = "一般用于显示价格显示")
	private String eventNote = "";
	
	@ZapcomApi(value="立减金额",remark="每件商品立减的金额")
	private String favorablePrice="";
	
	@ZapcomApi(value="IC编号")
	private String itemCode="";

	public String getFavorablePrice() {
		return favorablePrice;
	}

	public void setFavorablePrice(String favorablePrice) {
		this.favorablePrice = favorablePrice;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventNote() {
		return eventNote;
	}

	public void setEventNote(String eventNote) {
		this.eventNote = eventNote;
	} 
	
}
