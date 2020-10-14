package com.srnpr.xmassystem.modelevent;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 
 *  用于满减和组合购 详情页展示数据
 * @author zhouguohui
 *
 */
public class PlusModelFullCutMessage {
	

	@ZapcomApi(value="活动编号")
	private String eventCode="";
	@ZapcomApi(value="满减类型",remark="(4497472600010008为满减)")
	private String eventType="";
	@ZapcomApi(value="满减名称")
	private String eventName="";
	@ZapcomApi(value="促销广告语")
	private String saleMessage="";
	@ZapcomApi(value = "活动开始时间")
	private String beginTime = "";
	@ZapcomApi(value = "活动结束时间")
	private String endTime = "";
	
	@ZapcomApi(value="活动跳转类型",remark="100001:满减活动原生页；100002：满减活动专题页")
	private String forwardType = "";
	
	@ZapcomApi(value="活动跳转链接")
	private String forwardVal = "" ;
	
	@ZapcomApi(value="满减名称标签的背景色",remark="默认值为黄色")
	private String eventNameColor = "#fa6565";
	
	/**
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}
	/**
	 * @param eventCode the eventCode to set
	 */
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}
	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	/**
	 * @return the saleMessage
	 */
	public String getSaleMessage() {
		return saleMessage;
	}
	/**
	 * @param saleMessage the saleMessage to set
	 */
	public void setSaleMessage(String saleMessage) {
		this.saleMessage = saleMessage;
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
	public String getForwardType() {
		return forwardType;
	}
	public void setForwardType(String forwardType) {
		this.forwardType = forwardType;
	}
	public String getForwardVal() {
		return forwardVal;
	}
	public void setForwardVal(String forwardVal) {
		this.forwardVal = forwardVal;
	}
	public String getEventNameColor() {
		return eventNameColor;
	}
	public void setEventNameColor(String eventNameColor) {
		this.eventNameColor = eventNameColor;
	}
	
	
}
