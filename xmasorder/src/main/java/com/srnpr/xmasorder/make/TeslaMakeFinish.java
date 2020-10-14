package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmasorder.job.TeslaJobCreateOrder;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.modelnotice.PlusModelNoticeOrder;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webmodel.MMessage;
import com.srnpr.zapweb.websupport.MessageSupport;

/**
 * 各种通知及其他后续操作
 * 
 * @author jlin
 * 
 */
public class TeslaMakeFinish extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();

		PlusModelNoticeOrder pNoticeOrder = new PlusModelNoticeOrder();
		
		pNoticeOrder.setOrderCode(teslaOrder.getUorderInfo().getBigOrderCode());
		pNoticeOrder.setCreateTime(teslaOrder.getUorderInfo().getCreateTime());
		pNoticeOrder.setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
		// 通知订单信息持久化
		PlusHelperNotice.onOrderCreate(pNoticeOrder);
		new TeslaJobCreateOrder().exec(teslaOrder.getUorderInfo().getBigOrderCode());
		List<String> eventCodes = new ArrayList<String>();
		List<String> orderCodes = new ArrayList<String>();
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
			TeslaModelShowGoods good = teslaOrder.getShowGoods().get(i);
			if("0".equals(good.getGiftFlag())){continue;}
			if(PlusHelperEvent.checkEventItem(good.getSkuActivityCode())){
				PlusHelperNotice.onIcOrder(good.getOrderCode(), teslaOrder.getUorderInfo().getBuyerCode(), good.getSkuActivityCode(), good.getSkuNum());
			}
			eventCodes.add(good.getEventCode());
		}
		
		// 以订单明细绑定的订单号为准，解决一个商品两个订单导致取消时间漏设置问题
		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
			orderCodes.add(teslaOrder.getOrderDetails().get(i).getOrderCode());
		}
		
		PlusHelperNotice.onOrderCreateForCancelOrder(teslaOrder.getIsKaolaOrder(), orderCodes, eventCodes);
		List<ShoppingCartGoodsInfoForAdd> li = new ArrayList<ShoppingCartGoodsInfoForAdd>();
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
			if("0".equals(teslaOrder.getShowGoods().get(i).getGiftFlag())){continue;}
			ShoppingCartGoodsInfoForAdd good = new ShoppingCartGoodsInfoForAdd();
			good.setChooseFlag("1");
			good.setProduct_code(teslaOrder.getShowGoods().get(i).getProductCode());
			good.setSku_code(teslaOrder.getShowGoods().get(i).getSkuCode());
			good.setSku_num(0);
			li.add(good);
		}
		new ShopCartServiceForCache().saveShopCart(li, teslaOrder.getUorderInfo().getBuyerCode());
		
		//货到付款订单发送通知 20180327 --rhb
		if("449716200002".equals(teslaOrder.getUorderInfo().getPayType())) {
			String order_sources = bConfig("xmasorder.send_notice_order_source");
			if(order_sources.contains(teslaOrder.getUorderInfo().getOrderSource())) {//通过订单来源判断是否发送通知
				
				MDataMap parmas = new MDataMap();
				parmas.put("big_order_code", teslaOrder.getUorderInfo().getBigOrderCode());
				parmas.put("isPayed", "false");
				String str = JSON.toJSONString(parmas);
				
				//写入定时任务，定时执行发送下单成功通知
				JobExecHelper.createExecInfo("449746990005", str, null);
			}
		}
		
		/**542增加积分共享通知 -rhb 20190423 */
		String buyerCode = teslaOrder.getUorderInfo().getBuyerCode();
		String buyerMobile = teslaOrder.getUorderInfo().getBuyerMobile();
		//先查当前是否有积分共享活动
		String query = "SELECT event_code FROM sc_hudong_event_info WHERE event_status='449747270002' AND "
				+ "event_type_code='449748210003' AND begin_time<=SYSDATE() AND end_time>SYSDATE()";
		Map<String, Object> integratEvent = DbUp.upTable("sc_hudong_event_info").dataSqlOne(query, new MDataMap());
		if(null != integratEvent) {
			//查积分共享关系表
			String sSql = "SELECT invitee_code FROM mc_integral_relation WHERE inviter_code=:inviter_code AND event_code=:event_code AND is_valid='1'";
			List<Map<String, Object>> list = DbUp.upTable("mc_integral_relation").dataSqlList(sSql, new MDataMap("event_code",integratEvent.get("event_code")+"","inviter_code",buyerCode));
			if(null!=list && list.size()>0) {
				String content = "绑定好友" + buyerMobile + "下单成功，签收后您将获得48积分。";
				MessageSupport messageSupport = new MessageSupport();
				for (Map<String, Object> map : list) {
					String sendPhone = (String) DbUp.upTable("mc_login_info").dataGet("login_name", "memeber_code=:member_code", new MDataMap("member_code",map.get("invitee_code")+""));
					if(StringUtils.isNotEmpty(sendPhone)) {
						
						MMessage message = new MMessage();
						message.setMessageContent(content);
						message.setMessageReceive(sendPhone);
						message.setSendSource("4497467200020006");//写死惠家有
						messageSupport.sendMessage(message);
					}
				}
			}
		}
		
		List<TeslaModelOrderInfo> sorderInfo = teslaOrder.getSorderInfo();
		for (TeslaModelOrderInfo orderInfo : sorderInfo) {
			/**京东商品0元单创建下单任务*/
			if(Constants.SMALL_SELLER_CODE_JD.equals(orderInfo.getSmallSellerCode()) && orderInfo.getDueMoney().compareTo(BigDecimal.ZERO)==0) {
				JobExecHelper.createExecInfo("449746990017", orderInfo.getOrderCode(), null);
			}
			/**多货主商品0元旦创建下单任务*/
			if("4497471600430002".equals(orderInfo.getDeliveryStoreType()) && orderInfo.getDueMoney().compareTo(BigDecimal.ZERO)==0) {
				JobExecHelper.createExecInfo("449746990018", orderInfo.getOrderCode(), null);
			}
		}
		
		
		//特殊活动记录滚动日志
		if(StringUtils.isNotBlank(teslaOrder.getEventCode())) {
			MDataMap member = DbUp.upTable("mc_login_info").one("member_code", teslaOrder.getUorderInfo().getBuyerCode());
			String nickName = member.get("login_name").substring(0,3)+"****"+member.get("login_name").substring(7);

			DbUp.upTable("lc_special_event_log").insert("uid", WebHelper.upUuid(),"member_code",teslaOrder.getUorderInfo().getBuyerCode(),
					"nick_name",nickName,"create_time",DateUtil.getSysDateTimeString());
		}
		
		// 记录小程序渠道来源
		if(StringUtils.isNotBlank(teslaOrder.getOutChannelId())) {
			if(DbUp.upTable("fh_smallapp_channel").count("channel_id", teslaOrder.getOutChannelId()) > 0) {
				MDataMap insertMap = new MDataMap();
				insertMap.put("ou_channel_id", teslaOrder.getOutChannelId());
				insertMap.put("big_order_code", teslaOrder.getUorderInfo().getBigOrderCode());
				insertMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("fh_smallapp_order").dataInsert(insertMap);
			}
		}
		
		// 记录小程序直播间来源
		if(StringUtils.isNotBlank(teslaOrder.getLiveRoomID()) && !"0".equals(teslaOrder.getLiveRoomID())) {
			MDataMap insertMap = new MDataMap();
			insertMap.put("roomid", teslaOrder.getLiveRoomID());
			insertMap.put("big_order_code", teslaOrder.getUorderInfo().getBigOrderCode());
			insertMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("fh_liveroom_order").dataInsert(insertMap);
		}
		
		//记录订单页面来源
		if(!StringUtils.isEmpty(teslaOrder.getOrderPageSouce())) {
			for(TeslaModelOrderInfo soi : teslaOrder.getSorderInfo()) {
				MDataMap insertMap = new MDataMap();
				insertMap.put("user_code", soi.getBuyerCode());
				insertMap.put("order_code", soi.getOrderCode());
				insertMap.put("channel_id", teslaOrder.getChannelId());
				insertMap.put("source_type", teslaOrder.getOrderPageSouce());
				insertMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("fh_pv_uv_order").dataInsert(insertMap);
			}
		}
		
		// 插入定时任务
		for(Object key : teslaOrder.getExecInfoMap().keySet()) {
			@SuppressWarnings("rawtypes")
			Collection vals = teslaOrder.getExecInfoMap().getCollection(key);
			for(Object val : vals) {
				JobExecHelper.createExecInfo(key+"", val+"", null);
			}
		}

		// 如果是小程序订单则需要走一下外呼衍生单逻辑判断
		if("449715190025".equals(teslaOrder.getUorderInfo().getOrderSource())) {
			JobExecHelper.createExecInfo("449746990045", teslaOrder.getUorderInfo().getBigOrderCode(), null);
		}

		return xResult;
	}
	
}
