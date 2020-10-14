package com.srnpr.xmassystem.service;

import org.apache.commons.collections.MapUtils;

import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 拼团活动处理类
 */
public class PlusServiceCollage extends PlusServiceTop implements IPlusServiceProduct {

	@Override
	public void refreshSkuInfo(PlusModelSkuInfo plusSku, PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {
		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
		
		String icCode = plusSku.getItemCode();
		PlusModelEventItemProduct plusItemProduct = plusSupportEvent.upItemProductByIcCode(icCode);
		checkForProduct(plusSku, plusQuery, plusEvent, plusItemProduct);//商品相关的通用检查和设置
		
		// 特殊判断 如果购买状态不为立即购买 则返回空的活动编号和ic编号 按原价购买
		if (plusSku.getBuyStatus() != 1&&plusSku.getBuyStatus() != 7&&plusSku.getBuyStatus() != 6) {
			plusSku.setEventCode("");
			plusSku.setEventType("");
			plusSku.setBuyStatus(1);
			plusSku.setSellPrice(plusSku.getSkuPrice());
			plusSku.setSellNote("");
			plusSku.setDescriptionUrlHref("");
			plusSku.setButtonText("立即购买");
			return;
		}
		
		MDataMap eventMap = DbUp.upTable("sc_event_info").one("event_code", plusSku.getEventCode());
		plusSku.setCollagePersonCount(MapUtils.getString(eventMap, "collage_person_count", "0"));//几人团
		plusSku.setSellPrice(plusItemProduct.getPriceEvent());//设置活动价
		plusSku.setIsCollage("1");//拼团标示
		plusSku.setCollageEndTime(plusEvent.getEndTime());
		plusSku.setCollageType(MapUtils.getString(eventMap, "collage_type", "4497473400050001"));
	}
}
