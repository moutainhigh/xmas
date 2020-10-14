package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelAeraCode;

public class PlusConfigAreaCode extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.AreaCode;
	}

	public Class<?> getPlusClass() {
		return PlusModelAeraCode.class;
	}

}
