package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/***
 * 各种钱的计算
 * @author jlin
 *
 */
public class TeslaMakeReckonIqiyi extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		//合并订单详情
		Multimap<String, TeslaModelOrderDetail> detailMap=ArrayListMultimap.create();
		for (TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
			detailMap.put(detail.getOrderCode(), detail);
		}
		
		List<TeslaModelOrderInfo> sorderInfos = teslaOrder.getSorderInfo();
		
		//目前不考虑运费
		for (TeslaModelOrderInfo teslaModelOrderInfo : sorderInfos) {
			
			String orderCode=teslaModelOrderInfo.getOrderCode();
			BigDecimal productMoney=BigDecimal.ZERO;//商品金额
			
			Collection<TeslaModelOrderDetail> detailList=detailMap.get(orderCode);
			for (TeslaModelOrderDetail teslaModelOrderDetail : detailList) {
				productMoney=productMoney.add(teslaModelOrderDetail.getSkuPrice().multiply(new BigDecimal(String.valueOf(teslaModelOrderDetail.getSkuNum()))));
			}
			
			teslaModelOrderInfo.setProductMoney(productMoney);
			teslaModelOrderInfo.setOrderMoney(productMoney);
			teslaModelOrderInfo.setPayedMoney(productMoney);
			teslaModelOrderInfo.setDueMoney(productMoney);
		}
		
		return result;
	}

}
