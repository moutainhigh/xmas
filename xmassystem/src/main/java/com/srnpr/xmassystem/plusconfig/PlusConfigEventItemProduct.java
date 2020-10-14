package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;

public class PlusConfigEventItemProduct extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Item;
	}

	public Class<?> getPlusClass() {

		return PlusModelEventItemProduct.class;
	}

}
