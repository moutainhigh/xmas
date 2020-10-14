package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.xmassystem.enumer.EEventRuleExtend;
import com.srnpr.xmassystem.enumer.EEventSellScope;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 扩展规则
 * 
 * @author srnpr
 *
 */
public class PlusModelEventRuleExtend {

	@ZapcomApi(value = "规则类型")
	private EEventRuleExtend ruleExtend;

	@ZapcomApi(value = "规则的数字")
	private BigDecimal ruleNumber;

	public BigDecimal getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(BigDecimal ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public EEventRuleExtend getRuleExtend() {
		return ruleExtend;
	}

	public void setRuleExtend(EEventRuleExtend ruleExtend) {
		this.ruleExtend = ruleExtend;
	}

}
