package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlusList;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载橙意卡专享活动
 */
public class LoadEventInfoPlusList extends LoadTop<PlusModelEventInfoPlusList, PlusModelQuery> {

	static PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
	
	public PlusModelEventInfoPlusList topInitInfo(PlusModelQuery query) {
		String sSql = "SELECT event_code FROM sc_event_info WHERE event_status IN('4497472700020002') "
					+ " AND event_type_code = '4497472600010026' AND seller_code = 'SI2003' AND end_time > now()";
		List<Map<String, Object>> mapList =  DbUp.upTable("sc_event_info").dataSqlPriLibList(sSql, new MDataMap());
		PlusModelEventInfoPlusList plusList = new PlusModelEventInfoPlusList();
		PlusModelEventInfoPlus plus;
		for(Map<String, Object> map : mapList) {
			plus = new PlusModelEventInfoPlus();
			// 活动基本信息
			plusSupportEvent.upEventInfoFromDB(plus, map.get("event_code").toString());
			
			// 专享活动特定信息
			MDataMap mData = DbUp.upTable("sc_event_plus").onePriLib("event_code", map.get("event_code").toString());
			plus.setPrice(new BigDecimal(mData.get("price")));
			plus.setShowName(mData.get("show_name"));
			plus.setShowNotes(mData.get("show_notes"));
			plus.setProductLimit(mData.get("product_limit"));
			plus.setCategoryLimit(mData.get("category_limit"));
			
			String productCodes = mData.get("product_codes");
			String categoryCodes = mData.get("category_codes");
			
			if(StringUtils.isNotBlank(productCodes)) {
				plus.setProductCodes(Arrays.asList(productCodes.split(",")));
			}
			
			if(StringUtils.isNotBlank(categoryCodes)) {
				plus.setCategoryCodes(Arrays.asList(categoryCodes.split(",")));
			}
			
			// 折扣只能是0-1之间的数
			if(plus.getPrice().compareTo(BigDecimal.ZERO) <= 0 || plus.getPrice().compareTo(new BigDecimal(1)) >= 0) {
				continue;
			}
			
			plusList.getPlusEventInfoList().add(plus);
		}
		
		return plusList;
	}

	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.EventPlusList;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelEventInfoPlusList.class;
		}
		
	};
}
