package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;

public class PlusConfigEvent extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Event;
	}

	public Class<?> getPlusClass() {
		return PlusModelEventInfo.class;
	}

}
