package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventFree;
import com.srnpr.xmassystem.modelevent.PlusModelFreeQuery;
import com.srnpr.xmassystem.modelevent.PlusModelFreeShipping;
import com.srnpr.xmassystem.plusconfig.PlusConfigFree;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadEventFree extends LoadTopMain<PlusModelEventFree,PlusModelFreeQuery>{

	public PlusModelEventFree topInitInfo(PlusModelFreeQuery tQuery) {
		PlusModelEventFree plusModelEventFree = new PlusModelEventFree();
		if(tQuery==null){
			return plusModelEventFree;
		}
		
		addEventFree(plusModelEventFree,tQuery.getCode());
		return plusModelEventFree;
	}

    private final static PlusConfigFree PLUS_CONFIG = new PlusConfigFree();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	
	/**
	 * 初始化  满减运费
	 * @param plusModelEventFree
	 * @param tQuery
	 */
	public void addEventFree(PlusModelEventFree plusModelEventFree,String sellerCode){
		String sql = "SELECT info.event_code,info.event_name,info.begin_time,info.end_time,free.is_false_order,free.full_price,free.cut_price, "+
				 " info.event_type_code,info.event_status FROM sc_event_info info,"+
				 " sc_free_shipping free WHERE info.event_status = '4497472700020002' AND  info.event_type_code='4497472600010013'  "+
                " AND now()<info.end_time AND info.seller_code =:SellerCode AND info.event_code = free.event_code";
	    List<Map<String, Object>> mapList  = DbUp.upTable("sc_free_shipping").dataSqlList(sql,new MDataMap("SellerCode",sellerCode));
	
	    
	    List<PlusModelFreeShipping> eventFrees=new ArrayList<PlusModelFreeShipping>();
		if(mapList!=null && mapList.size()>0){
			for(Map<String, Object> map : mapList){
				PlusModelFreeShipping freeShipping = new PlusModelFreeShipping();
				freeShipping.setFirstOrder(map.get("is_false_order")==null?"":map.get("is_false_order").toString());
				freeShipping.setFullPrice(Integer.parseInt(map.get("full_price")==null?"":map.get("full_price").toString()));
				freeShipping.setCutPrice(Integer.parseInt(map.get("cut_price")==null?"":map.get("cut_price").toString()));
				freeShipping.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				freeShipping.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				freeShipping.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				freeShipping.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				freeShipping.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				freeShipping.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				eventFrees.add(freeShipping);
				
			}
		
	   }
	   plusModelEventFree.setEventFree(eventFrees);
		
	}


	@Override
	public PlusModelEventFree topInitInfoMain(PlusModelFreeQuery tQuery) {
		PlusModelEventFree plusModelEventFree = new PlusModelEventFree();
		
		String sellerCode = tQuery.getCode();
		String sql = "SELECT info.event_code,info.event_name,info.begin_time,info.end_time,free.is_false_order,free.full_price,free.cut_price, "+
				 " info.event_type_code,info.event_status FROM sc_event_info info,"+
				 " sc_free_shipping free WHERE info.event_status = '4497472700020002' AND  info.event_type_code='4497472600010013'  "+
               " AND now()<info.end_time AND info.seller_code =:SellerCode AND info.event_code = free.event_code";
	    List<Map<String, Object>> mapList  = DbUp.upTable("sc_free_shipping").dataSqlPriLibList(sql,new MDataMap("SellerCode", sellerCode));
	    
	    List<PlusModelFreeShipping> eventFrees=new ArrayList<PlusModelFreeShipping>();
		if(mapList!=null && mapList.size()>0){
			for(Map<String, Object> map : mapList){
				PlusModelFreeShipping freeShipping = new PlusModelFreeShipping();
				freeShipping.setFirstOrder(map.get("is_false_order")==null?"":map.get("is_false_order").toString());
				freeShipping.setFullPrice(Integer.parseInt(map.get("full_price")==null?"":map.get("full_price").toString()));
				freeShipping.setCutPrice(Integer.parseInt(map.get("cut_price")==null?"":map.get("cut_price").toString()));
				freeShipping.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				freeShipping.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				freeShipping.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				freeShipping.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				freeShipping.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				freeShipping.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				eventFrees.add(freeShipping);
			}
	   }
	   plusModelEventFree.setEventFree(eventFrees);
		
	   return plusModelEventFree;
	}
}
