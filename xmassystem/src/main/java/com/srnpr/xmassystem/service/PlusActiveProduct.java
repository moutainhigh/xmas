package com.srnpr.xmassystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.SerializeSupport;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取闪购商品的前三个商品，用于首页数据显示
 * @author zhouguohui
 *
 */
public class PlusActiveProduct {
	
	public List<MDataMap> getActiveProductWeApp(String sellerCode,String channelId){
		List<MDataMap> listMap = new ArrayList<MDataMap>();
		
		String querySql="select * from (select p.zid, p.sku_code,p.seat,p.favorable_price,p.product_code,i.begin_time,i.end_time,sk.sell_price,i.channels from productcenter.pc_productinfo pi,productcenter.pc_skuinfo sk, sc_event_item_product p join "+ 
                " (select event_code,begin_time,end_time,channels from sc_event_info where (event_status='4497472700020002' or event_status='4497472700020004') and event_type_code=4497472600010019 "
                + "and seller_code=:sellerCode and begin_time<=:startTime and end_time>=:startTime AND (channels = '' OR channels like '%"+channelId+"%') limit 0,1) i on p.event_code=i.event_code "
                + "where p.flag_enable=1 and pi.product_status = '4497153900060002' and pi.product_code = p.product_code AND p.sku_code = sk.sku_code order by p.product_code,p.favorable_price,sk.zid)t "
                + "group by t.product_code order by t.seat asc,t.zid desc limit 0,10";
		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(querySql, new MDataMap("sellerCode",sellerCode,"startTime",DateUtil.getSysDateTimeString()));
		
		if(list!=null){
			for(Map<String, Object> map:list){
				String channels = (String)map.get("channels");
				if(StringUtils.isBlank(channels) || channels.contains(channelId)) {
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("sku_code", map.get("sku_code")==null?"":map.get("sku_code").toString());
					mDataMap.put("seat", map.get("seat")==null?"":map.get("seat").toString());
					mDataMap.put("begin_time", map.get("begin_time")==null?"":map.get("begin_time").toString());
					mDataMap.put("end_time", map.get("end_time")==null?"":map.get("end_time").toString());
					mDataMap.put("favorable_price", map.get("favorable_price")==null?"":map.get("favorable_price").toString());
					mDataMap.put("sell_price", map.get("sell_price")==null?"":map.get("sell_price").toString());
					listMap.add(mDataMap);
				}
			}
		}
		
		return  listMap;
	}
	
	public List<MDataMap> getActiveProduct(String sellerCode,String channelId){
		List<MDataMap> listMap = new ArrayList<MDataMap>();
		
		String querySql="select p.sku_code,p.seat,p.favorable_price,i.begin_time,i.end_time,i.channels from sc_event_item_product p join "+ 
		                " (select event_code,begin_time,end_time,channels from sc_event_info where (event_status='4497472700020002' or event_status='4497472700020004') and event_type_code='4497472600010005' and seller_code=:sellerCode" +
		                " and begin_time<=:startTime and end_time>=:startTime AND (channels = '' OR channels like '%"+channelId+"%') limit 0,1) i on p.event_code=i.event_code " +
		                " JOIN productcenter.pc_productinfo t ON t.product_code = p.product_code " +
                        " where p.flag_enable=1 AND t.product_status = '4497153900060002' group by p.product_code order by p.seat asc,p.zid desc limit 0,10";
		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(querySql, new MDataMap("sellerCode",sellerCode,"startTime",DateUtil.getSysDateTimeString()));
		
		if(list!=null){
			for(Map<String, Object> map:list){
				String channels = (String)map.get("channels");
				if(StringUtils.isBlank(channels) || channels.contains(channelId)) {
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("sku_code", map.get("sku_code")==null?"":map.get("sku_code").toString());
					mDataMap.put("seat", map.get("seat")==null?"":map.get("seat").toString());
					mDataMap.put("begin_time", map.get("begin_time")==null?"":map.get("begin_time").toString());
					mDataMap.put("end_time", map.get("end_time")==null?"":map.get("end_time").toString());
					mDataMap.put("favorable_price", map.get("favorable_price")==null?"":map.get("favorable_price").toString());
					listMap.add(mDataMap);
				}
			}
		}
		
		return  listMap;
	}
	
	public List<MDataMap> getActiveProductPc(String sellerCode){
		List<MDataMap> listMap = new ArrayList<MDataMap>();
		
		String querySql="select p.sku_code,p.seat,p.favorable_price,i.begin_time,i.end_time from sc_event_item_product p join "+ 
		                " (select event_code,begin_time,end_time from sc_event_info where (event_status='4497472700020002' or event_status='4497472700020004') and event_type_code='4497472600010005' and seller_code=:sellerCode" +
		                " and begin_time<=:startTime and end_time>=:startTime limit 0,1) i on p.event_code=i.event_code " +
		                " JOIN productcenter.pc_productinfo t ON t.product_code = p.product_code " +
		                " where p.flag_enable=1 AND t.product_status = '4497153900060002' group by p.product_code order by p.seat asc,p.zid desc";
		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(querySql, new MDataMap("sellerCode",sellerCode,"startTime",DateUtil.getSysDateTimeString()));
		
		if(list!=null){
			for(Map<String, Object> map:list){
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("sku_code", map.get("sku_code")==null?"":map.get("sku_code").toString());
				mDataMap.put("seat", map.get("seat")==null?"":map.get("seat").toString());
				mDataMap.put("begin_time", map.get("begin_time")==null?"":map.get("begin_time").toString());
				mDataMap.put("end_time", map.get("end_time")==null?"":map.get("end_time").toString());
				mDataMap.put("favorable_price", map.get("favorable_price")==null?"":map.get("favorable_price").toString());
				listMap.add(mDataMap);
			}
		}
		
		return  listMap;
	}
}
