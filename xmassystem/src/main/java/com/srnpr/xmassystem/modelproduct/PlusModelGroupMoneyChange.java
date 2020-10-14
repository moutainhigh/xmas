package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusModel;
/**
 * 微公社金额扣减变动
 * 
 * @author xiegj
 *
 */
public class PlusModelGroupMoneyChange implements IPlusModel {

	private String createTime = "";

	private String changeOrderCode = "";

	private BigDecimal changeMoney = BigDecimal.ZERO;
	
	private String memberCode = "";

	private String manageCode = "";
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getChangeOrderCode() {
		return changeOrderCode;
	}

	public void setChangeOrderCode(String changeOrderCode) {
		this.changeOrderCode = changeOrderCode;
	}

	public BigDecimal getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(BigDecimal changeMoney) {
		this.changeMoney = changeMoney;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	
}
