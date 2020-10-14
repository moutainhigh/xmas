package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;

public class PlusConfigSku extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Sku;
	}

	public Class<?> getPlusClass() {
		return PlusModelSkuInfo.class;
	}

}
