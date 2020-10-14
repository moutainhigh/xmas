package com.srnpr.xmasorder.make;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/***
 * 处理分销系统运费(还可增加其他操作)
 * @author xiegj
 *
 */
public class TeslaMakeReckonFX extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		//处理分销系统运费
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			teslaOrder.getSorderInfo().get(i).setTransportMoney(teslaOrder.getOrderOther().getCarriageFXMoney());
		}
		return result;
	}

}
