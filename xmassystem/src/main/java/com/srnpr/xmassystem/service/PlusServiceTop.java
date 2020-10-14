package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.srnpr.xmassystem.enumer.EEventSellScope;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventSellScope;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;

public class PlusServiceTop extends BaseClass {

	/**
	 * 获取距离当前时间的间隔秒数
	 * 
	 * @param sDate
	 * @return
	 */
	public long upDeepSecond(String sDate) {

		return (DateHelper.parseDate(sDate).getTime() - (new Date().getTime())) / 1000;

	}

	/**
	 * 商品相关的活动的检查
	 */
	public void checkForProduct(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent,
			PlusModelEventItemProduct plusItemProduct) {
		String sDateTime = FormatHelper.upDateTime();

		// ##开始进入判断活动状态部分#########################################################

		// 判断如果活动尚未开始
		if (plusEvent.getBeginTime().compareTo(sDateTime) > 0) {
			plusSku.setBuyStatus(2);
			// ##zgh 修改bug 8807 不在显示尚未开始，值显示当前时间
			// plusSku.setButtonText(bInfo(964305103));
			plusSku.setButtonText("");
			plusSku.setLimitSecond(upDeepSecond(plusEvent.getBeginTime()));
		}
		// 判断活动已结束
		else if (plusEvent.getEndTime().compareTo(sDateTime) < 0) {
			plusSku.setBuyStatus(3);
			plusSku.setButtonText(bInfo(964305104));
		}
		// 判断活动是否在已发布状态
		else if (!plusEvent.getEventStatus().equals("4497472700020002")) {
			plusSku.setBuyStatus(3);
			plusSku.setButtonText(bInfo(964305104));
		}
		else if(!"4497472600010018".equals(plusSku.getEventType()) &&  !"1".equals(plusItemProduct.getItemFlagEnable()) ){//4497472600010018 为LD会员日，只检查非LD会员日的活动
			//如果此活动单独作废，则致为活动结束   fq++
			plusSku.setBuyStatus(3);
			plusSku.setButtonText(bInfo(964305104));
		}
		
		// 秒杀，特价，闪购，小程序-闪购，会员专享，拼团，加价购
		String[] typs = {"4497472600010001","4497472600010002","4497472600010005","4497472600010019","4497472600010020","4497472600010024","4497472600010025"}; 
		
		// 如果活动的活动价比商品原价高则活动价作废
		// !!!!!! 如果活动商品价格计算规则变更 别忘记此处 别再次留坑了
		if(ArrayUtils.contains(typs, plusEvent.getEventType())&& plusItemProduct != null){
			boolean isTall = false; //活动价是否高于原价标识
			if("4497471600450002".equals(plusItemProduct.getFavorablePriceType())) {
				//按毛利率计算的
				isTall = plusSku.getSkuPrice().compareTo(new PlusSupportProduct().computePriceByGross(plusSku.getCostPrice(), plusItemProduct.getProfitRate().toString())) < 0;
			}else if("4497471600450001".equals(plusItemProduct.getFavorablePriceType())){
				//按活动价计算的
				isTall = plusSku.getSkuPrice().compareTo(plusItemProduct.getPriceEvent()) < 0;
			}
			if(isTall) {
				plusSku.setBuyStatus(3);
				plusSku.setButtonText(bInfo(964305104));
			}
		}

		// 如果可购买 则判断活动明细的剩余库存数
		if (plusSku.getBuyStatus() == 1) {

			/**如果为内购去商品的sku库存数量**/
			if(plusEvent.getEventType().equals("4497472600010006")){
				
				plusSku.setLimitStock(new PlusSupportStock().upSkuStockBySkuCode(plusSku.getSkuCode()));
			}else if(plusEvent.getEventType().equals("4497472600010018")) {//判断是否是LD会员日
				plusSku.setLimitStock(new PlusSupportStock().upSkuStockBySkuCode(plusSku.getSkuCode()));
			}else if(plusEvent.getEventType().equals("4497472600010030")){//判断是否是打折促销
				plusSku.setLimitStock(new PlusSupportStock().upSkuStockBySkuCode(plusSku.getSkuCode()));
			}else if(plusEvent.getEventType().equals("4497472600010004")){//扫码购
				plusSku.setLimitStock(new PlusSupportStock().upSkuStockBySkuCode(plusSku.getSkuCode()));
			}else{
				plusSku.setLimitStock(new PlusSupportEvent()
				.upEventItemSkuStock(plusSku.getItemCode()));
			}

			plusSku.setMaxBuy(Math.min(plusSku.getLimitStock(),
					plusSku.getMaxBuy()));

			if (plusSku.getMaxBuy() <= 0) {
				plusSku.setBuyStatus(4);
				plusSku.setButtonText(bInfo(964305105));
			}

		}

		// 如果可购买 则设置倒计时为当前时间到结束时间
		if (plusSku.getBuyStatus() == 1) {
			plusSku.setLimitSecond(upDeepSecond(plusEvent.getEndTime()));
		}

		plusSku.setSellNote(plusEvent.getEventNote());
		plusSku.setDescriptionUrlHref(plusEvent.getDescriptionUrl());
		// ## 开始各种限制 ####################
		if (plusSku.getBuyStatus() == 1 && plusItemProduct != null) {

			// 取每个限制的最小值
			for (PlusModelEventSellScope pScope : plusItemProduct
					.getSellScopes()) {
				plusSku.setMaxBuy(Math.min(pScope.getScopeNumber(),
						plusSku.getMaxBuy()));

				plusSku.setLimitBuy(Math.min(pScope.getScopeNumber(),
						plusSku.getLimitBuy()));

				// 如果限制是每用户
				if ((pScope.getSellScope() == EEventSellScope.EveryMember || pScope
						.getSellScope() == EEventSellScope.EveryDay)
						&& StringUtils.isNotBlank(plusQuery.getMemberCode())) {

					// 取出该用户已经购买该明细活动的数量
					String sMemberMax = XmasKv.upFactory(EKvSchema.LogItem)
							.hget(plusSku.getItemCode(),
									plusQuery.getMemberCode());

					if (StringUtils.isNotBlank(sMemberMax)) {

						plusSku.setMaxBuy(Math.min(pScope.getScopeNumber()
								- Long.parseLong(sMemberMax),
								plusSku.getMaxBuy()));

						// 如果已达到限制状态
						if ((pScope.getScopeNumber() - Long
								.parseLong(sMemberMax)) <= 0) {
							plusSku.setBuyStatus(7);
							plusSku.setButtonText(bInfo(964305107));
						}

					}

				}

			}

		}
	}

}
