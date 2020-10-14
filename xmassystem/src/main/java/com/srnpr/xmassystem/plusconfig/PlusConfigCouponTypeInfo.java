package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelCouponTypeInfo;

public class PlusConfigCouponTypeInfo extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.CouponTypeInfo;
	}

	public Class<?> getPlusClass() {
		return PlusModelCouponTypeInfo.class;
	}
}
