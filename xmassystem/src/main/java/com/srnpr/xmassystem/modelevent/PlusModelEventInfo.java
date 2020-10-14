package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 活动信息
 * 
 * @author srnpr
 *
 */
public class PlusModelEventInfo extends IPlusAbstractModel implements IPlusModel {

	@Override
	public int getCurrentVersion() {
		return 1;
	}

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

	@ZapcomApi(value = "活动执行类编号")
	private String eventExecClass = "";
	@ZapcomApi(value = "活动状态")
	private String eventStatus = "";
	@ZapcomApi(value = "广告图")
	private String descriptionUrl = "";
	@ZapcomApi(value = "价格影响范围", remark = "默认0：只从特定渠道才影响价格，1：全局影响价格")
	private int scopePrice = 0;

	@ZapcomApi(value = "取消订单时效", remark = "默认为0，不自动取消  否则精确到秒")
	private long cancelTime = 0;
	
	@ZapcomApi(value = "家有活动编号", remark = "")
	private String outActiveCode = "";
	
	@ZapcomApi(value = "渠道列表，多个逗号分割", remark = "")
	private String channels = "";	
	
	@ZapcomApi(value = "是否叠加标识 0:不可叠加 1:可叠加", remark = "")
	private int isSuprapositionFlag;
	
	@ZapcomApi(value = "叠加活动类型", remark = "")
	private String suprapositionType;
	
	@ZapcomApi(value="拼团人数",remark="拼团活动拓展字段，拼团活动几人团")
	private String collagePersonCount = "0"; 
	
	@ZapcomApi(value="拼团时效",remark="拼团活动拓展字段，拼团有效时长")
	private Integer collageTimeLiness = -1; 
	
	@ZapcomApi(value="排序方式",remark="449748550001 人工排序 449748550002 智能排序")
	private String sortOrder = "449748550001"; 
	
	@ZapcomApi(value = "折扣", remark = "打折促销活动使用")
	private BigDecimal eventDiscount = BigDecimal.ZERO;
	
	@ZapcomApi(value="折扣最大金额", remark = "会员日活动、打折促销活动 使用")
	private BigDecimal maxDiscountMoney = BigDecimal.ZERO;
	
	@ZapcomApi(value="活动标签名称", remark = "打折促销活动使用")
	private String eventTipName = "";
	
	@ZapcomApi(value="活动描述")
	private String eventDescription = "";

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getCollageTimeLiness() {
		return collageTimeLiness;
	}

	public void setCollageTimeLiness(Integer collageTimeLiness) {
		this.collageTimeLiness = collageTimeLiness;
	}

	public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
	}
	
	public int getIsSuprapositionFlag() {
		return isSuprapositionFlag;
	}

	public void setIsSuprapositionFlag(int isSuprapositionFlag) {
		this.isSuprapositionFlag = isSuprapositionFlag;
	}

	public String getSuprapositionType() {
		return suprapositionType;
	}

	public void setSuprapositionType(String suprapositionType) {
		this.suprapositionType = suprapositionType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventExecClass() {
		return eventExecClass;
	}

	public void setEventExecClass(String eventExecClass) {
		this.eventExecClass = eventExecClass;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
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

	public int getScopePrice() {
		return scopePrice;
	}

	public void setScopePrice(int scopePrice) {
		this.scopePrice = scopePrice;
	}

	public String getEventNote() {
		return eventNote;
	}

	public void setEventNote(String eventNote) {
		this.eventNote = eventNote;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getDescriptionUrl() {
		return descriptionUrl;
	}

	public void setDescriptionUrl(String descriptionUrl) {
		this.descriptionUrl = descriptionUrl;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getOutActiveCode() {
		return outActiveCode;
	}

	public void setOutActiveCode(String outActiveCode) {
		this.outActiveCode = outActiveCode;
	}

	public BigDecimal getEventDiscount() {
		return eventDiscount;
	}

	public void setEventDiscount(BigDecimal eventDiscount) {
		this.eventDiscount = eventDiscount;
	}

	public BigDecimal getMaxDiscountMoney() {
		return maxDiscountMoney;
	}

	public void setMaxDiscountMoney(BigDecimal maxDiscountMoney) {
		this.maxDiscountMoney = maxDiscountMoney;
	}

	public String getEventTipName() {
		return eventTipName;
	}

	public void setEventTipName(String eventTipName) {
		this.eventTipName = eventTipName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
}
