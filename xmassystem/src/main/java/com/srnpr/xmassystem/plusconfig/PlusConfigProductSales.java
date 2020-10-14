package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;

public class PlusConfigProductSales extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ProductSales;
	}

	public Class<?> getPlusClass() {
		return PlusModelProductSales.class;
	}

}
