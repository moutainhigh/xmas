package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;
import com.srnpr.xmassystem.modelproduct.PlusProductModelQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigFlashSaleItem;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadFlashSaleProduct extends LoadTopMain<PlusModelFlashSaleProduct,PlusProductModelQuery>{

	/**
	 * 刷主库缓存暂时不用
	 */
	@Override
	public PlusModelFlashSaleProduct topInitInfoMain(PlusProductModelQuery tQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlusModelFlashSaleProduct topInitInfo(PlusProductModelQuery tQuery) {
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
		String sqlCount = "SELECT product_code,count(1) num FROM systemcenter.sc_event_item_product WHERE event_code = '"+eventCode+"' AND flag_enable = 1 group by product_code";
		List<Map<String,Object>> productMaps = DbUp.upTable("sc_event_item_product").dataSqlList(sqlCount, null);
		List<String> productCodes = new ArrayList<String>();
		for (Map<String,Object> product : productMaps) {
			String num = product.get("num")!=null?product.get("num").toString():"0";
			Integer numInt = Integer.parseInt(num);
			if(numInt > 1) {
				productCodes.add( product.get("product_code").toString());
			}
		}
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : maps) {
			String productCode = MapUtils.getString(map, "product_code");
			String sSql = "SELECT * FROM systemcenter.sc_event_item_product WHERE product_code = '"+productCode+"' AND flag_enable = 1 and  event_code = '"+eventCode+"' ORDER BY favorable_price ASC LIMIT 1 ";
			boolean flag = this.getContinue(productCodes, productCode);
			if(flag) {
				map = DbUp.upTable("sc_event_item_product").dataSqlOne(sSql, null);
				
			}
			resultMap.add(map);
		}
		result.setItems(resultMap);
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean getContinue(List<String> list,String productCode) {
		for(String s : list) {
			if(s.equals(productCode)) {
				return true;
			}
		}
		return false;
	}

	private final static PlusConfigFlashSaleItem PLUS_CONFIG = new PlusConfigFlashSaleItem();
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	
}
