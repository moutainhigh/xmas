package com.srnpr.xmassystem.load;


import java.math.BigDecimal;
import java.util.List;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelHuDongEvent;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 互动活动缓存
 */
public class LoadHuDongEvent extends LoadTop<PlusModelHuDongEvent, PlusModelQuery> {

	@Override
	public PlusModelHuDongEvent topInitInfo(PlusModelQuery tQuery) {
		PlusModelHuDongEvent plusModel = new PlusModelHuDongEvent();
		
		MDataMap eventInfo = DbUp.upTable("sc_hudong_event_info").one("event_code", tQuery.getCode());
		List<MDataMap> skuList = DbUp.upTable("sc_huodong_event_farm_product").queryAll("sku_code,activity_price", "", "", new MDataMap("event_code", tQuery.getCode(), "is_delete", "0"));
		
		if(eventInfo != null) {
			plusModel.setHdEventCode(eventInfo.get("event_code"));
			plusModel.setCxEventCode(eventInfo.get("cx_event_code"));
			plusModel.setEventStatus(eventInfo.get("event_status"));
			plusModel.setBeginTime(eventInfo.get("begin_time"));
			plusModel.setEndTime(eventInfo.get("end_time"));
			plusModel.setOrderLimit(eventInfo.get("order_limit"));
		}
		
		for(MDataMap m : skuList) {
			plusModel.getSkuPriceMap().put(m.get("sku_code"), new BigDecimal(m.get("activity_price")));
		}
		
		return plusModel;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.HuDongEvent;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelHuDongEvent.class;
		}
		
	};


}
