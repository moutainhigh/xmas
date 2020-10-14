package com.srnpr.xmasorder.channel.make;

import java.math.BigDecimal;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;

/**
 * 核算
 * @remark 计算订单金额
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeAccount extends PorscheTopOrderMake {

	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		PorscheGtResult result = new PorscheGtResult();

		for (int i = 0; i < porscheGtOrder.getOrderInfo().size(); i++) {
			PorscheModelOrderInfo orderInfo = porscheGtOrder.getOrderInfo().get(i);
			for (int j = 0; j < porscheGtOrder.getOrderDetails().size(); j++) {
				PorscheModelOrderDetail detail = porscheGtOrder.getOrderDetails().get(j);
				if(detail.getOrderCode().equals(orderInfo.getOrderCode())){
					// 所以计算商品金额时需要使用SkuPrice字段
					porscheGtOrder.getOrderInfo().get(i).setProductMoney(
							porscheGtOrder.getOrderInfo().get(i).getProductMoney()
									.add(detail.getSkuPrice().multiply(BigDecimal.valueOf(detail.getSkuNum()))));// 商品总金额=活动商品金额之和
					porscheGtOrder.getOrderInfo().get(i).getProductMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
				}
			}
			
			//小订单金额
			porscheGtOrder.getOrderInfo().get(i).setOrderMoney(porscheGtOrder.getOrderInfo().get(i).getProductMoney());
			//大订单金额累加
			porscheGtOrder.getOrderInfoUpper().setOrderMoney(porscheGtOrder.getOrderInfoUpper().getOrderMoney()
					.add(porscheGtOrder.getOrderInfo().get(i).getOrderMoney()));
			//小单运费
			porscheGtOrder.getOrderInfo().get(i).getTransportMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			//小单应付款
			porscheGtOrder.getOrderInfo().get(i).setDueMoney(porscheGtOrder.getOrderInfo().get(i).getOrderMoney()
					.add(porscheGtOrder.getOrderInfo().get(i).getTransportMoney()));
			porscheGtOrder.getOrderInfo().get(i).getDueMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			//大单应付款
			porscheGtOrder.getOrderInfoUpper().setDueMoney(porscheGtOrder.getOrderInfoUpper().getDueMoney()
					.add(porscheGtOrder.getOrderInfo().get(i).getDueMoney()));
			//小单已付款
			porscheGtOrder.getOrderInfo().get(i).setPayedMoney(porscheGtOrder.getOrderInfo().get(i).getDueMoney());
		}
		//大单所有金额
		porscheGtOrder.getOrderInfoUpper().setAllMoney(porscheGtOrder.getOrderInfoUpper().getDueMoney());
		//大单已付款
		porscheGtOrder.getOrderInfoUpper().setPayedMoney(porscheGtOrder.getOrderInfoUpper().getDueMoney());
		return result;
	}
}
