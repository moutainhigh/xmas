package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EEventPriceType;
import com.srnpr.xmassystem.enumer.EEventRuleExtend;
import com.srnpr.xmassystem.enumer.EEventSellScope;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventPriceStep;
import com.srnpr.xmassystem.modelevent.PlusModelEventSellScope;
import com.srnpr.xmassystem.modelorder.PlusModelOrder;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportService;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 拍卖
 * 
 * @author srnpr
 *
 */
public class PlusServiceAuction extends PlusServiceTop implements
		IPlusServiceProduct {

	public void refreshSkuInfo(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {

		String sIcCode = plusSku.getItemCode();

		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();

		PlusModelEventItemProduct plusItemProduct = plusSupportEvent
				.upItemProductByIcCode(sIcCode);

		// 商品相关的通用检查和设置
		checkForProduct(plusSku, plusQuery, plusEvent, plusItemProduct);

		// ## 开始进入设置价格部分####################
		// 设置价格部分
		plusSku.setSellPrice(plusItemProduct.getPriceEvent());

		if (plusItemProduct.getPriceType() == EEventPriceType.StepAuction) {
			plusSku.setSellPrice(plusItemProduct.getPriceEvent());

			// 获取差距的秒数
			long lDeepTime = ((new Date().getTime()) - DateHelper.parseDate(
					plusEvent.getBeginTime()).getTime()) / (1000);

			for (PlusModelEventPriceStep pStep : plusItemProduct
					.getPriceSteps()) {

				BigDecimal bStep = new BigDecimal(String.valueOf(Math
						.ceil(lDeepTime / pStep.getStepDeep().intValue())))
						.multiply(pStep.getStepPrice());

				// 显示价格等于起始价加上-时间间隔*递减价格
				BigDecimal bShowPrice = plusItemProduct.getPriceEvent().add(
						bStep);

				BigDecimal bFloorPrice = new PlusSupportService()
						.upRuleByEnumer(plusItemProduct.getRuleExtends(),
								EEventRuleExtend.PriceShowFloor)
						.getRuleNumber();

				// 判断是否小于底价 如果小于显示底价则设置为显示底价
				if (bShowPrice.compareTo(bFloorPrice) > 0) {
					plusSku.setSellPrice(bShowPrice);
				} else {
					plusSku.setSellPrice(bFloorPrice);
				}

				/*
				 * if (pStep.getStepDeep().compareTo(new BigDecimal(lDeepTime))
				 * <= 0) { plusSku.setSellPrice(pStep.getStepPrice());
				 */

			}

			BigDecimal bRealPrice = new PlusSupportService().upRuleByEnumer(
					plusItemProduct.getRuleExtends(),
					EEventRuleExtend.PriceRealFloor).getRuleNumber();

			// 如果价格小于真实底价 则将可购数量设置为0 将这个活动对应的库存数量清为-1
			if (plusSku.getSellPrice().compareTo(bRealPrice) <= 0) {
				plusSku.setMaxBuy(0);
				// 如果
				XmasKv.upFactory(EKvSchema.Stock).set(sIcCode, "-1");

			}

		}

	}

}
