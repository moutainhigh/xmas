package com.srnpr.xmasorder.make;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/**
 * 简单计算订单
 * 
 * @author xiegj
 *
 */
public class TeslaMakeReckon extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
//		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
//			TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(i);
//			BigDecimal pm = teslaOrder.getOrderInfo().getProductMoney();
//			teslaOrder.getOrderInfo().setProductMoney(pm.add(detail.getSkuPrice()));
//		}
		return result;
	}

}
