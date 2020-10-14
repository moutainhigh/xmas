package com.srnpr.xmasorder.make;

import java.util.List;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/**
 * 微匠支付条件过滤
 * 不支持拆单所以不能多个商户的商品一起购买
 */
public class TeslaMakeVJPay  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		List<TeslaModelOrderDetail> list = teslaOrder.getOrderDetails();

		// 不是微匠支付则直接放行
		if(!"449746280016".equals(teslaOrder.getUorderInfo().getPayType())){
			return xResult;
		}
		
		String smallSellerCode = null;
		for(TeslaModelOrderDetail item : list){
			if(smallSellerCode == null){
				smallSellerCode = item.getSmallSellerCode();
			}
			
			if(!smallSellerCode.equalsIgnoreCase(item.getSmallSellerCode())){
				xResult.setResultCode(0);
				xResult.setResultMessage(bConfig("xmasorder.vjpay_create_order"));
				break;
			}
		}
		return xResult;
	}

}
