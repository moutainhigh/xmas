package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 积分商城重新给sku售价，sku原始价格赋值，并校验sku售价之和+运费是否等于应付款项
 * @author
 *
 */
public class TeslaMakePointShop extends TeslaTopOrderMake {

	@SuppressWarnings("deprecation")
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		//重新给sku售价，原始价格赋值
		//当非积分商城订单进入，则不校验
		boolean isMoreStock = false;
		boolean isMoreUser = false;
		boolean isPointShop = false;
		BigDecimal totalSkuPrice = BigDecimal.ZERO;
		int useIntegral = 0;
		List<TeslaModelOrderDetail> newDetailList = new ArrayList<TeslaModelOrderDetail>();
		List<TeslaModelOrderDetail> detailList = teslaOrder.getOrderDetails();
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i ++) {
			TeslaModelOrderInfo orderInfo = teslaOrder.getSorderInfo().get(i);
			for(TeslaModelOrderDetail detail : detailList) {
				if(orderInfo.getOrderCode().equals(detail.getOrderCode())) {
					String skuCode = detail.getSkuCode();
					String integralDetailId = detail.getIntegralDetailId();
					if(!"".equals(integralDetailId)) {
						Map<String, Object> skuInfo = DbUp.upTable("pc_skuinfo").dataSqlOne("select one_allowed, allow_count, s.sell_price, d.extra_charges, d.jf_cost from pc_skuinfo s, "
								+ "familyhas.fh_apphome_channel_details d where s.sku_code = '" + skuCode + "' " + "and d.uid = '" + integralDetailId + "' and d.product_code = s.product_code", new MDataMap());
						if(skuInfo != null) {
							BigDecimal sellPrice = new BigDecimal(MapUtils.getString(skuInfo, "sell_price", "0"));
							BigDecimal skuPrice = new BigDecimal(MapUtils.getString(skuInfo, "extra_charges", "0"));
							detail.setSellPrice(sellPrice);
							detail.setSkuPrice(new BigDecimal(MapUtils.getString(skuInfo, "jf_cost", "0")).add(new BigDecimal(MapUtils.getString(skuInfo, "extra_charges", "0"))));
							detail.setShowPrice(new BigDecimal(MapUtils.getString(skuInfo, "jf_cost", "0")).add(new BigDecimal(MapUtils.getString(skuInfo, "extra_charges", "0"))));
							detail.setIntegralMoney(new BigDecimal(MapUtils.getString(skuInfo, "jf_cost", "0")).multiply(new BigDecimal(detail.getSkuNum())));//积分抵扣钱
							totalSkuPrice = totalSkuPrice.add(skuPrice.multiply(new BigDecimal(detail.getSkuNum())));
							useIntegral += (MapUtils.getInteger(skuInfo, "jf_cost", 0) * 200 * detail.getSkuNum());
							
							// 服务费结算的商品销售价不能低于成本价，此处做特殊处理将销售价改为成本价
							if(detail.getCostPrice().compareTo(BigDecimal.ZERO) > 0 
									&& detail.getShowPrice().compareTo(detail.getCostPrice()) < 0
									&& "4497471600110003".equals(detail.getSettlementType())){
								detail.setShowPrice(detail.getCostPrice());
							}
							
							//判断是否超过最大活动库存
							if(detail.getSkuNum() > MapUtils.getIntValue(skuInfo, "allow_count", 0)) {
								isMoreStock = true;
							}
							
							//判断是否超过用户最大限购数
							int one_allowed = MapUtils.getIntValue(skuInfo, "one_allowed", 0);
							if(one_allowed != 0) {
								int buyedCount = DbUp.upTable("oc_orderinfo").upTemplate().queryForInt("select ifnull(sum(d.sku_num), 0) from oc_orderinfo i, oc_orderdetail d, "
										+ "oc_order_activity a where i.order_code = d.order_code and i.order_code = a.order_code and a.product_code = d.product_code and a.sku_code = d.sku_code and i.buyer_code = '"
										+ teslaOrder.getUorderInfo().getBuyerCode() + "' and i.order_status != '4497153900010006' and d.product_code = '" + detail.getProductCode() + "' " + "and a.activity_code = '" + integralDetailId + "'", 
										new HashMap<String, Object>());
								if((one_allowed - buyedCount) < detail.getSkuNum()) {
									isMoreUser = true;
								}
							}
						}
						
						isPointShop = true;
						newDetailList.add(detail);
					}
				}
			}
		}
		
		
		if(isPointShop) {
			//重新设置订单明细
			teslaOrder.setOrderDetails(newDetailList);
			
			//校验 sku售价之和+运费是否等于应付款项
			//当非积分商城订单进入，则不校验
			BigDecimal totalTransport = BigDecimal.ZERO;
			for(TeslaModelOrderInfo orderInfo : teslaOrder.getSorderInfo()) {
				totalTransport = totalTransport.add(orderInfo.getTransportMoney());
			}
			if(totalTransport.add(totalSkuPrice).compareTo(teslaOrder.getCheck_pay_money()) != 0) {
				result.setResultCode(963907001);
				result.setResultMessage(bInfo(963907001));
				return result;
			}
			teslaOrder.setPointShop(isPointShop);
			teslaOrder.getUse().setIntegral(useIntegral);
			
			//校验最大库存限制
			if(isMoreStock) {
				result.setResultCode(963907002);
				result.setResultMessage(bInfo(963907002));
				return result;
			}
			
			//校验用户最大购买数限制
			if(isMoreUser) {
				result.setResultCode(963907003);
				result.setResultMessage(bInfo(963907003));
				return result;
			}
		}
		return result;
	}
}
