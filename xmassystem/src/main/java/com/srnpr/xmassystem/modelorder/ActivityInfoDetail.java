package com.srnpr.xmassystem.modelorder;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ActivityInfoDetail implements IPlusModel {
	
	@ZapcomApi(value="活动编号")
	private String event_code = "";
	
	@ZapcomApi(value="商品编号")
	private String product_code = "";
	
	@ZapcomApi(value="SKU编号")
	private String sku_code = "";
	
	@ZapcomApi(value="SKU名称")
	private String sku_name = "";
	
	@ZapcomApi(value="优惠价")
	private String favorable_price = "";
	
	@ZapcomApi(value="销售价")
	private String selling_price = "";
	
	@ZapcomApi(value="成团人数")
	private String purchase_num = "";
	
	@ZapcomApi(value="是否可用")
	private String flag_enable = "";
	
	@ZapcomApi(value="活动结束时间")
	private String end_time = "";
	
	

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getFavorable_price() {
		return favorable_price;
	}

	public void setFavorable_price(String favorable_price) {
		this.favorable_price = favorable_price;
	}

	public String getSelling_price() {
		return selling_price;
	}

	public void setSelling_price(String selling_price) {
		this.selling_price = selling_price;
	}

	public String getPurchase_num() {
		return purchase_num;
	}

	public void setPurchase_num(String purchase_num) {
		this.purchase_num = purchase_num;
	}

	public String getFlag_enable() {
		return flag_enable;
	}

	public void setFlag_enable(String flag_enable) {
		this.flag_enable = flag_enable;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	
	
	
}
