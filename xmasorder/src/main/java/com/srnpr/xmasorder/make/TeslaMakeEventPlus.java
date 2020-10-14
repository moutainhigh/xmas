package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.PlusServiceEventPlus;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 橙意卡专享活动优惠
 */
public class TeslaMakeEventPlus extends TeslaTopOrderMake {

	PlusServiceEventPlus plusServiceEventPlus = new PlusServiceEventPlus();
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		//兑换码兑换、惠惠农场不参与促销
		if(StringUtils.isNotEmpty(teslaOrder.getActivityCode()) && StringUtils.isNotEmpty(teslaOrder.getRedeemCode()) 
				|| StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
			return result;
		}
		
		// 互动活动不跟其他促销叠加
		if(teslaOrder.getHuDongEvent() != null) {
			return result;
		}
		
		// 导入订单不参与促销
		String orderTypeStr = WebHelper.getImportOrderSource()+"";
		List<String> orderTypeList = Arrays.asList(orderTypeStr.split(","));
		if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getOrderType()) 
				&& orderTypeList.contains(teslaOrder.getUorderInfo().getOrderType())) {
			return result; 
		}
		
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(teslaOrder.getUorderInfo().getBuyerCode()));
		if(StringUtils.isBlank(levelInfo.getPlusEndDate())) {
			return result; 
		}
		
		Date plusEndDate = null;
		try {
			plusEndDate = DateUtils.parseDate(levelInfo.getPlusEndDate(), new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
		} catch (Exception e) {
			return result; 
		}
		
		Date now = new Date();
		
		// 已过期则不参与活动
		if(!DateUtils.isSameDay(plusEndDate, now) && plusEndDate.before(now)) {
			return result; 
		}
		
		// 提取已经参与的活动
		Map<String,List<String>> eventTypeListMap = new HashMap<String, List<String>>();
		for(TeslaModelOrderActivity oa : teslaOrder.getActivityList()) {
			if(StringUtils.isBlank(oa.getActivityType()) || StringUtils.isBlank(oa.getProductCode())) {
				continue;
			}
			
			if(!eventTypeListMap.containsKey(oa.getSkuCode())) {
				eventTypeListMap.put(oa.getOrderCode()+oa.getSkuCode(), new ArrayList<String>());
			}
			
			eventTypeListMap.get(oa.getOrderCode()+oa.getSkuCode()).add(oa.getActivityType());
		}
		
		PlusModelEventInfoPlus eventInfoPlus =  plusServiceEventPlus.getEventInfoPlus();
		if(eventInfoPlus == null) {
			return result;
		}
		
		Map<String,BigDecimal> plusMoneyMap = new HashMap<String, BigDecimal>();
		
		BigDecimal totalPlusMoney = BigDecimal.ZERO;
		// 计算优惠金额
		for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
			if(!"1".equals(detail.getGiftFlag())) {
				continue;
			}
			
			if(!"".equals(detail.getIntegralDetailId())) {
				continue;
			}
			
			// 分销商品不参与
			if(detail.getFxFlag() == 1) {
				continue;
			}
			
			// 特定橙意卡商品编码不参与活动
			if(detail.getProductCode().equals(bConfig("xmassystem.plus_product_code"))) {
				continue;
			}
			
			// 检查商品以及SKU已参与的活动判断是否可以参与橙意卡专享活动
			if(!plusServiceEventPlus.checkEventLimit(eventInfoPlus, detail.getProductCode(), eventTypeListMap.get(detail.getOrderCode()+detail.getSkuCode()))) {
				continue;
			}
			
			// 计算一下打折优惠的金额
			BigDecimal plusMoney = detail.getSkuPrice().multiply(new BigDecimal(1).subtract(eventInfoPlus.getPrice())).setScale(0, BigDecimal.ROUND_HALF_UP);
			// 计算出的优惠金额不能超过商品售价
			if(plusMoney.compareTo(detail.getSkuPrice()) > 0) {
				plusMoney = detail.getSkuPrice();
			}
			// 更新售价，减去优惠的金额
			detail.setSkuPrice(detail.getSkuPrice().subtract(plusMoney));
			detail.setSaveAmt(detail.getSaveAmt().add(plusMoney));
			
			// 记录参与的活动
			if(plusMoney.compareTo(BigDecimal.ZERO) > 0) {
				TeslaModelOrderActivity activity = new TeslaModelOrderActivity();
				activity.setOrderCode(detail.getOrderCode());
				activity.setActivityCode(eventInfoPlus.getEventCode());
				activity.setActivityName(eventInfoPlus.getShowName());
				activity.setActivityType(eventInfoPlus.getEventType());
				activity.setPreferentialMoney(plusMoney);
				activity.setProductCode(detail.getProductCode());
				activity.setSkuCode(detail.getSkuCode());
				activity.setOutActiveCode(eventInfoPlus.getOutActiveCode());
				teslaOrder.getActivityList().add(activity);
				
				// 累加总优惠金额
				totalPlusMoney = totalPlusMoney.add(plusMoney.multiply(new BigDecimal(detail.getSkuNum())));
				
				// 按小订单汇总
				if(!plusMoneyMap.containsKey(detail.getOrderCode())) {
					plusMoneyMap.put(detail.getOrderCode(), plusMoney.multiply(new BigDecimal(detail.getSkuNum())));
				} else {
					plusMoneyMap.put(detail.getOrderCode(), plusMoney.multiply(new BigDecimal(detail.getSkuNum())).add(plusMoneyMap.get(detail.getOrderCode())));
				}
			}
		}
		
		if(totalPlusMoney.compareTo(BigDecimal.ZERO) > 0) {
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmassystem.plus_order_pay_name"));
			discount.setDis_type("0");
			discount.setDis_price(totalPlusMoney.doubleValue());
			teslaOrder.getShowMoney().add(discount);
			teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(totalPlusMoney));
		
			// 分别记录到小订单上
			for(Entry<String, BigDecimal> entry : plusMoneyMap.entrySet()) {
				TeslaModelOrderPay op = new TeslaModelOrderPay();
				op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
				op.setOrderCode(entry.getKey());
				op.setPayedMoney(entry.getValue());
				op.setPayType("449746280022");
				teslaOrder.getOcOrderPayList().add(op);
			}
		}
		
		return result;
	}
}
