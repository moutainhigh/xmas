package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;

public class PlusConfigSkuSpread extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.SkuInfoSpread;
	}

	public Class<?> getPlusClass() {
		return PlusModelSkuInfoSpread.class;
	}

}
