package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelFreight;

public class PlusConfigFreight extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Freight;
	}

	public Class<?> getPlusClass() {
		return PlusModelFreight.class;
	}
	
	
	public int getExpireSecond()
	{
		return 3600;
	}

}
