package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;

public class PlusConfigSeller extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Seller;
	}

	public Class<?> getPlusClass() {
		return PlusModelSellerInfo.class;
	}

}
