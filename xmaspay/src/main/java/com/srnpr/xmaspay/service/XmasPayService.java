package com.srnpr.xmaspay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class XmasPayService {

	/**
	 * 获取大订单信息
	 * @param bigOrderInfo
	 * @return
	 */
	public MDataMap getOrderInfoSupper(String bigOrderCode){
		List<Map<String,Object>> list = DbUp.upTable("oc_orderinfo_upper").upTemplate().queryForList("select * from oc_orderinfo_upper where big_order_code = :big_order_code", new MDataMap("big_order_code",bigOrderCode));
		return list.isEmpty() ? null : new MDataMap(list.get(0));
	}
	
	/**
	 * 更新表oc_orderinfo_upper的已支付金额、支付类型、更新时间
	 * @param orderInfoSupper
	 */
	public void updateOrderSupperPayedMoney(MDataMap orderInfoSupper){
		DbUp.upTable("oc_orderinfo_upper").dataUpdate(orderInfoSupper, "payed_money,pay_type,update_time", "big_order_code");
	}
	
	/**
	 * 更新表oc_orderinfo的已支付金额
	 * @param orderInfoSupper
	 */
	public void updateOrderInfoPayedMoney(MDataMap mDataMap){
		DbUp.upTable("oc_orderinfo").dataUpdate(mDataMap, "payed_money,update_time", "order_code");
	}
	
	/**
	 * 获取小订单信息列表
	 * @param bigOrderCode
	 * @return
	 */
	public List<MDataMap> getOrderInfoList(String bigOrderCode){
		List<Map<String,Object>> list = DbUp.upTable("oc_orderinfo").upTemplate().queryForList("select * from oc_orderinfo where big_order_code = :big_order_code", new MDataMap("big_order_code",bigOrderCode));
		
		List<MDataMap> itemList = new ArrayList<MDataMap>();
		for(Map<String,Object> map : list){
			itemList.add(new MDataMap(map));
		}
		return itemList;
	}
	
	/**
	 * 查询订单的收货地址
	 * @param orderCode
	 * @return
	 */
	public MDataMap getOrderAddress(String orderCode){
		List<Map<String,Object>> list = DbUp.upTable("oc_orderadress").upTemplate().queryForList("select * from oc_orderadress where order_code = :order_code", new MDataMap("order_code",orderCode));
		return list.isEmpty() ? null : new MDataMap(list.get(0));
	}
	
	/**
	 * 获取订单的支付信息
	 * @param bigOrderCode 大订单号
	 */
	public MDataMap getOrderInfoSupperPayment(String bigOrderCode){
		List<Map<String,Object>> list = DbUp.upTable("oc_orderinfo_upper_payment").upTemplate().queryForList("select * from oc_orderinfo_upper_payment where big_order_code = :big_order_code", new MDataMap("big_order_code",bigOrderCode));
		return list.isEmpty() ? null : new MDataMap(list.get(0));
	}
	
	/**
	 * 获取大订单的支付信息
	 * @param bigOrderCode 大订单号
	 * @param payType 支付类型
	 */
	public MDataMap getOrderInfoUpperPayment(String bigOrderCode, String payType){
		List<Map<String,Object>> list = DbUp.upTable("oc_orderinfo_upper_payment").upTemplate().queryForList("select * from oc_orderinfo_upper_payment where big_order_code = :big_order_code and pay_type = :pay_type", new MDataMap("big_order_code",bigOrderCode,"pay_type", payType));
		return list.isEmpty() ? null : new MDataMap(list.get(0));
	}
	
	/**
	 * 保存订单的支付信息
	 * @param mDataMap 大订单信息
	 * @return
	 */
	public MDataMap saveOrderInfoUpperPayment(MDataMap mDataMap){
		DbUp.upTable("oc_orderinfo_upper_payment").dataInsert(mDataMap);
		return mDataMap;
	}
	
	/**
	 * 保存支付网关的支付信息
	 * @param mDataMap
	 */
	public void savePayGatePayment(MDataMap mDataMap){
		MDataMap whereMDataMap = new MDataMap();
		whereMDataMap.put("c_order", mDataMap.get("c_order"));
		List<Map<String,Object>> orderList = DbUp.upTable("oc_payment_paygate").upTemplate().queryForList("select zid from oc_payment_paygate where c_order = :c_order", whereMDataMap);
		if(orderList.isEmpty()){
			DbUp.upTable("oc_payment_paygate").dataInsert(mDataMap);
		}
	}
	
	/**
	 * 保存苹果支付的支付信息
	 * @param mDataMap
	 */
	public void saveApplePayment(MDataMap mDataMap){
		MDataMap whereMDataMap = new MDataMap();
		whereMDataMap.put("no_order", mDataMap.get("no_order"));
		List<Map<String,Object>> orderList = DbUp.upTable("oc_payment_applepay").upTemplate().queryForList("select zid from oc_payment_applepay where no_order = :no_order", whereMDataMap);
		if(orderList.isEmpty()){
			DbUp.upTable("oc_payment_applepay").dataInsert(mDataMap);
		}
	}
	
	// 支付宝交易信息
	public MDataMap saveOrderPayment(MDataMap payment){
		DbUp.upTable("oc_payment").dataInsert(payment);
		return payment;
	}
	
	// 微信交易信息
	public MDataMap saveOrderPaymentWechat(MDataMap payment){
		DbUp.upTable("oc_payment_wechatNew").dataInsert(payment);
		return payment;
	}
	
	/**
	 * 查询小订单的支付信息
	 * @param orderCode
	 * @param payType
	 * @return
	 */
	public MDataMap getOrderPay(String orderCode, String payType){
		List<Map<String,Object>> list = DbUp.upTable("oc_order_pay").upTemplate().queryForList("select * from oc_order_pay where order_code = :order_code and pay_type = :pay_type", new MDataMap("order_code",orderCode,"pay_type", payType));
		return list.isEmpty() ? null : new MDataMap(list.get(0));
	}
	
	/**
	 * 保存小订单支付信息
	 * @param mDataMap
	 */
	public void saveOrderPay(MDataMap mDataMap){
		DbUp.upTable("oc_order_pay").dataInsert(mDataMap);
	}
	
	/**
	 * 查询用户信息
	 * @param memberCode
	 * @return
	 */
	public MDataMap getMemberInfo(String memberCode){
		return DbUp.upTable("mc_member_info").one("member_code", memberCode);
	}
	
	/**
	 * 保存支付处理日志
	 * @param mDataMap
	 */
	public void saveXmasPayLog(MDataMap mDataMap){
		DbUp.upTable("lc_payprocess_log").dataInsert(mDataMap);
	}
	
}
