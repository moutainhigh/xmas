package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventFree;

public class PlusConfigFree extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.FreeShipping;
	}

	public Class<?> getPlusClass() {
		return PlusModelEventFree.class;
	}

}
