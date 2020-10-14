package com.srnpr.xmasproduct.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class SkuPrice extends RootResult {

	@ZapcomApi(value="价格返回集合",remark="key为productCode  value为 价格和活动标签集合")
	private Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();

	/**
	 * @return the map
	 */
	public Map<String, BigDecimal> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, BigDecimal> map) {
		this.map = map;
	}
	
}
