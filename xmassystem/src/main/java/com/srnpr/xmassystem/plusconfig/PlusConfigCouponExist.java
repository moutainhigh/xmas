package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelbean.CouponExistInfo;

public class PlusConfigCouponExist extends ConfigTop {

	@Override
	public EKvSchema getSchema() {
		return EKvSchema.CouponExist;
	}

	@Override
	public Class<?> getPlusClass() {
		return CouponExistInfo.class;
	}

}
