package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelCouponTypeInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelCouponTypeInfoQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigCouponTypeInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/**
 * 加载优惠券活动过期后 用户的券未过期且未使用的券类型
 * @remark 
 * @author 任宏斌
 * @date 2019年4月17日
 */
public class LoadCouponTypeInfo extends LoadTop<PlusModelCouponTypeInfo, PlusModelCouponTypeInfoQuery> {

	public PlusModelCouponTypeInfo topInitInfo(PlusModelCouponTypeInfoQuery query) {
		
		String couponTypeCode = query.getCode();

		PlusModelCouponTypeInfo productInfo = new PlusSupportProduct().initCouponTypeInfoFromDb(couponTypeCode);

		return productInfo;

	}

	private final static PlusConfigCouponTypeInfo PLUS_CONFIG = new PlusConfigCouponTypeInfo();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
