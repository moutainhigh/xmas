package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 
 * @author xiegj
 *
 */
public class TeslaModelSubRole {

	@ZapcomApi(value="拆单规则",remark = "small_seller_code-是否虚拟商品-入库类型-供应商-入库地")
	private String subRole = "";
	
	@ZapcomApi(value="订单号-DD",remark = "DD123456")
	private String orderCode = "";
	
	@ZapcomApi(value="本订单下的skuCode",remark = "1,2,3")
	private List<String>  skus= new ArrayList<String>();

	@ZapcomApi(value="本单运费",remark = "2.33")
	private BigDecimal  transportMoney = BigDecimal.ZERO;
	
	private String jjgFlag = "0";
	
	
	public String getJjgFlag() {
		return jjgFlag;
	}

	public void setJjgFlag(String jjgFlag) {
		this.jjgFlag = jjgFlag;
	}

	public String getSubRole() {
		return subRole;
	}

	public void setSubRole(String subRole) {
		this.subRole = subRole;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	public BigDecimal getTransportMoney() {
		return transportMoney;
	}

	public void setTransportMoney(BigDecimal transportMoney) {
		this.transportMoney = transportMoney;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
}
