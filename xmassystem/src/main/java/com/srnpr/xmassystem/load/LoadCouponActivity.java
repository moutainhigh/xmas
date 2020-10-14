package com.srnpr.xmassystem.load;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 优惠券活动缓存
 */
public class LoadCouponActivity extends LoadTop<PlusModelCouponActivity, PlusModelQuery> {

	@Override
	public PlusModelCouponActivity topInitInfo(PlusModelQuery tQuery) {
		String code = tQuery.getCode();
		PlusModelCouponActivity activity = new PlusModelCouponActivity();
		if(StringUtils.isNotEmpty(code)) {
			MDataMap map = DbUp.upTable("oc_activity").one("activity_code", code);
			if(map != null) {
				activity.setActivityCode(map.get("activity_code"));
				activity.setActivityName(map.get("activity_name"));
				activity.setActivityType(map.get("activity_type"));
				activity.setBeginTime(map.get("begin_time"));
				activity.setEndTime(map.get("end_time"));
				activity.setFlag(NumberUtils.toInt(map.get("flag")));
				activity.setProvideNum(NumberUtils.toInt(map.get("provide_num")));
				activity.setProvideType(map.get("provide_type"));
				activity.setOutActivityCode(map.get("out_activity_code"));
				activity.setIsMultiUse(map.get("is_multi_use"));
				activity.setIsDetailShow(map.get("is_detail_show"));
				activity.setDisup_amt(new BigDecimal(map.get("disup_amt")));
				activity.setMinlimit_tp(map.get("minlimit_tp"));
				activity.setMinlimit_amt(new BigDecimal(map.get("minlimit_amt")));
				activity.setIs_onelimit(map.get("is_onelimit"));
				activity.setMindis_amt(new BigDecimal(map.get("mindis_amt")));
			}
		}
		
		activity.setV(PlusModelCouponActivity._VERSION);
		return activity;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.CouponActivity;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelCouponActivity.class;
		}
		
		public int getExpireSecond() {
			return 60*30;
		}
		
	};


}
