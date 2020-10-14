package com.srnpr.xmassystem.face;

import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;

public interface IPlusServiceProduct extends IPlusService {

	/**
	 * 刷新SKU的最新信息
	 * 
	 * @param plusSku
	 * @param plusQuery
	 * @param plusEvent
	 */
	public void refreshSkuInfo(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent);

}
