package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;

public class PlusConfigProduct extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Product;
	}

	public Class<?> getPlusClass() {
		return PlusModelProductInfo.class;
	}

}
