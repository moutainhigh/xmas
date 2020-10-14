package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelJdOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderAddress;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderDetailForThird;
import com.srnpr.xmasorder.model.TeslaModelOrderExtras;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.websupport.DataMapSupport;

/**
 * 订单处理
 * @author jlin
 *
 */
public class TeslaOrderService {

	/**
	 * 缓存订单信息持久化到数据库
	 * @param orderCode OS 订单号
	 */
	public boolean orderSaveToDB (String orderCode) {
		
		TeslaXOrder teslaXOrder = getXOrderInCache(orderCode);
		
		DataMapSupport dataMapSupport = new DataMapSupport();
		
		try {
			//持久化  oc_orderinfo_upper
			TeslaModelOrderInfoUpper orderInfoUpper = teslaXOrder.getUorderInfo();
			dataMapSupport.saveToDb("oc_orderinfo_upper", orderInfoUpper,orderInfoUpper.getBigOrderCode());
			
			List<String> duohuozhuOrder = new ArrayList<String>();
			//持久化  oc_orderinfo  oc_orderadress
			for (TeslaModelOrderInfo orderInfo : teslaXOrder.getSorderInfo()) {
				
				dataMapSupport.saveToDb("oc_orderinfo", orderInfo,orderInfo.getOrderCode());
				
				//持久化地址信息
				TeslaModelOrderAddress orderAddress = teslaXOrder.getAddress();
				orderAddress.setUid(WebHelper.upUuid());
				orderAddress.setOrderCode(orderInfo.getOrderCode());
				dataMapSupport.saveToDb("oc_orderadress", orderAddress,orderAddress.getOrderCode());
				
				//添加日志信息
				DbUp.upTable("lc_orderstatus").dataInsert(new MDataMap("code",orderInfo.getOrderCode(),"info","订单创建","create_time",orderInfo.getCreateTime(),"create_user",orderInfo.getBuyerCode(),"now_status",orderInfo.getOrderStatus()));
				
				//多货主订单
				if("4497471600430002".equals(orderInfo.getDeliveryStoreType())){
					//持久化oc_order_duohz
					DbUp.upTable("oc_order_duohz").insert("order_code",orderInfo.getOrderCode(),"small_seller_code",orderInfo.getSmallSellerCode(),"create_time",DateUtil.getSysDateTimeString());
					duohuozhuOrder.add(orderInfo.getOrderCode());
				}
			}
			
			//持久化  oc_orderdetail
			int seq = 0;
			for (TeslaModelOrderDetail orderDetail : teslaXOrder.getOrderDetails()) {
				dataMapSupport.saveToDb("oc_orderdetail", orderDetail,orderDetail.getOrderCode());
				//持久化多货主订单详情表
				if(duohuozhuOrder.contains(orderDetail.getOrderCode())) {
					for(int i = 0; i< orderDetail.getSkuNum(); i ++) {
						DbUp.upTable("oc_order_duohz_detail").insert("order_code",orderDetail.getOrderCode(),"sku_code",orderDetail.getSkuCode(),"seq",String.valueOf(++seq));
					}
				}
			}
			
			//持久化oc_third_orderdetail -rhb 20180808
			for(TeslaModelOrderDetailForThird orderDetail : teslaXOrder.getThirdDetail()) {
				dataMapSupport.saveToDb("oc_third_orderdetail", orderDetail,orderDetail.getOrderCode());
			}
			
			Map<String, BigDecimal> couponCodeMap = new HashMap<String, BigDecimal>(); // key:coupon_code;value:money
			//持久化  oc_order_pay
			for (TeslaModelOrderPay orderPay : teslaXOrder.getOcOrderPayList()) {
				dataMapSupport.saveToDb("oc_order_pay", orderPay,orderPay.getOrderCode());
				
				if ("449746280002".equals(orderPay.getPayType())) {
					if (couponCodeMap.containsKey(orderPay.getPaySequenceid())) {
						couponCodeMap.put(
								orderPay.getPaySequenceid(),
								couponCodeMap.get(orderPay.getPaySequenceid()).add(
										orderPay.getPayedMoney()));
					} else {
						couponCodeMap.put(orderPay.getPaySequenceid(),
								orderPay.getPayedMoney());
					}
				}
			}
			
			//持久化  oc_order_activity
			for (TeslaModelOrderActivity orderActivity : teslaXOrder.getActivityList()) {
				dataMapSupport.saveToDb("oc_order_activity", orderActivity,orderActivity.getOrderCode());
			}
			
			//此处添加 定时通知信息
			List<TeslaModelOrderInfo> sorderList = teslaXOrder.getSorderInfo();
			for (TeslaModelOrderInfo teslaModelOrderInfo : sorderList) {
				String small_seller_code = teslaModelOrderInfo.getSmallSellerCode();
				String order_code = teslaModelOrderInfo.getOrderCode();
				if ("SI2003".equals(small_seller_code) ) {
					JobExecHelper.createExecInfo("449746990002", order_code, null);
				} else if ("SF03KJT".equals(small_seller_code)) {
					// 判断0元订单
					if (teslaModelOrderInfo.getDueMoney().compareTo(BigDecimal.ZERO) <= 0||"4497153900010002".equals(teslaModelOrderInfo.getOrderStatus())) {
//						JobExecHelper.createExecInfo("449746990003", order_code, null);
						JobExecHelper.createExecInfoForWebcore("449746990003", order_code , "" , "TeslaOrderService line 95");
					}
				}
			}
			
			//持久化 订单附加信息
			TeslaModelOrderExtras teslaModelOrderExtras = teslaXOrder.getExtras();
			dataMapSupport.saveToDb("lc_client_info", teslaModelOrderExtras,teslaModelOrderExtras.getOrderCode());
			
			//持久 oc_order_jd
			for (TeslaModelJdOrderInfo orderInfo : teslaXOrder.getJdOrderInfo()) {
				dataMapSupport.saveToDb("oc_order_jd", orderInfo,orderInfo.getOrderCode());
			}
			
			//优惠券扣减 -rhb 20181119
			for (String couponCode : couponCodeMap.keySet()) {// 优惠券扣减
				MDataMap updateMap = new MDataMap();
				updateMap.put("coupon_code", couponCode);
				updateMap.put("status", "1");
				updateMap.put("surplus_money", couponCodeMap.get(couponCode).toString());
				updateMap.put("update_time", DateHelper.upNow());
				updateMap.put("last_mdf_id", "app");
				
				Map<String, Object> map = DbUp.upTable("oc_coupon_info").dataSqlOne("select ct.money_type,ci.end_time from oc_coupon_type ct,oc_coupon_info ci where ci.coupon_type_code = ct.coupon_type_code and ci.coupon_code = :couponCode", new MDataMap("couponCode",couponCode));
				String sSql = "update oc_coupon_info set last_mdf_id=:last_mdf_id,status=:status,update_time=:update_time,surplus_money=(case when surplus_money-:surplus_money < 0 then 0 else surplus_money-:surplus_money end  ) where coupon_code=:coupon_code";
				if(map != null && "449748120002".equals(map.get("money_type"))){
					// 折扣券特殊处理，剩余金额值固定不变
					sSql = "update oc_coupon_info set last_mdf_id=:last_mdf_id,status=:status,update_time=:update_time where coupon_code=:coupon_code";
				}
				
				DbUp.upTable("oc_coupon_info").dataExec(sSql, updateMap);
				
				//5.5.8增加优惠券使用记录缓存
				String endTime = MapUtils.getString(map, "end_time", "");
				int seconds = getSeconds(endTime);
				XmasKv.upFactory(EKvSchema.CouponUse).set(couponCode, "1");
				XmasKv.upFactory(EKvSchema.CouponUse).expire(couponCode, seconds);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取指定时间与当前时间的差值 
	 * @param endTime
	 * @return 差值 单位：秒
	 */
	private int getSeconds(String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int seconds = 0;
		try {
			Date endDate = sdf.parse(endTime);
			long time1 = new Date().getTime();
			long time2 = endDate.getTime();
			seconds = new Long(((time2 - time1) / 1000)).intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return seconds;
	}

	/***
	 * 获取缓存的订单信息
	 * @param orderCode OS 订单号
	 * @return
	 */
	public TeslaXOrder getXOrderInCache(String orderCode){
		
		String orderStr=XmasKv.upFactory(EKvSchema.CreateOrder).get(orderCode);
		if(StringUtils.isBlank(orderStr)){
			return null;
		}
		
		return new GsonHelper().fromJson(orderStr, new TeslaXOrder());
	}
	
}
