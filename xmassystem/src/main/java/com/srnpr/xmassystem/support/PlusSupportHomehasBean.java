package com.srnpr.xmassystem.support;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelbean.HjybeanConsumeSetModel;
import com.srnpr.xmassystem.modelbean.HjybeanProduceSetModel;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 惠豆配置缓存相关方法.包括消费配置与返豆配置
 * @author zht
 *
 */
public class PlusSupportHomehasBean {
	public static final String SUFFIX_CONSUME = "Consume";
	public static final String SUFFIX_PRODUCE = "Produce";
	
	/**
	 * 
	 * @return
	 */
	public HjybeanConsumeSetModel getHomehasBeanConsumeConfig() {
		HjybeanConsumeSetModel model = new HjybeanConsumeSetModel();
		Map<String, String> consumeConfig = null;
		if (!XmasKv.upFactory(EKvSchema.HomehasBeanConfig).exists(SUFFIX_CONSUME)) {
			consumeConfig = queryBeanConsumeConfig();
			if(consumeConfig != null)
				XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hmset(SUFFIX_CONSUME, consumeConfig);
		} else {
			consumeConfig = XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hgetAll(SUFFIX_CONSUME);
		}
		
		if(consumeConfig != null)
			populateConsumeConfig(model, consumeConfig);
		return model;
	}
	
	/**
	 * 查询数据库中惠豆消费设置
	 */
	private Map<String, String> queryBeanConsumeConfig() {
		Map<String, String> consumeConfig = null;
		List<MDataMap> list = DbUp.upTable("fh_hd_consume_config").queryAll("seller_type,min_use,max_percent", "zid desc", "", null);
		if(null != list && list.size() > 0) {
			consumeConfig = list.get(0);
		}
		return consumeConfig;
	}
	
	public HjybeanProduceSetModel getHomehasBeanProduceConfig() {
		Map<String, String> produceConfig = null;
		HjybeanProduceSetModel model = new HjybeanProduceSetModel();
		if (!XmasKv.upFactory(EKvSchema.HomehasBeanConfig).exists(SUFFIX_PRODUCE)) {
			produceConfig = queryBeanProduceConfig();
			if(produceConfig != null)
				XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hmset(SUFFIX_PRODUCE, produceConfig);
		} else {
			produceConfig = XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hgetAll(SUFFIX_PRODUCE);
		}
		
		if(produceConfig != null)
			populateProduceConfig(model, produceConfig);
		return model;
	}
	
	/**
	 * 查询数据库中惠豆消费设置
	 */
	private Map<String, String> queryBeanProduceConfig() {
		Map<String, String> consumeConfig = new ConcurrentHashMap<String, String>();
		List<MDataMap> list = DbUp.upTable("fh_hd_produce_config").queryAll("seller_type,percent", "", "", null);
		if(null != list && list.size() > 0) {
			for(MDataMap map : list) {
				if(map.get("seller_type") != null && StringUtils.isNotEmpty(map.get("seller_type").toString())) {
					consumeConfig.put(map.get("seller_type"), map.get("percent"));
				}
			}
			
		}
		return consumeConfig;
	}
	
	private void populateConsumeConfig(HjybeanConsumeSetModel model, Map<String, String> consumeConfig) {
		String seller_type = consumeConfig.get("seller_type");
		if(StringUtils.isNotEmpty(seller_type)) {
			String[] sellerTypes = seller_type.split(",");
			for(String sellerType : sellerTypes) {
				model.getSellerType().add(sellerType);
			}
		}
		
		String minUse = consumeConfig.get("min_use");
		if(StringUtils.isNotEmpty(minUse)) {
			try {
				model.setMinUse(new BigDecimal(minUse));
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		String maxPercent = consumeConfig.get("max_percent");
		if(StringUtils.isNotEmpty(maxPercent)) {
			try {
				model.setMaxPercent(new BigDecimal(maxPercent));
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		String ratio = consumeConfig.get("ratio");
		if(StringUtils.isNotEmpty(ratio)) {
			try {
				model.setRatio(new BigDecimal(ratio));
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void populateProduceConfig(HjybeanProduceSetModel model, Map<String, String> produceConfig) {
		Set<Entry<String, String>> entrys = produceConfig.entrySet();
		for(Entry<String, String> entry: entrys) {
			BigDecimal value = BigDecimal.ZERO;
			if(StringUtils.isNotEmpty(entry.getValue())) {
				try {
					value = new BigDecimal(entry.getValue());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			model.putPercent(entry.getKey(), value);
		}
	}
	
	
	/**
	 * 更新惠豆消费配置
	 * @param map
	 * @return
	 */
	public boolean updateHomehasBeanConsumeConfig(MDataMap consumeConfig) {
		if(!consumeConfig.isEmpty()) {
			XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hmset(SUFFIX_CONSUME, consumeConfig);
		}
		return true;
	}
	
	/**
	 * 更新惠豆赠送配置
	 * @param map
	 * @return
	 */
	public boolean updateHomehasBeanProduceConfig(MDataMap produceConfig) {
		if(!produceConfig.isEmpty()) {
			XmasKv.upFactory(EKvSchema.HomehasBeanConfig).hmset(SUFFIX_PRODUCE, produceConfig);
		}
		return true;
	}
}
