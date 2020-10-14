package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.enumer.EEventSellScope;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 销售限制
 * 
 * @author srnpr
 *
 */
public class PlusModelEventSellScope {

	@ZapcomApi(value = "限制类型")
	private EEventSellScope sellScope;
	@ZapcomApi(value = "限制数量")
	private long scopeNumber;

	public EEventSellScope getSellScope() {
		return sellScope;
	}

	public void setSellScope(EEventSellScope sellScope) {
		this.sellScope = sellScope;
	}

	public long getScopeNumber() {
		return scopeNumber;
	}

	public void setScopeNumber(long scopeNumber) {
		this.scopeNumber = scopeNumber;
	}

}
