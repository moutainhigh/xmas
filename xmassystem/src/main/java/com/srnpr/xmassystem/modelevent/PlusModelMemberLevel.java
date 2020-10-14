package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusExpireTime;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelMemberLevel extends IPlusAbstractModel implements IPlusModel,IPlusExpireTime{
	
	@Override
	public int getCurrentVersion() {
		return 1;
	}

	@ZapcomApi(value = "用户编号")
	private String memberCode = "";

	@ZapcomApi(value = "手机号")
	private String phone = "";
	
	@ZapcomApi(value = "客户等级")
	private String level = "";
	
	@ZapcomApi(value = "客户id")
	private String custId = "";
	
	@ZapcomApi(value = "橙意卡过期时间")
	private String plusEndDate = "";
	
	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	private int expireSecond;
	
	@Override
	public int getExpireSecond() {
		return expireSecond;
	}

	public void setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
	}

	public String getPlusEndDate() {
		return plusEndDate;
	}

	public void setPlusEndDate(String plusEndDate) {
		this.plusEndDate = plusEndDate;
	}
	
}
