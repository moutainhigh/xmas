package com.srnpr.xmassystem.support;

import java.util.List;

import com.srnpr.xmassystem.enumer.EEventRuleExtend;
import com.srnpr.xmassystem.modelevent.PlusModelEventRuleExtend;

public class PlusSupportService {

	public PlusModelEventRuleExtend upRuleByEnumer(
			List<PlusModelEventRuleExtend> lRuleExtends, EEventRuleExtend eKey) {

		PlusModelEventRuleExtend pResult = null;

		for (PlusModelEventRuleExtend pEventRuleExtend : lRuleExtends) {
			if (pEventRuleExtend.getRuleExtend().equals(eKey)) {
				pResult = pEventRuleExtend;
			}
		}

		return pResult;

	}

}
