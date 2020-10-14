package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessage;

public class PlusConfigHomeScrollMessage extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ScrollMessage;
	}

	public Class<?> getPlusClass() {
		return PlusModelHomeScrollMessage.class;
	}
	
	
	public int getExpireSecond()
	{
		return 150;
	}

}
