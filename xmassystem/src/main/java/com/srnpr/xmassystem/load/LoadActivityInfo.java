package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelorder.ActivityInfoDetail;
import com.srnpr.xmassystem.modelorder.ActivityInfoQuery;
import com.srnpr.xmassystem.plusconfig.OrderActivity;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;


public class LoadActivityInfo extends LoadTop<ActivityInfoDetail,ActivityInfoQuery>{

public ActivityInfoDetail topInitInfo(ActivityInfoQuery query) {
		
		String activityCode = query.getCode();

		ActivityInfoDetail productInfo = new PlusSupportProduct().getActivityInfo(activityCode);

		return productInfo;

	}

	private final static OrderActivity PLUS_CONFIG = new OrderActivity();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}
	
	
	
}
