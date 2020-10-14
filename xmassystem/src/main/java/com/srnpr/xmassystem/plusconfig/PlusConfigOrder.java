package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelorder.PlusModelOrder;

public class PlusConfigOrder extends ConfigTop {

	public EKvSchema getSchema() {

		return EKvSchema.Order;
	}

	public Class<?> getPlusClass() {

		return PlusModelOrder.class;
	}

}
