package com.srnpr.xmassystem.modelbean;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Administrator
 *
 */
public class HjybeanProduceSetModel {
	Map<String, BigDecimal> percents = new ConcurrentHashMap<String, BigDecimal>();
	
	public BigDecimal getPercent(String sellerType) {
		return percents.get("sellerType");
	}
	
	public void putPercent(String sellerType, BigDecimal percent) {
		percents.put(sellerType, percent);
	}
}
