package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelOrderAddress;

public class PlusConfigOrderAddress extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Address;
	}

	public Class<?> getPlusClass() {
		return PlusModelOrderAddress.class;
	}
	
	public int getExpireSecond()
	{
		return 600;
	}

}
