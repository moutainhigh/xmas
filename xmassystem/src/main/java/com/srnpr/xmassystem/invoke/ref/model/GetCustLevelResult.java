package com.srnpr.xmassystem.invoke.ref.model;

import com.srnpr.zapcom.topapi.RootResult;

/**
 * 用户等级
 */
public class GetCustLevelResult extends RootResult{

	private String cust_id;
	private String custlvl;
	private String plus_start_date ;
	private String plus_end_date ;
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getCustlvl() {
		return custlvl;
	}
	public void setCustlvl(String custlvl) {
		this.custlvl = custlvl;
	}
	public String getPlus_start_date() {
		return plus_start_date;
	}
	public void setPlus_start_date(String plus_start_date) {
		this.plus_start_date = plus_start_date;
	}
	public String getPlus_end_date() {
		return plus_end_date;
	}
	public void setPlus_end_date(String plus_end_date) {
		this.plus_end_date = plus_end_date;
	}
	
}
