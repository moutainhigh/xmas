package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventPurchase;
import com.srnpr.xmassystem.modelevent.PlusModelPurchase;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigPurchase;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 内购缓存加载方法
 * @author zhouguohui
 *
 */
public class LoadEventPurchase  extends LoadTopMain<PlusModelEventPurchase, PlusModelSaleQuery>{

	public PlusModelEventPurchase topInitInfo(PlusModelSaleQuery tQuery) {
		PlusModelEventPurchase pmep  = new PlusModelEventPurchase();
		if(tQuery==null){
			return pmep;
		}
		addEventPurchase(pmep,tQuery.getCode());
		
		return pmep;
	}
	
	private final static PlusConfigPurchase PLUS_CONFIG = new PlusConfigPurchase();
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	
	
	/**
	 * 取内购类型设定
	 * @param plusEventSale
	 * @param sellerCode
	 */
	private void addEventPurchase(PlusModelEventPurchase pmep,String sellerCode){
		
		String sql = "SELECT sei.*,sec.item_code,sec.sku_name,sec.favorable_price FROM "+
				 " sc_event_info sei , sc_event_item_product sec WHERE sei.seller_code =:sellerCode AND sei.event_status = '4497472700020002' "+
				 " AND sei.event_type_code = '4497472600010006' AND  sei.end_time>now() AND sei.event_code = sec.event_code "+
				 " ORDER BY sei.begin_time ASC, sei.zid DESC";
		List<Map<String, Object>> listPurchaseSql = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode",sellerCode));
		 List<PlusModelPurchase> listPurchases = new ArrayList<PlusModelPurchase>();
		if(listPurchaseSql!=null && listPurchaseSql.size()>0){
			for(Map<String, Object> map : listPurchaseSql){
				PlusModelPurchase purchase = new PlusModelPurchase();
				
				purchase.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				purchase.setItemCode(map.get("item_code")==null?"":map.get("item_code").toString());
				purchase.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				purchase.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				purchase.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				purchase.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				purchase.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				purchase.setSkuName(map.get("sku_name")==null?"":map.get("sku_name").toString());
				purchase.setFavorablePrice(map.get("favorable_price")==null?"":map.get("favorable_price").toString());
				purchase.setChannels(map.get("channels")==null?"":map.get("channels").toString());
				
				// 无限制时设置渠道列表为空
				if("4497471600070001".equals(map.get("channel_limit"))) {
					purchase.setChannels("");
				}
				
				listPurchases.add(purchase);
				
			}
		}
		
		pmep.setListPurchase(listPurchases);
		
	}



	@Override
	public PlusModelEventPurchase topInitInfoMain(PlusModelSaleQuery tQuery) {
		PlusModelEventPurchase pmep  = new PlusModelEventPurchase();
		
		String sellerCode = tQuery.getCode();
		String sql = "SELECT sei.*,sec.item_code,sec.sku_name,sec.favorable_price FROM "+
				 " sc_event_info sei , sc_event_item_product sec WHERE sei.seller_code =:sellerCode AND sei.event_status = '4497472700020002' "+
				 " AND sei.event_type_code = '4497472600010006' AND  sei.end_time>now() AND sei.event_code = sec.event_code "+
				 " ORDER BY sei.begin_time ASC, sei.zid DESC";
		List<Map<String, Object>> listPurchaseSql = DbUp.upTable("sc_event_info").dataSqlPriLibList(sql, new MDataMap("sellerCode",sellerCode));
		 List<PlusModelPurchase> listPurchases = new ArrayList<PlusModelPurchase>();
		if(listPurchaseSql!=null && listPurchaseSql.size()>0){
			for(Map<String, Object> map : listPurchaseSql){
				PlusModelPurchase purchase = new PlusModelPurchase();
				
				purchase.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				purchase.setItemCode(map.get("item_code")==null?"":map.get("item_code").toString());
				purchase.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				purchase.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				purchase.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				purchase.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				purchase.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				purchase.setSkuName(map.get("sku_name")==null?"":map.get("sku_name").toString());
				purchase.setFavorablePrice(map.get("favorable_price")==null?"":map.get("favorable_price").toString());
				listPurchases.add(purchase);
			}
		}
		pmep.setListPurchase(listPurchases);
		
		return pmep;
	}
}
