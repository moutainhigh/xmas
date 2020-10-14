package com.srnpr.xmassystem.load;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSkuItem;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载子活动专用
 * 
 * @author srnpr
 *
 */
public class LoadSubItem extends LoadTop<PlusModelSkuInfo, PlusModelSkuQuery> {
	public PlusModelSkuInfo topInitInfo(PlusModelSkuQuery query) {

		String sEventCode = XmasKv.upFactory(EKvSchema.SubEventCode).get(
				query.getSourceCode());

		String sSkuCode = PlusHelperEvent.upSubSkuCode(query.getCode());

		PlusModelSkuInfo plusSku = new PlusSupportProduct()
				.initBaseInfoFromDb(sSkuCode);

		plusSku.setEventCode(sEventCode);

		XmasKv.upFactory(EKvSchema.EventChildren).hset(query.getSourceCode(),
				query.getCode(), "");
		
		
		

		XmasKv.upFactory(EKvSchema.ProductIcChildren).hset(
				plusSku.getProductCode(), query.getCode(), query.getCode());

		// 初始化对应的库存信息

		plusSku.setItemCode(query.getCode());

		return plusSku;

	}

	private final static PlusConfigSkuItem PLUS_CONFIG = new PlusConfigSkuItem();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}
}
