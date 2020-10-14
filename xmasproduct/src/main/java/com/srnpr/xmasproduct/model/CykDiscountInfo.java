package com.srnpr.xmasproduct.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.baseclass.BaseClass;

/** 
* @author Angel Joy
* @Time 2020年4月13日 下午1:24:16 
* @Version 1.0
* <p>Description:</p>
*/
public class CykDiscountInfo extends BaseClass{
	
	@ZapcomApi(value="橙意卡商品编号",demo = "8016123456",remark="")
	private String cykProductCode = "";
	
	@ZapcomApi(value="橙意卡SKU编号",demo = "8016123456",remark="")
	private String cykSkuCode = "";
	
	@ZapcomApi(value="开通橙意卡后节约金钱",demo = "",remark="5.45")
	private BigDecimal cykDiscountAmount = BigDecimal.ZERO;
	
	@ZapcomApi(value="橙意卡节约金钱描述",demo = "开通橙意卡预计节约￥",remark="开通橙意卡预计节约￥")
	private String cykShowTip = bConfig("xmasproduct.plus_discount_tip");
	
	@ZapcomApi(value="橙意卡节约金钱描述",demo = "",remark="首次访问商品详情页面，此提示值不展示，仅限于橙意卡用户未登录情况下，展示tip，登陆刷页面不展示tip的友好提示")
	private String reason = "";

	@ZapcomApi(value="橙意卡提示是否展示",demo = "1",remark="1：展示，0：不展示")
	private int ifShow = 0;
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIfShow() {
		return ifShow;
	}

	public void setIfShow(int ifShow) {
		this.ifShow = ifShow;
	}

	public String getCykShowTip() {
		return cykShowTip;
	}

	public void setCykShowTip(String cykShowTip) {
		this.cykShowTip = cykShowTip;
	}

	public String getCykProductCode() {
		return cykProductCode;
	}

	public void setCykProductCode(String cykProductCode) {
		this.cykProductCode = cykProductCode;
	}

	public String getCykSkuCode() {
		return cykSkuCode;
	}

	public void setCykSkuCode(String cykSkuCode) {
		this.cykSkuCode = cykSkuCode;
	}

	public BigDecimal getCykDiscountAmount() {
		return cykDiscountAmount;
	}

	public void setCykDiscountAmount(BigDecimal cykDiscountAmount) {
		this.cykDiscountAmount = cykDiscountAmount;
	}
	
	
	
	
}
