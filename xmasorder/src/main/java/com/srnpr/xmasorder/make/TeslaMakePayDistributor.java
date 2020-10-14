package com.srnpr.xmasorder.make;

import java.util.List;

import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.DateUtil;

/**
 * 创建标准支付信息
 * @author jlin
 *
 */
public class TeslaMakePayDistributor extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult xResult = new TeslaXResult();
		
		List<TeslaModelOrderInfo> orderList = teslaOrder.getSorderInfo();
		for (TeslaModelOrderInfo teslaModelOrderInfo : orderList) {
			
			TeslaModelOrderPay orderPay = new TeslaModelOrderPay();
			orderPay.setOrderCode(teslaModelOrderInfo.getOrderCode());
			orderPay.setPayedMoney(teslaModelOrderInfo.getOrderMoney());
			orderPay.setCreateTime(DateUtil.getSysDateTimeString());
			orderPay.setPayType(bConfig("xmasorder.FX_PAY_TYPE"));
			orderPay.setMerchantId(teslaModelOrderInfo.getBuyerCode());
			
			teslaOrder.getOcOrderPayList().add(orderPay);
		}
		
		return xResult;
	}
	
}
