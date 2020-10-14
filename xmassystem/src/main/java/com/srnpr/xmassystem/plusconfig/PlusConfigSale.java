package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;

public class PlusConfigSale extends ConfigTop {

	public EKvSchema getSchema() {
		
		return EKvSchema.EventSale;
	}

	public Class<?> getPlusClass() {
		// TODO Auto-generated method stub
		return PlusModelEventSale.class;
	}

}
