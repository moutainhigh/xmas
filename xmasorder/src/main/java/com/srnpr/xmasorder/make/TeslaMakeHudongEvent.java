package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadHuDongEvent;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelHuDongEvent;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 互动活动价格设置
 */
public class TeslaMakeHudongEvent extends TeslaTopOrderMake {

	static LoadHuDongEvent loadHuDongEvent = new LoadHuDongEvent();
	static LoadEventInfo loadEventInfo = new LoadEventInfo();
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		// 设置互动活动编号
		setHuDongCode(teslaOrder);
		
		// 忽略非互动活动的下单请求
		if(StringUtils.isBlank(teslaOrder.getHuDongCode())) {
			return result;
		}
		
		PlusModelHuDongEvent huDongEvent = loadHuDongEvent.upInfoByCode(new PlusModelQuery(teslaOrder.getHuDongCode()));
		// 判断发布状态
		if(!"4497472700020002".equals(huDongEvent.getEventStatus())) {
			return result;
		}
		
		// 判断有效时间，忽略已经过期和还未开始的
		String nowText = FormatHelper.upDateTime();
		if (huDongEvent.getBeginTime().compareTo(nowText) > 0
				|| huDongEvent.getEndTime().compareTo(nowText) < 0) {
			return result;
		}
		
		if(StringUtils.isBlank(huDongEvent.getCxEventCode())) {
			return result;
		}
		
		PlusModelEventQuery q = new PlusModelEventQuery();
		q.setCode(huDongEvent.getCxEventCode());
		PlusModelEventInfo eventInfo = loadEventInfo.upInfoByCode(q);
		
		// 检查促销活动是否有效
		if(!new PlusSupportEvent().checkEventEnable(eventInfo)) {
			return result;
		}
		
		BigDecimal eventPrice;
		Map<String,BigDecimal> orderPayMoneyMap = new HashMap<String, BigDecimal>();
		for(TeslaModelOrderDetail orderDetail : teslaOrder.getOrderDetails()) {
			eventPrice = huDongEvent.getSkuPriceMap().get(orderDetail.getSkuCode());
			if(eventPrice == null) {
				continue;
			}
			
			// 用原价跟互动活动设置的价格进行对比
			if(orderDetail.getSellPrice().compareTo(eventPrice) <= 0) {
				continue;
			}
			
			TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
			activity.setOrderCode(orderDetail.getOrderCode());
			activity.setActivityCode(eventInfo.getEventCode());
			activity.setActivityType(eventInfo.getEventType());
			activity.setPreferentialMoney(orderDetail.getSellPrice().subtract(eventPrice));
			activity.setProductCode(orderDetail.getProductCode());
			activity.setSkuCode(orderDetail.getSkuCode());
			activity.setOutActiveCode(eventInfo.getOutActiveCode());//用此字段存sku的活动商品IC编号
			teslaOrder.getActivityList().add(activity);
			
			if(!orderPayMoneyMap.containsKey(orderDetail.getOrderCode())) {
				orderPayMoneyMap.put(orderDetail.getOrderCode(), BigDecimal.ZERO);
			}
			
			// 设置优惠金额
			orderDetail.setSaveAmt(orderDetail.getSellPrice().subtract(eventPrice));
			// 设置商品金额为原价
			orderDetail.setShowPrice(orderDetail.getSellPrice());
			// 最后再重设商品活动价
			orderDetail.setSkuPrice(eventPrice);
			
			// 按订单汇总优惠金额
			orderPayMoneyMap.put(orderDetail.getOrderCode(), orderPayMoneyMap.get(orderDetail.getOrderCode()).add(activity.getPreferentialMoney().multiply(new BigDecimal(orderDetail.getSkuNum()))));
		
			for(TeslaModelShowGoods sg : teslaOrder.getShowGoods()) {
				if(sg.getSkuCode().equals(orderDetail.getSkuCode())) {
					sg.setSkuPrice(orderDetail.getShowPrice());
				}
			}
		}
		
		if(!orderPayMoneyMap.isEmpty()) {
			// 记录优惠信息
			Set<Entry<String, BigDecimal>> entryList = orderPayMoneyMap.entrySet();
			BigDecimal totalMoney = BigDecimal.ZERO;
			for(Entry<String, BigDecimal> entry : entryList){
				TeslaModelOrderPay op = new TeslaModelOrderPay();
				op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
				op.setOrderCode(entry.getKey());
				op.setPaySequenceid("");
				op.setPayedMoney(entry.getValue());
				op.setPayType("449746280024");
				teslaOrder.getOcOrderPayList().add(op);
				
				totalMoney = totalMoney.add(entry.getValue());
			}
			
			// 优惠的金额要加到商品总金额中，解决订单确认页面商品金额不对的问题
			teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(totalMoney));
			
			// 记录抵扣的金额
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name((String)DbUp.upTable("sc_event_type").dataGet("type_name", "", new MDataMap("type_code","4497472600010031")));
			discount.setDis_type("0");
			discount.setDis_price(totalMoney.doubleValue());
			teslaOrder.getShowMoney().add(discount);
			
			// 记录当前生效的互动活动信息
			teslaOrder.setHuDongEvent(huDongEvent);
		}
		
		return result;
	}
	
	// 重新设置互动活动编号，兼容未直接传互动活动的情况
	private void setHuDongCode(TeslaXOrder teslaOrder) {
		// 惠惠农场活动
		if(StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
			MDataMap tree = DbUp.upTable("sc_huodong_farm_user_tree").one("member_code", teslaOrder.getUorderInfo().getBuyerCode(), "tree_code", teslaOrder.getTreeCode());
			if(tree != null) {
				teslaOrder.setHuDongCode(tree.get("event_code"));
			}
			return;
		}
	}
}
