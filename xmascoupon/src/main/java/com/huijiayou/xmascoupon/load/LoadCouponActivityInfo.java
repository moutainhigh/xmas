package com.huijiayou.xmascoupon.load;


import com.huijiayou.xmascoupon.plusconfig.PlusConfigCouponActivity;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;

/**
 * 优惠券活动缓存
 * @see LoadCouponActivity
 */
@Deprecated
public class LoadCouponActivityInfo extends LoadTop<PlusModelCouponActivity, PlusModelQuery> {

	@Override
	public PlusModelCouponActivity topInitInfo(PlusModelQuery tQuery) {
		return new LoadCouponActivity().topInitInfo(tQuery);
	}

	private final static PlusConfigCouponActivity PLUS_CONFIG = new PlusConfigCouponActivity();
	
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

}
