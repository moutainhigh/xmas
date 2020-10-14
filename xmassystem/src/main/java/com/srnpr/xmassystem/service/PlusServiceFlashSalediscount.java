package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;

/**
 * 打折促销刷新类
 * @remark 
 * @author 任宏斌
 * @date 2020年4月17日
 */
public class PlusServiceFlashSalediscount extends PlusServiceTop implements
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

		//判断是否LD品
		boolean isLDGoods = false;
		if("SI2003".equals(plusSku.getSmallSellerCode())){ isLDGoods = true;}
		
		// 设置价格
		//折扣
		BigDecimal eventDiscount = plusEvent.getEventDiscount();
		//最大优惠金额
		BigDecimal maxDiscountMoney = plusEvent.getMaxDiscountMoney();
		//原价
		BigDecimal skuPrice = plusSku.getSkuPrice();
		//折后价格
		BigDecimal salePrice = skuPrice.multiply(eventDiscount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
		
		if(maxDiscountMoney.compareTo(BigDecimal.ZERO) > 0 && skuPrice.subtract(salePrice).compareTo(maxDiscountMoney) > 0) {
			plusSku.setSellPrice(isLDGoods ? skuPrice.subtract(maxDiscountMoney).setScale(0, RoundingMode.HALF_UP) : skuPrice.subtract(maxDiscountMoney));
		}else {
			plusSku.setSellPrice(isLDGoods ? salePrice.setScale(0, RoundingMode.HALF_UP) : salePrice);
		}

	}

}
