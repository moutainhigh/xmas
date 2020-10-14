package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSku;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载SKU信息
 * 
 * @author srnpr
 *
 */
public class LoadSkuInfo extends LoadTopMain<PlusModelSkuInfo, PlusModelSkuQuery> {

	public PlusModelSkuInfo topInitInfo(PlusModelSkuQuery query) {
		

		

		String sSkuCode = query.getCode();

		
		PlusModelSkuInfo plusSku = new PlusSupportProduct().initBaseInfoFromDb(sSkuCode);
		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();

		plusSupportEvent.upSkuEventFromDB(plusSku, query);

		return plusSku;

	}

	private final static PlusConfigSku PLUS_CONFIG = new PlusConfigSku();

	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	public PlusModelSkuInfo topInitInfoMain(PlusModelSkuQuery query) {
		String sSkuCode = query.getCode();
		PlusModelSkuInfo plusSku = new PlusSupportProduct().initBaseInfoFromDbMain(sSkuCode);
		
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> aItem = new ArrayList<String>();
		ArrayList<String> aType = new ArrayList<String>();
		String sql="SELECT pro.event_code,pro.item_code,inf.event_type_code FROM sc_event_item_product pro,sc_event_info inf"+
				   " WHERE pro.event_code = inf.event_code AND pro.sku_code =:skuCode"+
				   " AND (inf.event_status='4497472700020002' OR inf.event_status='4497472700020004')"+
				   " AND inf.end_time > now() AND pro.flag_enable = 1";
		for (Map<String, Object> map : DbUp.upTable("sc_event_item_product").dataSqlPriLibList(sql, new MDataMap("skuCode",sSkuCode))) {
			aList.add(map.get("event_code").toString());
			aItem.add(map.get("item_code").toString());
			aType.add(map.get("event_type_code").toString());
		}
		
		//补充商品参加促销活动 活动明细未记录sku编号的情况 暂时仅支持打折促销
		String sql2 = "SELECT pro.event_code,pro.item_code,inf.event_type_code FROM sc_event_item_product pro,sc_event_info inf " + 
				" WHERE pro.event_code = inf.event_code AND pro.product_code =:product_code " + 
				" AND inf.event_type_code='4497472600010030' " + 
				" AND (inf.event_status='4497472700020002' OR inf.event_status='4497472700020004') " + 
				" AND inf.end_time > now() AND pro.flag_enable = 1";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_event_item_product").dataSqlPriLibList(sql2, new MDataMap("product_code", plusSku.getProductCode()));
		if(null != dataSqlList && !dataSqlList.isEmpty()) {
			for (Map<String, Object> map : dataSqlList) {
				aList.add(map.get("event_code").toString());
				aItem.add(map.get("item_code").toString());
				aType.add(map.get("event_type_code").toString());
			}
		}
		
		plusSku.setEventCode(StringUtils.join(aList, ","));
		plusSku.setItemCode(StringUtils.join(aItem, ","));
		plusSku.setEventType(StringUtils.join(aType, ","));
		
		return plusSku;
	}

}
