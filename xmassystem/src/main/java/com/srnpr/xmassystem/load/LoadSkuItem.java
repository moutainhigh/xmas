package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSkuItem;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载活动明细信息 本方法缓存的活动明细对应的商品的信息
 * 
 * @author srnpr
 *
 */
public class LoadSkuItem extends LoadTopMain<PlusModelSkuInfo, PlusModelSkuQuery> {

	public PlusModelSkuInfo topInitInfo(PlusModelSkuQuery query) {

		MDataMap mItemMap = DbUp.upTable("sc_event_item_product").one(
				"item_code", query.getCode());

		String sSkuCode = mItemMap.get("sku_code");

		PlusModelSkuInfo plusSku = new PlusSupportProduct()
				.initBaseInfoFromDb(sSkuCode);

		plusSku.setEventCode(mItemMap.get("event_code"));

		plusSku.setItemCode(mItemMap.get("item_code"));

		XmasKv.upFactory(EKvSchema.EventChildren).hset(plusSku.getEventCode(),
				plusSku.getItemCode(), "");

		return plusSku;

	}

	private final static PlusConfigSkuItem PLUS_CONFIG = new PlusConfigSkuItem();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

	@Override
	public PlusModelSkuInfo topInitInfoMain(PlusModelSkuQuery query) {
		MDataMap mItemMap = DbUp.upTable("sc_event_item_product").onePriLib("item_code", query.getCode());

		String sSkuCode = mItemMap.get("sku_code");
		PlusModelSkuInfo plusSku = new PlusSupportProduct().initBaseInfoFromDbMain(sSkuCode);

		plusSku.setEventCode(mItemMap.get("event_code"));
		plusSku.setItemCode(mItemMap.get("item_code"));
		XmasKv.upFactory(EKvSchema.EventChildren).hset(plusSku.getEventCode(), plusSku.getItemCode(), "");
		return plusSku;
	}

}
