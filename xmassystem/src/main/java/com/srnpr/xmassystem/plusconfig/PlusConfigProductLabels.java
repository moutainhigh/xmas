package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;

public class PlusConfigProductLabels extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ProductLabels;
	}

	public Class<?> getPlusClass() {
		return PlusModelProductLabel.class;
	}

}
