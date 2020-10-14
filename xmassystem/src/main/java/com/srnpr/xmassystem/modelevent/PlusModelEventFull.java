package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelEventFull extends PlusModelEventInfo {

	
	@ZapcomApi(value="编号")
	private String fullCode="";
	
	@ZapcomApi(value="满减类型",remark="怎么减")
	private String fullType="";
	

	@ZapcomApi(value="首单满减",remark="是否首单满减： 449747710001 为无限制  449747710002为首单满减")
	private String firstOrder="";
	
	@ZapcomApi(value="SKU限制规则")
	private PlusModelFullRule ruleSku=new PlusModelFullRule();
	@ZapcomApi(value="品牌限制规则")
	private PlusModelFullRule ruleBrand=new PlusModelFullRule();
	@ZapcomApi(value="分类限制规则")
	private PlusModelFullRule ruleCategory=new PlusModelFullRule();
	
	@ZapcomApi(value="满减价格计算")
	private List<PlusModelFullMoney> fullMoneys=new ArrayList<PlusModelFullMoney>();
	
	@ZapcomApi(value="活动跳转链接",remark="专题页链接")
	private String activityUrl = "";
	
	@ZapcomApi(value="最大满减金额",remark="满减类型为449747630008 时赋值")
	private BigDecimal fullCutMaxPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="满减价计算类型",remark="满减类型为449747630008 时赋值")
	private String fullCutCalType = "";
	
	@ZapcomApi(value="满减叠加类型")
	private String suprapositionType = "";

	public String getSuprapositionType() {
		return suprapositionType;
	}

	public void setSuprapositionType(String suprapositionType) {
		this.suprapositionType = suprapositionType;
	}

	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	public String getFullType() {
		return fullType;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
	}

	public PlusModelFullRule getRuleSku() {
		return ruleSku;
	}

	public void setRuleSku(PlusModelFullRule ruleSku) {
		this.ruleSku = ruleSku;
	}

	public PlusModelFullRule getRuleBrand() {
		return ruleBrand;
	}

	public void setRuleBrand(PlusModelFullRule ruleBrand) {
		this.ruleBrand = ruleBrand;
	}

	public PlusModelFullRule getRuleCategory() {
		return ruleCategory;
	}

	public void setRuleCategory(PlusModelFullRule ruleCategory) {
		this.ruleCategory = ruleCategory;
	}

	public List<PlusModelFullMoney> getFullMoneys() {
		return fullMoneys;
	}

	public void setFullMoneys(List<PlusModelFullMoney> fullMoneys) {
		this.fullMoneys = fullMoneys;
	}

	/**
	 * @return the firstOrder
	 */
	public String getFirstOrder() {
		return firstOrder;
	}

	/**
	 * @param firstOrder the firstOrder to set
	 */
	public void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public BigDecimal getFullCutMaxPrice() {
		return fullCutMaxPrice;
	}

	public void setFullCutMaxPrice(BigDecimal fullCutMaxPrice) {
		this.fullCutMaxPrice = fullCutMaxPrice;
	}

	public String getFullCutCalType() {
		return fullCutCalType;
	}

	public void setFullCutCalType(String fullCutCalType) {
		this.fullCutCalType = fullCutCalType;
	}
	
	
}
