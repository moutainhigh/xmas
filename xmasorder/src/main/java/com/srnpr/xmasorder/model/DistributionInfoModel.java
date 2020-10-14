package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 商品所属分销人信息
 * @author zhangbo
 *
 */
public class DistributionInfoModel {
	    
	@ZapcomApi(value = "分享商品编号")
	private String pid = "";

	@ZapcomApi(value = "分享人Id")
	private String fxrMemberCode = "";

	@ZapcomApi(value = "分享开始时间")
	private  String startTime = "";

	@ZapcomApi(value = "结束时间")
	private String endTime ="";

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFxrMemberCode() {
		return fxrMemberCode;
	}

	public void setFxrMemberCode(String fxrMemberCode) {
		this.fxrMemberCode = fxrMemberCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	
	
}
