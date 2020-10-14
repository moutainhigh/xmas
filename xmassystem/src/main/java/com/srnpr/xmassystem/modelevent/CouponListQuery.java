package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CouponListQuery implements IPlusQuery{

	@ZapcomApi(value = "商品编号", remark = "")
	private String code = "";
	
	@ZapcomApi(value = "用户编号", remark = "")
	private String memberCode = "";
	
	@ZapcomApi(value = "渠道编号", remark = "")
	private String channelId = "";
	
	@ZapcomApi(value="是否进行分销券查询标识",remark="1：是，0：否")
	private String fxFlag = "0";
	
	public String getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
