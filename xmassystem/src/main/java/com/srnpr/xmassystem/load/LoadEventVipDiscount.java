package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventVipDiscount;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelevent.PlusModelVipDiscount;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 会员日折扣缓存加载方法
 */
public class LoadEventVipDiscount extends LoadTopMain<PlusModelEventVipDiscount, PlusModelSaleQuery> {

	public PlusModelEventVipDiscount topInitInfo(PlusModelSaleQuery tQuery) {
//		PlusModelEventVipDiscount eventInfo = new PlusModelEventVipDiscount();
//
//		String sql = "SELECT sei.*, vip.vip_level, vip.item_code, vip.discount, hl.vip_name FROM `sc_event_info` sei"
//				+ " LEFT JOIN sc_event_vipdiscount vip ON sei.event_code = vip.event_code AND vip.flag_enable = 1"
//				+ " LEFT JOIN membercenter.mc_homhas_level hl ON vip.vip_level = hl.vip_level"
//				+ " WHERE sei.seller_code = :sellerCode AND sei.event_status = '4497472700020002' AND sei.event_type_code = '4497472600010018'"
//				+ " AND sei.end_time > now() ORDER BY sei.begin_time, vip.vip_level ASC";
//
//		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode", tQuery.getCode()));
//		PlusModelVipDiscount vd = null;
//		for (Map<String, Object> map : list) {
//			vd = new PlusModelVipDiscount();
//			vd.setEventCode(map.get("event_code") == null ? "" : map.get("event_code").toString());
//			vd.setItemCode(map.get("item_code") == null ? "" : map.get("item_code").toString());
//			vd.setEventName(map.get("event_name") == null ? "" : map.get("event_name").toString());
//			vd.setBeginTime(map.get("begin_time") == null ? "" : map.get("begin_time").toString());
//			vd.setEndTime(map.get("end_time") == null ? "" : map.get("end_time").toString());
//			vd.setEventType(map.get("event_type_code") == null ? "" : map.get("event_type_code").toString());
//			vd.setEventStatus(map.get("event_status") == null ? "" : map.get("event_status").toString());
//			vd.setOutActiveCode(map.get("out_active_code") == null ? "" : map.get("out_active_code").toString());
//			vd.setVipLevel(map.get("vip_level") == null ? "" : map.get("vip_level").toString());
//			vd.setVipName(map.get("vip_name") == null ? "" : map.get("vip_name").toString());
//			vd.setDisount(NumberUtils.toInt(map.get("discount") + "", 100));
//			eventInfo.getListVipDiscount().add(vd);
//		}

		return topInitInfoMain(tQuery);
	}

	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	private final static IPlusConfig PLUS_CONFIG = new ConfigTop(){

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.VipDiscount;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelEventVipDiscount.class;
		}
		
	};
	
	public static void main(String[] args) {
		PlusModelSaleQuery q = new PlusModelSaleQuery();
		q.setCode("SI2003");
		new LoadEventVipDiscount().upInfoByCode(q);
	}

	@Override
	public PlusModelEventVipDiscount topInitInfoMain(PlusModelSaleQuery tQuery) {
		PlusModelEventVipDiscount eventInfo = new PlusModelEventVipDiscount();

		String sql = "SELECT sei.*, vip.vip_level, vip.item_code, vip.discount, hl.vip_name FROM `sc_event_info` sei"
				+ " LEFT JOIN sc_event_vipdiscount vip ON sei.event_code = vip.event_code AND vip.flag_enable = 1"
				+ " LEFT JOIN membercenter.mc_homhas_level hl ON vip.vip_level = hl.vip_level"
				+ " WHERE sei.seller_code = :sellerCode AND sei.event_status = '4497472700020002' AND sei.event_type_code = '4497472600010018'"
				+ " AND sei.end_time > now() ORDER BY sei.begin_time, vip.vip_level ASC";
		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlPriLibList(sql, new MDataMap("sellerCode", tQuery.getCode()));
		PlusModelVipDiscount vd = null;
		
		Set<String> eventCodes = new HashSet<String>();
		for (Map<String, Object> map : list) {
			vd = new PlusModelVipDiscount();
			vd.setEventCode(map.get("event_code") == null ? "" : map.get("event_code").toString());
			vd.setItemCode(map.get("item_code") == null ? "" : map.get("item_code").toString());
			vd.setEventName(map.get("event_name") == null ? "" : map.get("event_name").toString());
			vd.setBeginTime(map.get("begin_time") == null ? "" : map.get("begin_time").toString());
			vd.setEndTime(map.get("end_time") == null ? "" : map.get("end_time").toString());
			vd.setEventType(map.get("event_type_code") == null ? "" : map.get("event_type_code").toString());
			vd.setEventStatus(map.get("event_status") == null ? "" : map.get("event_status").toString());
			vd.setOutActiveCode(map.get("out_active_code") == null ? "" : map.get("out_active_code").toString());
			vd.setVipLevel(map.get("vip_level") == null ? "" : map.get("vip_level").toString());
			vd.setVipName(map.get("vip_name") == null ? "" : map.get("vip_name").toString());
			vd.setDisount(NumberUtils.toInt(map.get("discount") + "", 100));
			vd.setChannels(map.get("channels") == null ? "" : map.get("channels").toString());
			vd.setMaxDiscountMoney(map.get("max_discount_money") == null ? BigDecimal.ZERO : new BigDecimal(map.get("max_discount_money").toString()));
			
			// 无限制时设置渠道列表为空
			if("4497471600070001".equals(map.get("channel_limit"))) {
				vd.setChannels("");
			}
			
			eventInfo.getListVipDiscount().add(vd);
			
			eventCodes.add(vd.getEventCode());
		}
		
		if(!eventCodes.isEmpty()){
			String v = "('"+StringUtils.join(eventCodes.toArray(), "','")+"')";
			List<Map<String, Object>> dataList = DbUp.upTable("sc_event_vipdiscount_exclude").dataSqlPriLibList("select event_code,exclude_date from sc_event_vipdiscount_exclude where flag_enable = 1 and event_code in "+v, new MDataMap());
			String eventCode,excludeDate;
			for(Map<String, Object> dayMap : dataList){
				eventCode = dayMap.get("event_code")+"";
				excludeDate = dayMap.get("exclude_date")+"";
				
				if(!eventInfo.getExcludeDayMap().containsKey(eventCode)){
					eventInfo.getExcludeDayMap().put(eventCode, new ArrayList<String>());
				}
				
				eventInfo.getExcludeDayMap().get(eventCode).add(excludeDate);
			}
		}

		return eventInfo;
	}
}
