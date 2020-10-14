package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;

public class PlusConfigProductAuthorityLogo extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ProductAuthorityLogo;
	}

	public Class<?> getPlusClass() {
		return PlusModelAuthorityLogos.class;
	}
}
