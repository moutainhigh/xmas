package com.srnpr.xmassystem.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventSellScope;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加价购活动处理类
 */
public class PlusServiceRepurchase extends PlusServiceTop implements IPlusServiceProduct {

	@Override
	public void refreshSkuInfo(PlusModelSkuInfo plusSku, PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {
		String icCode = plusSku.getItemCode();
		PlusModelEventItemQuery query = new PlusModelEventItemQuery();
		query.setCode(icCode);
		PlusModelEventItemProduct itemProduct = new LoadEventItemProduct().topInitInfo(query);
		this.checkForProduct(plusSku, plusQuery, plusEvent, itemProduct);//商品相关的通用检查和设置	
		// 商品在架有效
		if (plusSku.getBuyStatus() != 7&&plusSku.getBuyStatus() != 6) {
			plusSku.setSellPrice(itemProduct.getPriceEvent());//设置活动价
			return;
		}
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
		else if(!"1".equals(plusItemProduct.getItemFlagEnable()) ){//4497472600010018 为LD会员日，只检查非LD会员日的活动
			//如果此活动单独作废，则致为活动结束   fq++
			plusSku.setBuyStatus(3);
			plusSku.setButtonText(bInfo(964305104));
		}

		// 如果可购买 则判断活动明细的剩余库存数
		if (plusSku.getBuyStatus() == 1) {

			/**如果sku库存和活动库存的最小值**/
			long allStore = new PlusSupportStock().upSkuStockBySkuCode(plusSku.getSkuCode());
			long itemStror = new PlusSupportEvent().upEventItemSkuStock(plusSku.getItemCode());
			plusSku.setLimitStock(Math.min(allStore, itemStror));
			plusSku.setMaxBuy(Math.min(plusSku.getLimitStock(),
					plusSku.getMaxBuy()));

		/*	if (plusSku.getMaxBuy() <= 0) {
				plusSku.setBuyStatus(4);
				plusSku.setButtonText(bInfo(964305105));
			}*/

		}

		// 如果可购买 则设置倒计时为当前时间到结束时间
		if (plusSku.getBuyStatus() == 1) {
			plusSku.setLimitSecond(upDeepSecond(plusEvent.getEndTime()));
		}

		plusSku.setSellNote(plusEvent.getEventNote());
		plusSku.setDescriptionUrlHref(plusEvent.getDescriptionUrl());
		// ## 开始各种限制 ####################
/*		if (plusSku.getBuyStatus() == 1 && plusItemProduct != null) {
			// 取每个限制的最小值
			for (PlusModelEventSellScope pScope : plusItemProduct
					.getSellScopes()) {
				plusSku.setMaxBuy(Math.min(pScope.getScopeNumber(),
						plusSku.getMaxBuy()));

				plusSku.setLimitBuy(Math.min(pScope.getScopeNumber(),
						plusSku.getLimitBuy()));

			}

		}*/
	}
}
