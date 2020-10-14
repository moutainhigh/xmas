package com.huijiayou.xmascoupon.support;

import org.apache.commons.lang3.StringUtils;

import com.huijiayou.xmascoupon.model.PlusModelActivity;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 优惠券活动
 * @author zht
 *
 */
public class PlusSupportCouponActivity extends BaseClass {
	
	public PlusModelActivity getActivityInfo(String activityCode) {
		if(StringUtils.isNotEmpty(activityCode)) {
			MDataMap activity = DbUp.upTable("oc_activity").one("activity_code", activityCode);
			if(null != activity && activity.size() > 0) {
				PlusModelActivity ac = new PlusModelActivity();
				
			}
		}
		return null;
	}

}
