package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * <p>按提货券SKU购买件数拆单</p>
 * 到此步骤时须确保已经对提货券商品拆单<br>
 * 将提货券商品按、SKU、数量拆分成一单一货的订单.
 * 拆分明细表oc_orderdetail中一个SKU购买多个提货券的记录为多条.
 * 拆分支付表oc_order_pay中使用的优惠金额为多条记录.
 * 拆分活动表oc_order_activty中的记录为多条.
 */
public class TeslaMakeVoucherSplit  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
//		// 商品明细
//		List<TeslaModelOrderDetail> orderDetailList = teslaOrder.getOrderDetails();
//		// 支付金额明细
//		List<TeslaModelOrderPay> orderPayList = teslaOrder.getOcOrderPayList();
//		// 活动金额明细
//		List<TeslaModelOrderActivity> orderActivityList = teslaOrder.getActivityList();
//		// 订单列表
//		List<TeslaModelOrderInfo> orderList = teslaOrder.getSorderInfo();
//		
//		Map<String, TeslaModelOrderDetail> orderDetailMap = new HashMap<String, TeslaModelOrderDetail>();
//		Map<String, List<TeslaModelOrderPay>> orderPayMap = new HashMap<String, List<TeslaModelOrderPay>>();
//		Map<String, List<TeslaModelOrderActivity>> orderActivityMap = new HashMap<String, List<TeslaModelOrderActivity>>();
//		Map<String, TeslaModelOrderInfo> orderMap = new HashMap<String, TeslaModelOrderInfo>();
//		
//		for(TeslaModelOrderDetail dt : orderDetailList){
//			orderDetailMap.put(dt.getSkuCode(), dt);
//		}
//		
//		for(TeslaModelOrderPay op : orderPayList){
//			if(!orderPayMap.containsKey(op.getOrderCode())){
//				orderPayMap.put(op.getOrderCode(), new ArrayList<TeslaModelOrderPay>());
//			}
//			orderPayMap.get(op.getOrderCode()).add(op);
//		}
//		
//		for(TeslaModelOrderActivity oa : orderActivityList){
//			if(!orderActivityMap.containsKey(oa.getOrderCode())){
//				orderActivityMap.put(oa.getOrderCode(), new ArrayList<TeslaModelOrderActivity>());
//			}
//			orderActivityMap.get(oa.getOrderCode()).add(oa);
//		}
//		
//		for(TeslaModelOrderInfo od : orderList){
//			orderMap.put(od.getOrderCode(), od);
//		}
//		
//		// 拆分后新增商品明细
//		List<TeslaModelOrderDetail> newOrderDetailList = new ArrayList<TeslaModelOrderDetail>();
//		// 拆分后新增支付金额明细
//		List<TeslaModelOrderPay> newOrderPayList = new ArrayList<TeslaModelOrderPay>();
//		// 拆分后新增活动金额明细
//		List<TeslaModelOrderActivity> newOrderActivityList = new ArrayList<TeslaModelOrderActivity>();
//		// 拆分后新增订单
//		List<TeslaModelOrderInfo> newOrderList = new ArrayList<TeslaModelOrderInfo>();
//		
//		LoadProductInfo load = new LoadProductInfo();
//		PlusModelProductInfo productInfo;
//		TeslaModelOrderInfo orderInfo, newOrderInfo;
//		List<TeslaModelOrderPay> payList;
//		List<TeslaModelOrderActivity> activityList;
//		
//		TeslaModelOrderPay orderPay;
//		TeslaModelOrderActivity orderActivity;
//		TeslaModelOrderDetail orderDetail;
//		try {
//			// 循环SKU明细拆分订单
//			for(TeslaModelOrderDetail detail : orderDetailList){
//				productInfo = load.upInfoByCode(new PlusModelProductQuery(detail.getProductCode()));
//				orderInfo = orderMap.get(detail.getOrderCode());
//				payList = orderPayMap.get(detail.getOrderCode()) != null ? orderPayMap.get(detail.getOrderCode()) : new ArrayList<TeslaModelOrderPay>();
//				activityList = orderActivityMap.get(detail.getOrderCode()) != null ? orderActivityMap.get(detail.getOrderCode()) : new ArrayList<TeslaModelOrderActivity>();
//				
//				// 忽略非提货券商品和只有一件的
//				if(!"449747110002".equals(productInfo.getVoucherGood()) || detail.getSkuNum() <= 1){
//					newOrderDetailList.add(detail);
//					// 判断是否已经添加过，避免重复数据
//					if(!newOrderList.contains(orderInfo)){
//						newOrderList.add(orderInfo);
//						newOrderPayList.addAll(payList);
//						newOrderActivityList.addAll(activityList);
//					}
//					continue;
//				}
//				
//				// 总拆分的积分
//				BigDecimal integralMoney = detail.getIntegralMoney();
//				// 已经拆分的积分
//				BigDecimal useMoney = BigDecimal.ZERO;
//				// 根据购买数量拆单
//				for(int i = 0; i< detail.getSkuNum(); i++){
//					// 每个商品拆分一单
//					newOrderInfo = (TeslaModelOrderInfo)BeanUtils.cloneBean(orderInfo);
//					// 拆分金额
//					newOrderInfo.setDueMoney(newOrderInfo.getDueMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//					newOrderInfo.setOrderMoney(newOrderInfo.getOrderMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//					newOrderInfo.setProductMoney(newOrderInfo.getProductMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//					newOrderInfo.setPromotionMoney(newOrderInfo.getPromotionMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//					newOrderInfo.setTransportMoney(newOrderInfo.getTransportMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//					newOrderInfo.setOrderCode(KvHelper.upCode("DD"));
//					newOrderList.add(newOrderInfo);
//					
//					// 拆商品明细 
//					orderDetail = (TeslaModelOrderDetail) BeanUtils.cloneBean(detail);
//					orderDetail.setOrderCode(newOrderInfo.getOrderCode());
//					orderDetail.setSkuNum(1);
//					
//					// 拆分积分
//					BigDecimal money = integralMoney.divide(new BigDecimal(detail.getSkuNum())).setScale(0, BigDecimal.ROUND_UP);
//					if(useMoney.add(money).compareTo(integralMoney) > 0){
//						money = integralMoney.subtract(money);
//					}
//					useMoney = useMoney.add(money);
//					orderDetail.setIntegralMoney(money);
//					
//					
//					newOrderDetailList.add(orderDetail);
//					
//					// 拆支付明细
//					for(TeslaModelOrderPay op : payList){
//						orderPay = (TeslaModelOrderPay)BeanUtils.cloneBean(op);
//						orderPay.setOrderCode(newOrderInfo.getOrderCode());
//						if(orderPay.getPayType().equals("449746280008")){
//							// 积分拆分直接使用商品上的汇总
//							orderPay.setPayedMoney(money);
//						}else{
//							// 拆分金额
//							orderPay.setPayedMoney(orderPay.getPayedMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//							orderPay.setPayedAllFee(orderPay.getPayedAllFee().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//							orderPay.setPayedFee(orderPay.getPayedFee().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//							
//							if(StringUtils.isNotBlank(op.getPayRemark())){
//								// 微公社出人品奖励时扣减的微公社余额会小于实际使用的金额
//								try {
//									orderPay.setPayRemark(new BigDecimal(op.getPayRemark()).divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP).toString());
//								} catch (Exception e) {
//									e.printStackTrace();
//									orderPay.setPayRemark(op.getPayedMoney().toString());
//								}
//							}
//						}
//						newOrderPayList.add(orderPay);
//					}
//					
//					Iterator<TeslaModelOrderActivity> itor = activityList.iterator();
//					// 拆活动明细
//					TeslaModelOrderActivity oa;
//					while(itor.hasNext()){
//						oa = itor.next();
//						orderActivity = (TeslaModelOrderActivity)BeanUtils.cloneBean(oa);
//						// 人品奖励不拆分，直接分到第一个订单上
//						if(StringUtils.isBlank(oa.getSkuCode()) && oa.getPreferentialMoney().compareTo(new BigDecimal(0)) > 0){
//							itor.remove();
//						}
//						// 拆分优惠券到单个提货券订单上
//						if(StringUtils.isNotBlank(oa.getSkuCode()) && StringUtils.startsWith(oa.getTicketCode(), "CP") && oa.getPreferentialMoney().compareTo(new BigDecimal(0)) > 0){
//							orderActivity.setPreferentialMoney(orderActivity.getPreferentialMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP));
//						}
//						orderActivity.setOrderCode(newOrderInfo.getOrderCode());
//						newOrderActivityList.add(orderActivity);
//					}
//				}
//			}
//			
//			// 重设拆分后的订单数据
//			teslaOrder.setSorderInfo(newOrderList);
//			teslaOrder.setOrderDetails(newOrderDetailList);
//			teslaOrder.setOcOrderPayList(newOrderPayList);
//			teslaOrder.setActivityList(newOrderActivityList);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
		
		return new TeslaXResult();
	}

}
