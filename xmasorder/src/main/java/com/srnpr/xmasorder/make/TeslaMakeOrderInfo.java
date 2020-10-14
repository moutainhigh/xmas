package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;


/**
 * 特殊订单处理
 * @author fq
 *
 */
public class TeslaMakeOrderInfo extends TeslaTopOrderMake{

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		/**
		 * 查询需要更改的订单类型  [ 百度外卖订单（订单类型：449715200015；订单来源：449715190012）、电视宝商城订单（订单类型：449715200017；订单来源：449715190013），都是导入订单  将下0元单  ]
		 */
		String orderTypeStr = WebHelper.getImportOrderSource()+"";
		
		List<String> orderTypeList = Arrays.asList(orderTypeStr.split(","));
		
		if( teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
			if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getOrderType()) && orderTypeList.contains(teslaOrder.getUorderInfo().getOrderType())) {//如果包含则更改订单信息（订单为0元单）
				//修改支付类型
				if("449715200015".equals(teslaOrder.getUorderInfo().getOrderType())) {
					teslaOrder.getUorderInfo().setPayType("449716200005");
				} else if("449715200017".equals(teslaOrder.getUorderInfo().getOrderType())) {
					teslaOrder.getUorderInfo().setPayType("449716200006");
				}
				
				//订单信息处理
				result = orderHandle(teslaOrder);
			}
			
		}
		
		return result;
		
	}
	/**
	 * 订单信息特殊处理
	 * @param teslaOrder
	 * @return
	 */
	private TeslaXResult orderHandle(TeslaXOrder teslaOrder) {

		TeslaXResult result = new TeslaXResult();
		//修改 订单价格 为 应付款金额
		teslaOrder.getCheck_pay_money();
		teslaOrder.getUorderInfo().setDueMoney(teslaOrder.getCheck_pay_money());//应付款
		
		/*
		 * 将订单状态置为下单成功未发货
		 */
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			
			teslaOrder.getSorderInfo().get(i).setOrderStatus("4497153900010002");
			teslaOrder.getSorderInfo().get(i).setDueMoney(new BigDecimal(0.00));
			teslaOrder.getSorderInfo().get(i).setOrderMoney(new BigDecimal(0.00));
			
			/*
			 * 修改支付类型
			 */
			teslaOrder.getSorderInfo().get(i).setPayType(teslaOrder.getUorderInfo().getPayType());
			
			//设置版本号
			teslaOrder.getSorderInfo().get(i).setAppVersion(teslaOrder.getUorderInfo().getAppVersion());
			
			//设置外部订单号
			if(StringUtils.isNotBlank(teslaOrder.getOrderOther().getOut_order_code())) {
				teslaOrder.getSorderInfo().get(i).setOutOrderCode(teslaOrder.getOrderOther().getOut_order_code());
			} 
			else {
				result.inErrorMessage(0,
						teslaOrder.getOrderDetails().get(i).getProductCode());
			}
			
		}
		
		//获取sku 正常售价（活动商品也只取正常售价，不取活动价）
		Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>();
		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
			
			PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct()
			.upSkuInfoBySkuCode(teslaOrder.getOrderDetails().get(i)
					.getSkuCode(), teslaOrder.getUorderInfo()
					.getBuyerCode(),teslaOrder.getIsMemberCode(),teslaOrder.getIsPurchase());
			
			priceMap.put(teslaOrder.getOrderDetails().get(i)
					.getSkuCode(), plusModelSkuInfo.getSkuPrice());
		}
		
		/*
		 * 将订单价格，和sku价格置为0
		 */
		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
			//多彩下单商品跳过此步
			if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())) {
				break;
			}
			teslaOrder.getOrderDetails().get(i).setSkuPrice(priceMap.get(teslaOrder.getOrderDetails().get(i).getSkuCode()));
		}
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
			teslaOrder.getShowGoods().get(i).setSkuPrice(priceMap.get(teslaOrder.getShowGoods().get(i).getSkuCode()));
		}
		teslaOrder.getUorderInfo().setDueMoney(new BigDecimal(0.00));//应付款
		teslaOrder.getUorderInfo().setOrderMoney(new BigDecimal(0.00));
		teslaOrder.getUorderInfo().setAllMoney(new BigDecimal(0.00));//
		
		return result;
	
	}

}
