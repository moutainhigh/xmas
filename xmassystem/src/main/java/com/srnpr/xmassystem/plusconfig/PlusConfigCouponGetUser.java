package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelbean.CouponGetUserInfo;

public class PlusConfigCouponGetUser extends ConfigTop {

	@Override
	public EKvSchema getSchema() {
		return EKvSchema.CouponGetUser;
	}

	@Override
	public Class<?> getPlusClass() {
		return CouponGetUserInfo.class;
	}

}
