package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**   
 * 	折扣信息对象
*    xiegj
*/
public class TeslaModelDiscount  {
	@ZapcomApi(value = "折扣名称", remark = "折扣名称",require = 1, demo = "每单立减30元")
	private String dis_name = "";
	
	@ZapcomApi(value = "加减", remark = "0：减，1：加",require = 1, demo = "0")
	private String dis_type = "";
	
	@ZapcomApi(value = "折扣金额", remark = "折扣金额",require = 1, demo = "30.00")
	private Double dis_price = 0.00;

	public String getDis_type() {
		return dis_type;
	}

	public String getDis_name() {
		return dis_name;
	}

	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}

	public Double getDis_price() {
		return dis_price;
	}

	public void setDis_price(Double dis_price) {
		this.dis_price = dis_price;
	}

	public void setDis_type(String dis_type) {
		this.dis_type = dis_type;
	}

}

