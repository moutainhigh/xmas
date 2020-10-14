package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;

public class PlusConfigSkuItem extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.IcSku;
	}

	public Class<?> getPlusClass() {
		return PlusModelSkuInfo.class;
	}

}
