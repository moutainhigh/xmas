package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 阶梯价
 * 
 * @author srnpr
 *
 */
public class PlusModelEventPriceStep {

	@ZapcomApi(value = "间隔秒数", remark = "精确到秒数")
	private BigDecimal stepDeep = BigDecimal.ZERO;
	@ZapcomApi(value = "间隔价格")
	private BigDecimal stepPrice = BigDecimal.ZERO;

	public BigDecimal getStepDeep() {
		return stepDeep;
	}

	public void setStepDeep(BigDecimal stepDeep) {
		this.stepDeep = stepDeep;
	}

	public BigDecimal getStepPrice() {
		return stepPrice;
	}

	public void setStepPrice(BigDecimal stepPrice) {
		this.stepPrice = stepPrice;
	}

}
