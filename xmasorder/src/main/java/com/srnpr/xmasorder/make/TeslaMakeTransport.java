package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelSubRole;
import com.srnpr.xmasorder.service.TeslaFreight;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.service.PlusServiceFreeShipping;

/**
 * 运费计算 
 * 规则：多个商品时取运费最高的为订单运费
 * @author jlin
 *
 */
public class TeslaMakeTransport extends TeslaTopOrderMake {

	private final TeslaFreight teslaFreight = new TeslaFreight();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult xResult = new TeslaXResult();
		
		// 互动活动如果存在并且设置包邮则不计算运费
		if(teslaOrder.getHuDongEvent() != null && teslaOrder.getHuDongEvent().getOrderLimit().contains("449748520001")) {
			return xResult;
		}
		
		//获取区域编码
		String areaCode=teslaOrder.getAddress().getAreaCode();
		
//		//总运费
//		BigDecimal freightMoneyAll=BigDecimal.ZERO;
		
		//获取拆单信息
		List<TeslaModelOrderInfo>  orderInfos = teslaOrder.getSorderInfo();
		
		//获取详情
		List<TeslaModelOrderDetail>  orderDetails = teslaOrder.getOrderDetails();
		
		//订单详情以订单号分组
		Multimap<String, TeslaModelOrderDetail> detailMap=ArrayListMultimap.create();
		for (TeslaModelOrderDetail teslaModelOrderDetail : orderDetails) {
			if("1".equals(teslaModelOrderDetail.getGiftFlag())){
				detailMap.put(teslaModelOrderDetail.getOrderCode(), teslaModelOrderDetail);
			}
		}
		
		
//		for (TeslaModelOrderInfo orderInfo : orderInfos) {
//			orderInfo.setTransportMoney(getTransportMoneyByDetail(detailMap.get(orderInfo.getOrderCode()),areaCode));
//		}
		
		
		Map<String, BigDecimal> orderTransportMoneyMap=new HashMap<String, BigDecimal>();
		for (String orderCode : detailMap.keys()) {
			orderTransportMoneyMap.put(orderCode, getTransportMoneyByDetail(detailMap.get(orderCode),areaCode,teslaOrder));
		}
		if("SI3003".equals(teslaOrder.getUorderInfo().getSellerCode())){
			orderTransportMoneyMap=this.freightFreeForDog(teslaOrder, orderTransportMoneyMap);
		}
		//设置小单的运费
		for (TeslaModelOrderInfo orderInfo : orderInfos) {
			//多彩下单时 校验运费 -- rhb 20180806
			if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource()) 
					&& teslaOrder.getOrderOther().getThirdMoney().compareTo(orderTransportMoneyMap.get(orderInfo.getOrderCode())) !=0) {
				xResult.setResultCode(963909001);
				xResult.setResultMessage(bInfo(963909001));
				return xResult;
			}
				
			orderInfo.setTransportMoney(orderTransportMoneyMap.get(orderInfo.getOrderCode()));
		}
		
		//设置拆单规则的的运费
		for (TeslaModelSubRole subRole : teslaOrder.getRoles()) {
			subRole.setTransportMoney(orderTransportMoneyMap.get(subRole.getOrderCode()));
		}
		
		return xResult;
	}
	
	private BigDecimal getTransportMoneyByDetail(Collection<TeslaModelOrderDetail> orderDetails,String areaCode,TeslaXOrder teslaXOrder){
		
		Map<String,	Integer> productNumMap = new HashMap<String, Integer>();
		
		for (TeslaModelOrderDetail orderDetail : orderDetails) {
			String productCode = orderDetail.getProductCode();
			int  skuNum = orderDetail.getSkuNum();
			Integer num=productNumMap.get(productCode);
			productNumMap.put(productCode, num==null?(skuNum==0?0:skuNum):(num+skuNum));
		}
		
		//计算运费，子单中，所有的商品计算一次运费；子单运费 取 单下商品运费最高的一个
		BigDecimal freightMoneyMax=BigDecimal.ZERO;
		
		for (Map.Entry<String, Integer> freightMap : productNumMap.entrySet()) {

			String productCode = freightMap.getKey();
			int num = freightMap.getValue();
			
			//商品运费  取一个最大的值作为运费
			BigDecimal freightMoney = teslaFreight.getFreightMoney(productCode, num, areaCode);

			if (freightMoneyMax.compareTo(freightMoney) < 0) {
				freightMoneyMax = freightMoney;
			}

		}
		
		//---start by xiegj 促销系统内设置的运费相关活动
//		if(freightMoneyMax.compareTo(BigDecimal.ZERO)>0){//运费活动
//			BigDecimal orderMoney = BigDecimal.ZERO;
//			for (int j = 0; j < teslaXOrder.getOrderDetails().size(); j++) {
//				TeslaModelOrderDetail detail = teslaXOrder.getOrderDetails().get(j);
//				if("1".equals(teslaXOrder.getOrderDetails().get(j).getGiftFlag())){
//					orderMoney=orderMoney.add((detail.getShowPrice().subtract(detail.getSaveAmt())).multiply(BigDecimal.valueOf(detail.getSkuNum())));
//				}
//			}
//			freightMoneyMax = new PlusServiceFreeShipping().getFreeShipping(teslaXOrder.getUorderInfo().getBuyerCode(), orderMoney, freightMoneyMax, teslaXOrder.getUorderInfo().getSellerCode());
//		}
		//---end
		return freightMoneyMax;
	}
	private Map<String, BigDecimal> freightFreeForDog(TeslaXOrder teslaXOrder,Map<String, BigDecimal> orderTransportMoneyMap){
		Map<String, BigDecimal> map = orderTransportMoneyMap;
		if(!teslaXOrder.getSorderInfo().isEmpty()&&!orderTransportMoneyMap.isEmpty()){
			BigDecimal allproMoney = BigDecimal.ZERO;
			for (int i = 0; i < teslaXOrder.getOrderDetails().size(); i++) {
				allproMoney=allproMoney.add(teslaXOrder.getOrderDetails().get(i).getSkuPrice().multiply(BigDecimal.valueOf(teslaXOrder.getOrderDetails().get(i).getSkuNum())));
			}
			BigDecimal max = BigDecimal.ZERO;
			String ordercode = "";
			if(allproMoney.compareTo(BigDecimal.valueOf(Double.valueOf(bConfig("familyhas.full_amount"))))<0){
				for (Iterator<String> iterator = orderTransportMoneyMap.keySet().iterator(); iterator
						.hasNext();) {
					String key = iterator.next();
					if(orderTransportMoneyMap.get(key).compareTo(max)>0){
						max = orderTransportMoneyMap.get(key);
						ordercode=key;
					}
				}
				
			}
			if(Double.valueOf(bConfig("familyhas.full_amount"))>1){
				for (Iterator<String> iterator = orderTransportMoneyMap.keySet().iterator(); iterator
						.hasNext();) {
					String kk = iterator.next();
					map.put(kk, BigDecimal.ZERO);
					if(StringUtils.isNotBlank(ordercode)&&kk.equals(ordercode)){
						map.put(kk, max);
					}
				}
		}
		}
		return map;
	}
}
