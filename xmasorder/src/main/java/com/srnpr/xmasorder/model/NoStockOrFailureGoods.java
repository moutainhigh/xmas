package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 无库存商品
 * @remark 
 * @author 任宏斌
 * @date 2019年11月25日
 */
public class NoStockOrFailureGoods {

	@ZapcomApi(value="商品编号")
	private String product_code = "";
	
	@ZapcomApi(value="sku编号")
	private String sku_code = "";
	
	@ZapcomApi(value="sku图")
	private String sku_pic = "";
	
	@ZapcomApi(value="sku名称")
	private String sku_name = "";
	
	@ZapcomApi(value="购买数量")
	private int sku_num = 0;

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

	public String getSku_pic() {
		return sku_pic;
	}

	public void setSku_pic(String sku_pic) {
		this.sku_pic = sku_pic;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

}
