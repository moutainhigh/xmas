package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;
import com.srnpr.xmassystem.modelproduct.PlusSaleProductModelQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigPlusSaleItem;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadPlusSaleProduct extends LoadTopMain<PlusModelFlashSaleProduct,PlusSaleProductModelQuery>{

	/**
	 * 刷主库缓存暂时不用
	 */
	@Override
	public PlusModelFlashSaleProduct topInitInfoMain(PlusSaleProductModelQuery tQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlusModelFlashSaleProduct topInitInfo(PlusSaleProductModelQuery tQuery) {
		String eventCode = tQuery.getCode();
		if(StringUtils.isEmpty(eventCode)) {
			return null;
		}
		String sql = "SELECT * FROM systemcenter.sc_event_item_product WHERE event_code = '"+eventCode+"' AND flag_enable = 1 group by product_code order by seat asc";
		List<Map<String,Object>> maps = DbUp.upTable("sc_event_item_product").dataSqlList(sql, null);
		PlusModelFlashSaleProduct result = new PlusModelFlashSaleProduct();
		if(maps == null || maps.size() == 0) {
			return null;
		}
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : maps) {
			resultMap.add(map);
		}
		result.setItems(resultMap);
		return result;
	}
	

	private final static PlusConfigPlusSaleItem PLUS_CONFIG = new PlusConfigPlusSaleItem();
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	
}
