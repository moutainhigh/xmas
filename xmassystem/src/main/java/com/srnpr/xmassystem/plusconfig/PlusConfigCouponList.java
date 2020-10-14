package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelCouponListInfo;

public class PlusConfigCouponList extends ConfigTop {
	@Override
	public EKvSchema getSchema() {
		return EKvSchema.CouponListForProduct;
	}

	@Override
	public Class<?> getPlusClass() {
		return PlusModelCouponListInfo.class;
	}

	public int getExpireSecond()
	{
		return 300;
	}

}
