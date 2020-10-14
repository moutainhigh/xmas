package com.srnpr.xmassystem.load;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelbean.ActivityAgent;
import com.srnpr.xmassystem.modelevent.PlusModelActivityAgent;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 分销优惠券活动缓存
 */
public class LoadActivityAgent extends LoadTop<PlusModelActivityAgent, PlusModelQuery> {

	@Override
	public PlusModelActivityAgent topInitInfo(PlusModelQuery tQuery) {
		PlusModelActivityAgent activity = new PlusModelActivityAgent();
		List<MDataMap> listMap = DbUp.upTable("oc_activity").queryAll("", "", "activity_type = '449715400008' and flag = 1 and end_time > now()", new MDataMap());
		
		ActivityAgent act;
		List<MDataMap> listDetail;
		for(MDataMap map : listMap) {
			listDetail = DbUp.upTable("oc_activity_agent_product").queryAll("", "", "", new MDataMap("activity_code", map.get("activity_code"), "flag_enable", "1"));
			if(listDetail.isEmpty()) {
				continue;
			}
			
			act = new ActivityAgent();
			act.setActivityCode(map.get("activity_code"));
			act.setBeginTime(map.get("begin_time"));
			act.setEndTime(map.get("end_time"));
			for(MDataMap detail : listDetail) {
				if(StringUtils.isNotBlank(detail.get("coupon_type_code"))) {
					act.getCouponTypeMap().put(detail.get("produt_code"), detail.get("coupon_type_code"));
				}
			}
			
			activity.getActivityList().add(act);
		}
		
		return activity;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.ActivityAgent;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelActivityAgent.class;
		}
		
		public int getExpireSecond() {
			return 3600*24;
		}
		
	};


}
