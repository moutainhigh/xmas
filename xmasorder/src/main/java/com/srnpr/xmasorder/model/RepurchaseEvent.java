package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 换购活动
*/
public class RepurchaseEvent  {
	
	@ZapcomApi(value = "活动编号")
	private String event_code = "";
	
	@ZapcomApi(value = "活动名称")
	private String event_name = "";
	
	@ZapcomApi(value = "是否满足换购标识 ",remark = "1满足 0不满足")
	private String flag = "0";
	
	@ZapcomApi(value = "不满足的条件下所差金额数")
	private double value =0;
		
	@ZapcomApi(value = "满足条件的情况下可以选择的换购数量")
	private int repurchase_num = 0;
	
	@ZapcomApi(value = "加价购活动的换购下限金额")
	private BigDecimal limit_money  = new BigDecimal(0);
	
	
	public BigDecimal getLimit_money() {
		return limit_money;
	}

	public void setLimit_money(BigDecimal limit_money) {
		this.limit_money = limit_money;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getRepurchase_num() {
		return repurchase_num;
	}

	public void setRepurchase_num(int repurchase_num) {
		this.repurchase_num = repurchase_num;
	}
	
	
	
}

