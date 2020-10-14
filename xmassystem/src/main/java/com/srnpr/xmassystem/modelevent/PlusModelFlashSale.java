package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 秒杀列表页，返回规则：开始时间是今日的秒杀活动，以及两条明日的即将开始
 * @author Angel Joy
 *
 */
public class PlusModelFlashSale implements IPlusModel {
	@ZapcomApi(value="秒杀开始时间",remark="2019-05-14 13:00:00")
	private String beginTime;
	@ZapcomApi(value="秒杀活动编号",remark="CX2019000014")
	private String eventCode;
	@ZapcomApi(value="限制渠道",remark="")
	private String channels;
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getChannels() {
		return channels;
	}
	public void setChannels(String channels) {
		this.channels = channels;
	}
	

	
}
