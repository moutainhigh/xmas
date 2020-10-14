package com.srnpr.xmassystem.load;

import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventOnlinePay;
import com.srnpr.xmassystem.modelevent.PlusModelEventOnlinePayResult;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusQueryOrder;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/***
 * 加载商品的信息
 * @author jlin
 *
 */
public class LoadEventOnlinePay extends LoadTop<PlusModelEventOnlinePayResult, PlusQueryOrder> {

	public PlusModelEventOnlinePayResult topInitInfo(PlusQueryOrder query) {
		PlusModelEventOnlinePayResult result = new PlusModelEventOnlinePayResult();
		
		String sql = "SELECT ei.event_code, ei.item_code, ei.favorable_price FROM sc_event_info e,sc_event_item_product ei WHERE e.event_code = ei.event_code";
		sql += " AND e.event_type_code = '4497472600010021' AND e.event_status = '4497472700020002' and e.end_time > NOW() AND e.flag_enable = 1 AND ei.flag_enable = 1";
		sql += " AND ei.product_code = ''";
		sql += " AND e.seller_code = '"+query.getCode()+"'";
		sql += " ORDER BY e.begin_time ,ei.zid";
		
		List<Map<String, Object>> dataList = DbUp.upTable("sc_event_info").dataSqlPriLibList(sql, new MDataMap());
		
		PlusModelEventOnlinePay eventOnlineFO = null;
		PlusModelEventInfo eventInfo;
		PlusModelEventQuery que = new PlusModelEventQuery();
		
		for(Map<String, Object> map : dataList){
			que.setCode(map.get("event_code")+"");
			eventInfo = new LoadEventInfo().upInfoByCode(que);
			if(eventInfo == null) continue;
			
			eventOnlineFO = new PlusModelEventOnlinePay();
			eventOnlineFO.setFavorablePrice(map.get("favorable_price")+"");
			eventOnlineFO.setItemCode(map.get("item_code")+"");
			
			eventOnlineFO.setEventCode(eventInfo.getEventCode());
			eventOnlineFO.setEventName(eventInfo.getEventName());
			eventOnlineFO.setBeginTime(eventInfo.getBeginTime());
			eventOnlineFO.setEndTime(eventInfo.getEndTime());
			eventOnlineFO.setEventType(eventInfo.getEventType());
			eventOnlineFO.setEventNote(eventInfo.getEventNote());
			
			result.getList().add(eventOnlineFO);
		}
		
		return result;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop(){

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.EventOnlinePay;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelEventOnlinePayResult.class;
		}
		
	};

	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

}
