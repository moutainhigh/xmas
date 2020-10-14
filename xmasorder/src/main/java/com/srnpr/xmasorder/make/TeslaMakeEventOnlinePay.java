package com.srnpr.xmasorder.make;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadEventExcludeProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventExclude;
import com.srnpr.xmassystem.modelevent.PlusModelEventOnlinePay;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.service.PlusServiceEventOnlinePay;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.topdo.TopConfig;

/**
 * 在线支付立减活动  <br>
 * 需要在积分、储值金、暂存款之前，避免整单实付金额出现负数
 */
public class TeslaMakeEventOnlinePay extends TeslaTopOrderMake {

	PlusServiceEventOnlinePay plusServiceEventOnlinePay = new PlusServiceEventOnlinePay();
	LoadEventExcludeProduct loadEventExcludeProduct = new LoadEventExcludeProduct();
	
	// 闪购订单 449715200004， 普通订单 449715200005，扫码购订单  449715200010
	String[] array = {"449715200004","449715200005","449715200010"};
	// 在线支付方式
	String[] onlinePayTypes = {"449716200001","449746280003","449746280005","449746280011","449746280013","449746280014","449746280016","449746280020"};
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		// 5.1.8以前的版本不支持
		if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getAppVersion())
				&& AppVersionUtils.compareTo("5.1.8", teslaOrder.getUorderInfo().getAppVersion()) > 0){
			return result;
		}
		//兑换码兑换、惠惠农场兑换不参与
		if(StringUtils.isNotEmpty(teslaOrder.getActivityCode()) && StringUtils.isNotEmpty(teslaOrder.getRedeemCode())
				|| StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
			return result;
		}
		
		// 互动活动不跟其他促销叠加
		if(teslaOrder.getHuDongEvent() != null) {
			return result;
		}
		
		// 限定只能这几个订单类型的支持
		if(!ArrayUtils.contains(array, teslaOrder.getUorderInfo().getOrderType())){
			return result;
		}
		
		PlusModelEventExclude excludeProduct = new PlusModelEventExclude();
		PlusModelEventOnlinePay event = null;
		BigDecimal favorablePrice = null;
		BigDecimal totalCutMoney = BigDecimal.ZERO;
		
		// 需要过滤的活动
		Map<String,String> eventMap = new HashMap<String, String>();
		for(TeslaModelOrderActivity oa : teslaOrder.getActivityList()){
			// 过滤的活动包含：内购、拼团、橙意卡特殊活动
			if(oa.getActivityType().equals("4497472600010006")
					|| oa.getActivityType().equals("4497472600010024")||oa.getActivityType().equals("4497472600010032")){
				eventMap.put(oa.getSkuCode(), oa.getSkuCode());
			} 
		}
		
		// 记录下每个订单上减少的金额
		Map<String,BigDecimal> cutMoneyOrderMap = new HashMap<String, BigDecimal>();
		
		List<TeslaModelOrderDetail> detailList = teslaOrder.getOrderDetails();
		for(TeslaModelOrderDetail detail : detailList) {
			// 仅限LD商品
			if(!"SI2003".equalsIgnoreCase(detail.getSmallSellerCode())
					|| !"1".equals(detail.getGiftFlag())|| "1".equals(detail.getIfJJGFlag())){
				continue;
			}
			
			// 20190626 临时需求，写死商品编号不参与立减10元活动
			if("236760".equals(detail.getProductCode())) {
				continue;
			}
			
			// 忽略参与内购的商品
			if(eventMap.containsKey(detail.getSkuCode())){
				continue;
			}
			
			// 分销商品不参与
			if(detail.getFxFlag() == 1) {
				continue;
			}
			
			// 橙意会员卡商品不参与任何促销活动
			if(detail.getProductCode().equals(TopConfig.Instance.bConfig("xmassystem.plus_product_code"))) {
				continue;
			}
			
			if(event == null){ // 延迟获取活动信息，减少不必要的活动查询
				event = plusServiceEventOnlinePay.getEventOnlinePay(teslaOrder.getChannelId());
				try {
					if(event != null){
						favorablePrice = new BigDecimal(event.getFavorablePrice()).setScale(2,BigDecimal.ROUND_HALF_UP);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(event == null
						|| favorablePrice == null
						|| favorablePrice.compareTo(BigDecimal.ZERO) <= 0){
					// 没有在线支付立减的活动则直接跳出循环
					break;
				}
				// 查询获得商品排除列表
				excludeProduct = loadEventExcludeProduct.upInfoByCode(new PlusModelQuery(event.getEventCode()));
			}
			
			// 在活动排除列表则不参与活动
			if(excludeProduct.getProductCodeList().contains(detail.getProductCode())) {
				continue;
			}
			
			// 立减的金额不能超过商品的单价
			BigDecimal cutMoneyPrice = detail.getSkuPrice().compareTo(favorablePrice) > 0 ? favorablePrice : detail.getSkuPrice();
			BigDecimal cutMoney = cutMoneyPrice.multiply(new BigDecimal(detail.getSkuNum())).setScale(2,BigDecimal.ROUND_HALF_UP);
			totalCutMoney = totalCutMoney.add(cutMoney);

			// 真正选择在线支付时减少实际付款的金额
			if(ArrayUtils.contains(onlinePayTypes, teslaOrder.getUorderInfo().getPayType())){
				detail.setSkuPrice(detail.getSkuPrice().subtract(cutMoneyPrice));
				
				// 修正整单的金额
				for (TeslaModelOrderInfo orderInfo : teslaOrder.getSorderInfo()) {
					if(orderInfo.getOrderCode().equals(detail.getOrderCode())&&"0".equals(detail.getIfJJGFlag())){
						orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(cutMoney));
						orderInfo.setOrderMoney(orderInfo.getOrderMoney().subtract(cutMoney));
					}
				}
				
				// 累计订单上面再减的金额
				if(cutMoneyOrderMap.containsKey(detail.getOrderCode())){
					cutMoneyOrderMap.put(detail.getOrderCode(), cutMoneyOrderMap.get(detail.getOrderCode()).add(cutMoney));
				}else{
					cutMoneyOrderMap.put(detail.getOrderCode(), cutMoney);
				}
				
				// 记录活动信息 
				TeslaModelOrderActivity oa = new TeslaModelOrderActivity();
				oa.setProductCode(detail.getProductCode());
				oa.setSkuCode(detail.getSkuCode());
				oa.setOrderCode(detail.getOrderCode());
				oa.setPreferentialMoney(cutMoney);
				oa.setActivityCode(event.getEventCode());
				oa.setActivityType(event.getEventType());
				
				teslaOrder.getActivityList().add(oa);
			}

		}
		
		// 在线支付立减活动优惠金额
		teslaOrder.setOnlinePayEventMoney(totalCutMoney);
		
		// 真正选择了在线支付时
		if(ArrayUtils.contains(onlinePayTypes, teslaOrder.getUorderInfo().getPayType()) && totalCutMoney.compareTo(BigDecimal.ZERO) > 0){
			// 记录优惠信息
			Set<Entry<String, BigDecimal>> entryList = cutMoneyOrderMap.entrySet();
			for(Entry<String, BigDecimal> entry : entryList){
				TeslaModelOrderPay op = new TeslaModelOrderPay();
				op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
				op.setOrderCode(entry.getKey());
				op.setPaySequenceid("");
				op.setPayedMoney(entry.getValue());
				op.setPayType("449746280019");
				teslaOrder.getOcOrderPayList().add(op);
			}
			
			// 记录抵扣的金额
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name("在线支付立减");
			discount.setDis_type("0");
			discount.setDis_price(totalCutMoney.doubleValue());
			teslaOrder.getShowMoney().add(discount);
			
			// 优惠的金额要加到商品总金额中，解决订单确认页面商品金额不对的问题
			teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(totalCutMoney));
		}
		
		return result;
	}

}
