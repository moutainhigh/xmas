package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.modelbean.ActivityAgent;

/***
 * 发布的分销优惠券活动列表
 */
public class PlusModelActivityAgent extends IPlusAbstractModel implements IPlusModel {

	@Override
	public int getCurrentVersion() {
		return 1;
	}

	private List<ActivityAgent> activityList = new ArrayList<ActivityAgent>();

	public List<ActivityAgent> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ActivityAgent> activityList) {
		this.activityList = activityList;
	}
	
}
