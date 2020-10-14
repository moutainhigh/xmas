package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EEventPriceType;
import com.srnpr.xmassystem.enumer.EEventRuleExtend;
import com.srnpr.xmassystem.enumer.EEventSellScope;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventPriceStep;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventRuleExtend;
import com.srnpr.xmassystem.modelevent.PlusModelEventSellScope;
import com.srnpr.xmassystem.plusconfig.PlusConfigEventItemProduct;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.kvsupport.KvFactory;

/**
 * 加载IC系统的活动明细
 * 
 * @author srnpr
 *
 */
public class LoadEventItemProduct extends LoadTopMain<PlusModelEventItemProduct, PlusModelEventItemQuery> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.srnpr.xmassystem.top.LoadTop#topInitInfo(com.srnpr.xmassystem.face
	 * .IPlusQuery)
	 */
	public PlusModelEventItemProduct topInitInfo(PlusModelEventItemQuery tQuery) {

		PlusModelEventItemProduct result = new PlusModelEventItemProduct();
		MDataMap mItemMap = DbUp.upTable("sc_event_item_product").one(
				"item_code", tQuery.getCode());

		result.setEventCode(mItemMap.get("event_code"));

		PlusModelEventInfo plusModelEventInfo = new PlusSupportEvent()
				.upEventInfoByCode(result.getEventCode());

		result.setItemCode(mItemMap.get("item_code"));

		result.setPriceType(EEventPriceType.Base);
		result.setProductCode(mItemMap.get("product_code"));

		result.setSkuCode(mItemMap.get("sku_code"));

		result.setPriceEvent(new BigDecimal(mItemMap.get("favorable_price")));
		result.setPriceStart(new BigDecimal(mItemMap.get("favorable_price")));
		result.setSalesStock(Long.parseLong(mItemMap.get("sales_num")));
		result.setItemFlagEnable(mItemMap.get("flag_enable"));//记录该IC活动是否作废
		
		result.setProfitRate(new BigDecimal(mItemMap.get("profit_rate")));
		result.setFavorablePriceType(mItemMap.get("favorable_price_type"));

		// ##开始进入限购设置逻辑#############################################################################
		if (Long.parseLong(mItemMap.get("member_purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryMember);
			pScope.setScopeNumber(Long.parseLong(mItemMap
					.get("member_purchase_num")));
			result.getSellScopes().add(pScope);

		}

		if (Long.parseLong(mItemMap.get("persion_purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryOrder);
			pScope.setScopeNumber(Long.parseLong(mItemMap
					.get("persion_purchase_num")));
			result.getSellScopes().add(pScope);

		}

		if (Long.parseLong(mItemMap.get("purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryDay);
			pScope.setScopeNumber(Long.parseLong(mItemMap.get("purchase_num")));
			result.getSellScopes().add(pScope);

		}

		// ##开始进入阶梯价逻辑#############################################################################
		// 特价秒杀 如果是阶梯价
		if (StringUtils.contains("4497472600010001,4497472600010002",
				plusModelEventInfo.getEventType())) {
			if (mItemMap.get("price_is").equals("449746250001")) {

				// 判断阶梯价类型
				if (mItemMap.get("sort_type").equals("4497473400010001")) {
					result.setPriceType(EEventPriceType.StepTime);
				} else if (mItemMap.get("sort_type").equals("4497473400010002")) {
					result.setPriceType(EEventPriceType.StepStock);
				}

				result.setPriceSteps(new ArrayList<PlusModelEventPriceStep>());
				// 循环绑定阶梯价
				for (MDataMap map : DbUp
						.upTable("sc_event_price_step")
						.queryAll("", "after_type", "",
								new MDataMap("item_code", result.getItemCode()))) {

					PlusModelEventPriceStep plusStep = new PlusModelEventPriceStep();

					plusStep.setStepDeep(new BigDecimal(map.get("after_type")));

					// 如果是按时间 则设置差距时间 精确到秒
					if (result.getPriceType() == EEventPriceType.StepTime) {
						plusStep.setStepDeep(plusStep.getStepDeep().multiply(
								new BigDecimal("60")));

					}

					plusStep.setStepPrice(new BigDecimal(map
							.get("favorable_price")));

					result.getPriceSteps().add(plusStep);
				}

				// 根据小数从小到大排列
				Collections.sort(result.getPriceSteps(),
						new Comparator<PlusModelEventPriceStep>() {

							public int compare(PlusModelEventPriceStep o1,
									PlusModelEventPriceStep o2) {

								return o1.getStepDeep().compareTo(
										o2.getStepDeep());
							}

						});

			}
		}

		// ##开始加载拍卖活动模式设置

		if (StringUtils.contains("4497472600010003",
				plusModelEventInfo.getEventType())) {

			result.setPriceStart(new BigDecimal(mItemMap.get("starting_price")));
			result.setPriceEvent(result.getPriceStart());
			result.setPriceType(EEventPriceType.StepAuction);

			PlusModelEventPriceStep plusStep = new PlusModelEventPriceStep();
			plusStep.setStepDeep(new BigDecimal(mItemMap.get("time_interval"))
					.multiply(new BigDecimal("60")));
			// 由于拍卖价格是递减的 因此这里将价格设置为负数 拍卖那直接加就行了
			plusStep.setStepPrice(new BigDecimal(mItemMap.get("price_up_de"))
					.negate());
			result.getPriceSteps().add(plusStep);

			// 显示底价
			PlusModelEventRuleExtend ruleShowFloor = new PlusModelEventRuleExtend();
			ruleShowFloor.setRuleExtend(EEventRuleExtend.PriceShowFloor);
			ruleShowFloor.setRuleNumber(new BigDecimal(mItemMap
					.get("base_price")));

			result.getRuleExtends().add(ruleShowFloor);

			// 真实底价
			PlusModelEventRuleExtend ruleRealFloor = new PlusModelEventRuleExtend();
			ruleRealFloor.setRuleExtend(EEventRuleExtend.PriceRealFloor);
			ruleRealFloor.setRuleNumber(new BigDecimal(mItemMap
					.get("true_base_price")));

			result.getRuleExtends().add(ruleRealFloor);

		}

		// 将活动的子活动编号计入缓存系统
		XmasKv.upFactory(EKvSchema.EventChildren).hset(result.getEventCode(),
				result.getItemCode(), "");

		return result;

	}

	private final static PlusConfigEventItemProduct PLUS_CONFIG = new PlusConfigEventItemProduct();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}
	
	@Override
	public boolean deleteInfoByCode(String sCode) {
		new KvFactory("xs-ItemLog-").del(sCode);
		return super.deleteInfoByCode(sCode);
	}

	public String ipAddress() {
		try {
			Enumeration<NetworkInterface> enums = NetworkInterface.getNetworkInterfaces();
			Enumeration<InetAddress> es = null;
			InetAddress add;
			String address = null;
			while(enums.hasMoreElements()){
				es = enums.nextElement().getInetAddresses();
				if(es.hasMoreElements()){
					add = es.nextElement();
					if(add.getHostAddress().equals("127.0.0.1")){
						continue;
					}
					if(add.getHostAddress().contains(":")){
						continue;
					}
					address = add.getHostName() + " -" + add.getHostAddress();
					break;
				}
			}
			
			if(address == null) address = Inet4Address.getLocalHost().getHostName() + " -" +Inet4Address.getLocalHost().getHostAddress();
			return address;
		} catch (Exception e) {
			return "exception";
		} 
	}
	
	@Override
	public PlusModelEventItemProduct topInitInfoMain(PlusModelEventItemQuery tQuery) {
		MDataMap mItemMap = DbUp.upTable("sc_event_item_product").onePriLib("item_code", tQuery.getCode());

		PlusModelEventItemProduct result = new PlusModelEventItemProduct();
		result.setEventCode(mItemMap.get("event_code"));
		result.setItemCode(mItemMap.get("item_code"));
		result.setPriceType(EEventPriceType.Base);
		result.setProductCode(mItemMap.get("product_code"));
		result.setSkuCode(mItemMap.get("sku_code"));
		result.setPriceEvent(new BigDecimal(mItemMap.get("favorable_price")));
		result.setPriceStart(new BigDecimal(mItemMap.get("favorable_price")));
		result.setSalesStock(Long.parseLong(mItemMap.get("sales_num")));
		result.setItemFlagEnable(mItemMap.get("flag_enable"));//记录该IC活动是否作废
		
		result.setProfitRate(new BigDecimal(mItemMap.get("profit_rate")));
		result.setFavorablePriceType(mItemMap.get("favorable_price_type"));

		// ##开始进入限购设置逻辑#############################################################################
		if (Long.parseLong(mItemMap.get("member_purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryMember);
			pScope.setScopeNumber(Long.parseLong(mItemMap.get("member_purchase_num")));
			result.getSellScopes().add(pScope);
		}

		if (Long.parseLong(mItemMap.get("persion_purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryOrder);
			pScope.setScopeNumber(Long.parseLong(mItemMap.get("persion_purchase_num")));
			result.getSellScopes().add(pScope);
		}

		if (Long.parseLong(mItemMap.get("purchase_num")) > 0) {
			PlusModelEventSellScope pScope = new PlusModelEventSellScope();
			pScope.setSellScope(EEventSellScope.EveryDay);
			pScope.setScopeNumber(Long.parseLong(mItemMap.get("purchase_num")));
			result.getSellScopes().add(pScope);
		}

		// ##开始进入阶梯价逻辑#############################################################################
		// 特价秒杀 如果是阶梯价
		PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
		plusModelEventQuery.setCode(mItemMap.get("event_code"));
		PlusModelEventInfo plusModelEventInfo = new LoadEventInfo().upInfoByCodeMain(plusModelEventQuery);
		if (StringUtils.contains("4497472600010001, 4497472600010002", plusModelEventInfo.getEventType())) {
			if (mItemMap.get("price_is").equals("449746250001")) {
				// 判断阶梯价类型
				if (mItemMap.get("sort_type").equals("4497473400010001")) {
					result.setPriceType(EEventPriceType.StepTime);
				} else if (mItemMap.get("sort_type").equals("4497473400010002")) {
					result.setPriceType(EEventPriceType.StepStock);
				}
				
				result.setPriceSteps(new ArrayList<PlusModelEventPriceStep>());
				// 循环绑定阶梯价
				for (MDataMap map : DbUp.upTable("sc_event_price_step").queryPriLibAll("", "after_type", "", new MDataMap("item_code", result.getItemCode()))) {
					PlusModelEventPriceStep plusStep = new PlusModelEventPriceStep();
					plusStep.setStepDeep(new BigDecimal(map.get("after_type")));

					// 如果是按时间 则设置差距时间 精确到秒
					if (result.getPriceType() == EEventPriceType.StepTime) {
						plusStep.setStepDeep(plusStep.getStepDeep().multiply(new BigDecimal("60")));
					}

					plusStep.setStepPrice(new BigDecimal(map.get("favorable_price")));
					result.getPriceSteps().add(plusStep);
				}

				// 根据小数从小到大排列
				Collections.sort(result.getPriceSteps(),
					new Comparator<PlusModelEventPriceStep>() {
						public int compare(PlusModelEventPriceStep o1, PlusModelEventPriceStep o2) {
							return o1.getStepDeep().compareTo(o2.getStepDeep());
						}
					}
				);
			}
		}

		// ##开始加载拍卖活动模式设置

		if (StringUtils.contains("4497472600010003", plusModelEventInfo.getEventType())) {
			result.setPriceStart(new BigDecimal(mItemMap.get("starting_price")));
			result.setPriceEvent(result.getPriceStart());
			result.setPriceType(EEventPriceType.StepAuction);

			PlusModelEventPriceStep plusStep = new PlusModelEventPriceStep();
			plusStep.setStepDeep(new BigDecimal(mItemMap.get("time_interval")).multiply(new BigDecimal("60")));
			// 由于拍卖价格是递减的 因此这里将价格设置为负数 拍卖那直接加就行了
			plusStep.setStepPrice(new BigDecimal(mItemMap.get("price_up_de")).negate());
			result.getPriceSteps().add(plusStep);

			// 显示底价
			PlusModelEventRuleExtend ruleShowFloor = new PlusModelEventRuleExtend();
			ruleShowFloor.setRuleExtend(EEventRuleExtend.PriceShowFloor);
			ruleShowFloor.setRuleNumber(new BigDecimal(mItemMap.get("base_price")));
			result.getRuleExtends().add(ruleShowFloor);
			
			// 真实底价
			PlusModelEventRuleExtend ruleRealFloor = new PlusModelEventRuleExtend();
			ruleRealFloor.setRuleExtend(EEventRuleExtend.PriceRealFloor);
			ruleRealFloor.setRuleNumber(new BigDecimal(mItemMap.get("true_base_price")));
			result.getRuleExtends().add(ruleRealFloor);
		}

		// 将活动的子活动编号计入缓存系统
		XmasKv.upFactory(EKvSchema.EventChildren).hset(result.getEventCode(), result.getItemCode(), "");

		return result;
	}
}
