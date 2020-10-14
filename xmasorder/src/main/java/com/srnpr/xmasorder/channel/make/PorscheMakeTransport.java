package com.srnpr.xmasorder.channel.make;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelSubRole;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmasorder.service.TeslaFreight;

/**
 * 运费计算 <br/>
 * 规则：多个商品时取运费最高的为订单运费
 * @remark 
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeTransport extends PorscheTopOrderMake {

	private final TeslaFreight teslaFreight = new TeslaFreight();
	
	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		
		PorscheGtResult gtResult = new PorscheGtResult();
		
		//获取区域编码
		String areaCode=porscheGtOrder.getAddress().getAreaCode();
		
		//获取拆单信息
		List<PorscheModelOrderInfo>  orderInfos = porscheGtOrder.getOrderInfo();
		
		//获取详情
		List<PorscheModelOrderDetail>  orderDetails = porscheGtOrder.getOrderDetails();
		
		//订单详情以订单号分组
		Multimap<String, PorscheModelOrderDetail> detailMap=ArrayListMultimap.create();
		for (PorscheModelOrderDetail porscheModelOrderDetail : orderDetails) {
			if("1".equals(porscheModelOrderDetail.getGiftFlag())){
				detailMap.put(porscheModelOrderDetail.getOrderCode(), porscheModelOrderDetail);
			}
		}
		
		Map<String, BigDecimal> orderTransportMoneyMap=new HashMap<String, BigDecimal>();
		for (String orderCode : detailMap.keys()) {
			orderTransportMoneyMap.put(orderCode, getTransportMoneyByDetail(detailMap.get(orderCode),areaCode,porscheGtOrder));
		}
		
		//设置小单的运费
		for (PorscheModelOrderInfo orderInfo : orderInfos) {
			orderInfo.setTransportMoney(orderTransportMoneyMap.get(orderInfo.getOrderCode()));
		}
		
		//设置拆单规则的的运费
		for (PorscheModelSubRole subRole : porscheGtOrder.getRoles()) {
			subRole.setTransportMoney(orderTransportMoneyMap.get(subRole.getOrderCode()));
		}
		
		return gtResult;
	}
	
	private BigDecimal getTransportMoneyByDetail(Collection<PorscheModelOrderDetail> orderDetails,String areaCode,PorscheGtOrder porscheGtOrder){
		
		Map<String,	Integer> productNumMap = new HashMap<String, Integer>();
		
		for (PorscheModelOrderDetail orderDetail : orderDetails) {
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
		
		return freightMoneyMax;
	}
	
}
