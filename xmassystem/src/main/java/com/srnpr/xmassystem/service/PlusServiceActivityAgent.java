package com.srnpr.xmassystem.service;


import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.modelbean.ActivityAgent;
import com.srnpr.xmassystem.modelevent.PlusModelActivityAgent;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.topcache.SimpleCache;

/**
 * 分销相关
 */
public class PlusServiceActivityAgent {

	LoadActivityAgent loadActivityAgent = new LoadActivityAgent();
	
	static SimpleCache simpleCache = new SimpleCache(new SimpleCache.Config(300, 300, "activityAgent", true));
	
	/***
	 * 获取当前可用的分销活动
	 * @return
	 */
	public ActivityAgent getActivityAgent() {
		PlusModelActivityAgent plusModelActivityAgent = loadActivityAgent.upInfoByCode(new PlusModelQuery("SI2003"));
		if(!plusModelActivityAgent.getActivityList().isEmpty()) {
			String sys_time = FormatHelper.upDateTime();
			
			for(ActivityAgent item : plusModelActivityAgent.getActivityList()) {
				if(PlusSupportEvent.compareDate(sys_time,item.getEndTime())<=0 && PlusSupportEvent.compareDate(item.getBeginTime(),sys_time)<=0) {
					return item;
				}
			}
		}
		return null;
	}
}
