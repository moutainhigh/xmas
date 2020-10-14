package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

/**
 * 满减活动调过滤参数实体类
 * @author zhouguohui
 *
 */
public class PlusModelSaleProObject {
	
	private String memberCode=""; 

	private List<PlusModelProObject> saleObject = new ArrayList<PlusModelProObject>();

	/**
	 * @return the saleObject
	 */
	public List<PlusModelProObject> getSaleObject() {
		return saleObject;
	}

	/**
	 * @param saleObject the saleObject to set
	 */
	public void setSaleObject(List<PlusModelProObject> saleObject) {
		this.saleObject = saleObject;
	}

	/**
	 * @return the memberCode
	 */
	public String getMemberCode() {
		return memberCode;
	}

	/**
	 * @param memberCode the memberCode to set
	 */
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	
	
	
}
