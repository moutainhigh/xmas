package com.huijiayou.xmascoupon.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.plusconfig.ConfigTop;

/**
 * 优惠券活动配置
 */
public class PlusConfigCouponActivity extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.CouponActivity;
	}

	public Class<?> getPlusClass() {
		return PlusModelCouponActivity.class;
	}

}
