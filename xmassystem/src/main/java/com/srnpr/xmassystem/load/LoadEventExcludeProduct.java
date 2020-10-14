package com.srnpr.xmassystem.load;

import java.util.List;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventExclude;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载活动排除商品
 */
public class LoadEventExcludeProduct extends LoadTop<PlusModelEventExclude, PlusModelQuery> {

	public PlusModelEventExclude topInitInfo(PlusModelQuery query) {
		// 查询排除商品表
		List<MDataMap> mapList = DbUp.upTable("sc_event_exclude_product").queryAll("product_code", "", "", new MDataMap("event_code", query.getCode(), "flag_enable", "1"));
	
		PlusModelEventExclude model = new PlusModelEventExclude();
		for(MDataMap map : mapList) {
			model.getProductCodeList().add(map.get("product_code"));
		}
		
		return model;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.EventExclude;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelEventExclude.class;
		}
		
	};

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
