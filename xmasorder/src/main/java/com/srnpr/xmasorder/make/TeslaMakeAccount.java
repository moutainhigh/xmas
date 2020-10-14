package com.srnpr.xmasorder.make;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/**
 * 核算
 * 
 * 主要核算小订单内的金额
 * 
 * @author xiegj
 * 
 * 
 * 商品总金额=活动商品金额之和
 * 商品活动金额=商品原价与活动金额差额之和
 * 运费=拆单后小订单运费之和
 * 订单金额=商品金额-满减活动金额(无满减活动为0)
 * 应付款=订单金额+运费-优惠券-微公社支付(此处无优惠券及微公社计算)
 */
public class TeslaMakeAccount extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		//积分商城积分抵扣金额
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			BigDecimal integralMoney = BigDecimal.ZERO;
			TeslaModelOrderInfo orderInfo = teslaOrder.getSorderInfo().get(i);
			for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
				TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(j);
				if(detail.getOrderCode().equals(orderInfo.getOrderCode())){
					// 积分兑换订单 不参与任何活动，所以默认商品的兑换金额就是商品售价
					// 如果兑换商品结算类型是服务费结算且金额小于成本价时会重置ShowPrice为商品成本价
					// 所以计算商品金额时需要使用SkuPrice字段
					if(StringUtils.isNotBlank(detail.getIntegralDetailId())
							&& "4497471600110003".equals(detail.getSettlementType())){
						teslaOrder.getSorderInfo().get(i).setProductMoney(
								teslaOrder.getSorderInfo().get(i).getProductMoney()
										.add(detail.getSkuPrice().multiply(BigDecimal.valueOf(detail.getSkuNum()))));// 商品总金额=活动商品金额之和
					}else{
						teslaOrder.getSorderInfo().get(i).setProductMoney(
								teslaOrder.getSorderInfo().get(i).getProductMoney()
										.add(detail.getShowPrice().multiply(BigDecimal.valueOf(detail.getSkuNum()))));// 商品总金额=活动商品金额之和
					}
					
					teslaOrder.getSorderInfo().get(i).setPromotionMoney(
							teslaOrder.getSorderInfo().get(i).getPromotionMoney()
									.add(detail.getSaveAmt().multiply(BigDecimal.valueOf(detail.getSkuNum()))));// 商品活动金额=商品原价与活动金额差额之和
					integralMoney = integralMoney.add(detail.getIntegralMoney());
				}
			}
			
			teslaOrder.getSorderInfo().get(i).getProductMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).getTransportMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).setOrderMoney(teslaOrder.getSorderInfo().get(i).getProductMoney().subtract(teslaOrder.getSorderInfo().get(i).getPromotionMoney()));//订单金额=商品金额-满减活动金额(无满减活动为0)
			teslaOrder.getSorderInfo().get(i).getOrderMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).setDueMoney(teslaOrder.getSorderInfo().get(i).getOrderMoney().add(teslaOrder.getSorderInfo().get(i).getTransportMoney()));//应付款=订单金额+运费-优惠券-微公社支付(此处还无优惠券及微公社先关计算)
			teslaOrder.getSorderInfo().get(i).getDueMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).setDueMoney(teslaOrder.getSorderInfo().get(i).getDueMoney().subtract(integralMoney));
			teslaOrder.getUorderInfo().setDueMoney(teslaOrder.getUorderInfo().getDueMoney().add(teslaOrder.getSorderInfo().get(i).getDueMoney()));
		}
		/*for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			teslaOrder.getSorderInfo().get(i).getProductMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).getTransportMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).setOrderMoney(teslaOrder.getSorderInfo().get(i).getProductMoney().subtract(teslaOrder.getSorderInfo().get(i).getPromotionMoney()));//订单金额=商品金额-满减活动金额(无满减活动为0)
			teslaOrder.getSorderInfo().get(i).getOrderMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getSorderInfo().get(i).setDueMoney(teslaOrder.getSorderInfo().get(i).getOrderMoney().add(teslaOrder.getSorderInfo().get(i).getTransportMoney()));//应付款=订单金额+运费-优惠券-微公社支付(此处还无优惠券及微公社先关计算)
			teslaOrder.getSorderInfo().get(i).getDueMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);
			teslaOrder.getUorderInfo().setDueMoney(teslaOrder.getUorderInfo().getDueMoney().add(teslaOrder.getSorderInfo().get(i).getDueMoney()));
		}*/
		return result;
	}
	


}
