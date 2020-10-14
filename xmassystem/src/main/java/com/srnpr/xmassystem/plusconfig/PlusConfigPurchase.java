package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventPurchase;

public class PlusConfigPurchase extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.EvenetPurchase;
	}

	public Class<?> getPlusClass() {
		return PlusModelEventPurchase.class;
	}

}
