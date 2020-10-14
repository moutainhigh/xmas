package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.util.List;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceFlow;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceChange;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceChangeQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 查询SKU下面的调价记录
 */
public class LoadSkuPriceChange extends LoadTop<PlusModelSkuPriceChange,PlusModelSkuPriceChangeQuery>{

	public PlusModelSkuPriceChange topInitInfo(PlusModelSkuPriceChangeQuery tQuery) {
		String skuCode = tQuery.getCode();
		// 查询所有未结束的调价记录，根据开始时间倒序排列
		List<MDataMap> mapList = DbUp.upTable("pc_skuprice_change_flow").queryAll("", "start_time desc,zid desc", "status = '4497172300130002' AND is_delete != 3 AND do_type = 2 AND end_time >= DATE(NOW()) AND sku_code = :sku_code", new MDataMap("sku_code", skuCode));
		
		PlusModelSkuPriceChange model = new PlusModelSkuPriceChange();
		PlusModelSkuPriceFlow item;
		
		for(MDataMap map : mapList) {
			item = new PlusModelSkuPriceFlow();
			item.setFlowCode(map.get("flow_code"));
			item.setSellPriceOld(new BigDecimal(map.get("sell_price_old")));
			item.setSellPrice(new BigDecimal(map.get("sell_price")));
			item.setCostPriceOld(new BigDecimal(map.get("cost_price_old")));
			item.setCostPrice(new BigDecimal(map.get("cost_price")));
			item.setStartTime(map.get("start_time"));
			item.setEndTime(map.get("end_time"));
			model.getItemList().add(item);
		}
		
		return model;
	}
	
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

    private final static ConfigTop PLUS_CONFIG = new ConfigTop(){

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.SkuPriceChange;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelSkuPriceChange.class;
		}

		@Override
		public int getExpireSecond() {
			return 24*3600;
		}
    	
    };
	
}
