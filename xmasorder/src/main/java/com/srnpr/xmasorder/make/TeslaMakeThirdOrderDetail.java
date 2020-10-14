package com.srnpr.xmasorder.make;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderDetailForThird;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
/**
 * 初始化第三方订单详情(暂时仅为多彩订单)
 * @author renhongbin
 *
 */
public class TeslaMakeThirdOrderDetail extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
	   TeslaXResult result = new TeslaXResult();
	   
	   if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())) {
		   
		   //多彩订单状态转换
		   for(TeslaModelOrderInfo orderInfo: teslaOrder.getSorderInfo()) {
			   orderInfo.setOrderStatus("4497153900010002");
			   orderInfo.setOutOrderCode(teslaOrder.getOrderOther().getOut_order_code());
			   orderInfo.setAppVersion(teslaOrder.getUorderInfo().getAppVersion());
			   orderInfo.setPayType(teslaOrder.getUorderInfo().getPayType());
		   }
		   
		   //初始化第三方订单详情
		   List<TeslaModelOrderDetailForThird> orderDetailList = new ArrayList<TeslaModelOrderDetailForThird>();
		   for (TeslaModelOrderDetail orderDetail : teslaOrder.getOrderDetails()) {
			   TeslaModelOrderDetailForThird thirdOrderDetail = new TeslaModelOrderDetailForThird();
			   thirdOrderDetail.setUid(orderDetail.getUid());
			   thirdOrderDetail.setOrderCode(orderDetail.getOrderCode());
			   thirdOrderDetail.setProductCode(orderDetail.getProductCode());
			   thirdOrderDetail.setSkuCode(orderDetail.getSkuCode());
			   thirdOrderDetail.setSkuName(orderDetail.getSkuName());
			   thirdOrderDetail.setSkuNum(orderDetail.getSkuNum());
			   thirdOrderDetail.setThirdSkuPrice(orderDetail.getThirdSellPrice());
			   thirdOrderDetail.setThirdCostPrice(orderDetail.getThirdCostPrice());
			   thirdOrderDetail.setThirdSupplyPrice(orderDetail.getShowPrice());
			   thirdOrderDetail.setSmallSellerCode(orderDetail.getSmallSellerCode());
			   thirdOrderDetail.setGiftFlag(orderDetail.getGiftFlag());
			   thirdOrderDetail.setGiftCd(orderDetail.getGiftCd());
			   thirdOrderDetail.setOrderType(teslaOrder.getUorderInfo().getOrderType());
			   thirdOrderDetail.setCreateTime(teslaOrder.getOrderOther().getCreateTime());
			   orderDetailList.add(thirdOrderDetail);
		   }
		   
		   teslaOrder.setThirdDetail(orderDetailList);
	   }
	   return result;
	}
}
