package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.srnpr.xmassystem.load.LoadEventInfoPlusList;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlusList;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.topcache.SimpleCache;

/**
 * 橙意会员卡
 */
public class PlusServiceEventPlus {

	LoadEventInfoPlusList loadEventInfoPlusList = new LoadEventInfoPlusList();
	LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	static SimpleCache simpleCache = new SimpleCache(new SimpleCache.Config(300, 300, "plusEvent", true));
	
	/***
	 * 获取当前可用的橙意卡专享活动
	 * @return
	 */
	public PlusModelEventInfoPlus getEventInfoPlus() {
		PlusModelEventInfoPlusList plusEventListModel = loadEventInfoPlusList.upInfoByCode(new PlusModelQuery("SI2003"));
		if(!plusEventListModel.getPlusEventInfoList().isEmpty()) {
			String sys_time = FormatHelper.upDateTime();
			
			for(PlusModelEventInfoPlus item : plusEventListModel.getPlusEventInfoList()) {
				if(PlusSupportEvent.compareDate(sys_time,item.getEndTime())<=0 && PlusSupportEvent.compareDate(item.getBeginTime(),sys_time)<=0) {
					return item;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取当前可用的橙意卡专享活动
	 * 优先取本地5分钟的缓存，用于列表页活动标签计算
	 * @return
	 */
	public PlusModelEventInfoPlus getEventInfoPlusUseCache() {
		PlusModelEventInfoPlus plusEvent = simpleCache.get("event");
		if(plusEvent == null) {
			plusEvent = getEventInfoPlus();
			
			if(plusEvent == null) {
				plusEvent = new PlusModelEventInfoPlus();
			}
			simpleCache.put("event", plusEvent);
		}
		
		return plusEvent.getEventCode().isEmpty() ? null : plusEvent;
	}
	
	/**
	 * 检查商品是否满足活动限制
	 * @param plus
	 * @param productCode
	 * @param eventTypeList 商品已经参与的活动类型
	 * @return
	 */
	public boolean checkEventLimit(PlusModelEventInfoPlus plus, String productCode, List<String> eventTypeList) {
		if(plus == null) return false;
		
		// 商品判断
		if(!"4497476400020001".equals(plus.getProductLimit())) {
			// 仅包含
			if("4497476400020002".equals(plus.getProductLimit()) && !plus.getProductCodes().contains(productCode)) {
				return false;
			}
			
			// 以下除外
			if("4497476400020003".equals(plus.getProductLimit()) && plus.getProductCodes().contains(productCode)) {
				return false;
			}
		}
		
		// 分类判断
		if(!"4497476400020001".equals(plus.getCategoryLimit())) {
			PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
			boolean isContain = false;
			// 仅包含
			if("4497476400020002".equals(plus.getCategoryLimit())) {
				for(String cat : plus.getCategoryCodes()) {
					for(String pcat : productInfo.getCategorys()) {
						if(pcat.contains(cat)) {
							isContain = true;
							break;
						}
					}
				}
				
				if(!isContain) return false;
			}
			
			// 以下除外
			if("4497476400020003".equals(plus.getCategoryLimit())) {
				for(String cat : plus.getCategoryCodes()) {
					for(String pcat : productInfo.getCategorys()) {
						if(pcat.contains(cat)) {
							isContain = true;
							break;
						}
					}
				}
				
				if(isContain) return false;
			}
		}
		
		// 活动叠加的判断
		if(eventTypeList != null && !eventTypeList.isEmpty()) {
			// 如果不支持叠加活动则商品不能参与其他活动
			if(plus.getIsSuprapositionFlag() == 0) {
				return false;
			}
			
			// 如果支持叠加活动则商品参与的活动类型必须都在活动选定的叠加列表中
			if(plus.getIsSuprapositionFlag() == 1) {
				List<String> supTypeList = Arrays.asList(plus.getSuprapositionType().split(","));
				for(String type : eventTypeList) {
					// 商品已参与的活动类型不在可叠加列表中则商品不能参与此活动
					if(!supTypeList.contains(type)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 计算商品开通橙意卡可节约金额
	 * @return
	 * 2020年4月13日
	 * Angel Joy
	 * BigDecimal
	 */
	public BigDecimal discountAmount(String productCode,String memberCode,List<String> events) {
		// 计算一下打折优惠的金额
		PlusSupportProduct psp = new PlusSupportProduct();
		BigDecimal skuPrice  = psp.upPriceByProductCode(productCode, memberCode);
		PlusModelEventInfoPlus eventInfoPlus =  getEventInfoPlusUseCache();
		if(eventInfoPlus == null) {
			return BigDecimal.ZERO;
		}
		boolean flag = checkEventLimit(eventInfoPlus,productCode,events);
		if(!flag) {//不能参加橙意卡活动
			return BigDecimal.ZERO;
		}
		BigDecimal plusMoney = skuPrice.multiply(new BigDecimal(1).subtract(eventInfoPlus.getPrice())).setScale(0, BigDecimal.ROUND_HALF_UP);
		// 计算出的优惠金额不能超过商品售价
		if(plusMoney.compareTo(skuPrice) > 0) {
			plusMoney = skuPrice;
		}
		return plusMoney;
	}
}
