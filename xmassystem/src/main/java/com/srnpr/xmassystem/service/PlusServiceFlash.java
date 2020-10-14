package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EEventPriceType;
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
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 限时限量
 * 
 * @author srnpr
 *
 */
public class PlusServiceFlash extends PlusServiceTop implements
		IPlusServiceProduct {

	public void refreshSkuInfo(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {

		String sIcCode = plusSku.getItemCode();

		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();

		PlusModelEventItemProduct plusItemProduct = plusSupportEvent
				.upItemProductByIcCode(sIcCode);

		// 商品相关的通用检查和设置
		checkForProduct(plusSku, plusQuery, plusEvent, plusItemProduct);
		
		// 特殊判断 如果购买状态不为立即购买 则返回空的活动编号和ic编号 按原价购买
		if (plusSku.getBuyStatus() != 1&&plusSku.getBuyStatus() != 7&&plusSku.getBuyStatus() != 6) {
			plusSku.setEventCode("");
//			plusSku.setItemCode("");
			plusSku.setBuyStatus(1);
			plusSku.setSellPrice(plusSku.getSkuPrice());
			plusSku.setSellNote("");
			plusSku.setDescriptionUrlHref("");
			plusSku.setButtonText("立即购买");
			return;
		}

		// ## 开始进入设置价格部分####################
		// 设置价格部分
		plusSku.setSellPrice(plusItemProduct.getPriceEvent());
		
		// 按毛利率计算销售价
		if("4497471600450002".equals(plusItemProduct.getFavorablePriceType())) {
			plusSku.setSellPrice(new PlusSupportProduct().computePriceByGross(plusSku.getCostPrice(), plusItemProduct.getProfitRate().toString()));
		}

		if (plusItemProduct.getPriceType() == EEventPriceType.StepTime) {

			// 获取差距的分钟数
			long lDeepTime = ((new Date().getTime()) - DateHelper.parseDate(
					plusEvent.getBeginTime()).getTime()) / (1000);

			for (PlusModelEventPriceStep pStep : plusItemProduct
					.getPriceSteps()) {
				if (pStep.getStepDeep().compareTo(new BigDecimal(lDeepTime)) <= 0) {
					plusSku.setSellPrice(pStep.getStepPrice());

				} else {
					// break;
				}

			}

		} else if (plusItemProduct.getPriceType() == EEventPriceType.StepStock) {

			// 获取已销售的数量
			long lDeep = plusItemProduct.getSalesStock()
					- plusSupportEvent.upEventItemSkuStock(sIcCode);

			for (PlusModelEventPriceStep pStep : plusItemProduct
					.getPriceSteps()) {
				if (pStep.getStepDeep().compareTo(new BigDecimal(lDeep)) <= 0) {
					plusSku.setSellPrice(pStep.getStepPrice());

				} else {
					// break;
				}

			}
		}

	}

}
