package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class PlusModelHomeScrollMessage extends RootResult implements IPlusModel {

	@ZapcomApi(value = "消息列表")
	private List<PlusModelScrollMessage> messageList = new ArrayList<PlusModelScrollMessage>();
	@ZapcomApi(value = "每条展示时间", remark = "单位：秒")
	private Integer eachTime = 0;
	@ZapcomApi(value = "间隔时间", remark = "单位：秒")
	private Integer intervalTime = 0;
	
	public List<PlusModelScrollMessage> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<PlusModelScrollMessage> messageList) {
		this.messageList = messageList;
	}
	public Integer getEachTime() {
		return eachTime;
	}
	public void setEachTime(Integer eachTime) {
		this.eachTime = eachTime;
	}
	public Integer getIntervalTime() {
		return intervalTime;
	}
	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}
	
}
