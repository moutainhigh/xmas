package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelorder.ActivityInfoDetail;

public class OrderActivity extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ActivityInfo;
	}

	public Class<?> getPlusClass() { 
		return ActivityInfoDetail.class;
	}

}
