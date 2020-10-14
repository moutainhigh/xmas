package com.srnpr.xmaspay.service;

import java.math.BigDecimal;
import java.util.List;

import com.srnpr.zapcom.basemodel.MDataMap;

public interface IOrderPayService {
	
	/**
	 * 根据订单编号查询支付宝支付信息
	 * @param orderCode
	 * 		订单编号
	 * @return 支付宝支付信息
	 */
	public MDataMap getOcPaymentInfo(String orderCode);
	
	/**
	 * 根据订单编号查询订单信息
	 * @param orderCode
	 * 		订单编号
	 * @return 订单信息
	 */
	public MDataMap getOrderInfo(String orderCode);
	
	/**
	 *根据订单编号 获取订单信息
	 * @param orderCode
	 * 		订单编号
	 * @return 订单信息
	 */
	public MDataMap getOrderInfoUpper(String orderCode);
	
	/**
	 * 获取订单地址信息
	 * @param orderCode
	 * 		订单编号
	 * @return 订单地址信息
	 */
	public MDataMap getOrderAddressInfo(String orderCode);
	
	/**
	 * 保存支付信息
	 * @param mDataMap
	 */
	public void saveOcpaymentInfo(MDataMap mDataMap);
	
	/**
	 * 根据订单编号获取订单信息
	 * @param orderCode
	 * 		订单编号
	 * @return 订单信息
	 */
	public MDataMap upOrderInfo(String orderCode);
	
	/**
	 * save订单支付信息
	 * @param mDataMap
	 * 		订单支付信息
	 */
	public void saveOrderPayInfo(MDataMap mDataMap);
	
	/**
	 * 更新订单信息已付机内
	 * @param orderCode
	 * 		订单编号
	 * @param payMoney
	 * 		支付金额
	 */
	public void updatePayedMoney(String orderCode, BigDecimal payMoney);
	
	/**
	 * 更新订单信息
	 * @param mDataMap
	 * 		订单信息
	 */
	public void updateOrderInfo(MDataMap mDataMap);
	
	/**
	 * 更新大订单信息
	 * @param mDataMap
	 * 		订单信息
	 */
	public void updateOrderInfoUpper(MDataMap mDataMap);
	
	/**
	 * 创建支付同步信息
	 * @param orderCode
	 * 		订单编号
	 */
	public void createExecPayInfo(String orderCode);
	
	/**
	 * 创建支付单号报关任务信息
	 * @param payOrderCode
	 * 		支付的订单编号，通常是OS开头的大订单号
	 */
	public void createExecOrderCustoms(String payOrderCode);
	
	/**
	 * 根据订单编号查询订单信息集合
	 * @param orderCode
	 * 		订单编号
	 * @return 订单信息集合
	 */
	public List<MDataMap> getOrderInfoList(String orderCode);	
	
	/**
	 * 根据订单编号获取交易流水编号
	 * @param orderCode
	 * 		订单编号
	 * @return 支付宝交易流水编号
	 */
	public String getAlipayTradeNO(String orderCode);
	
	/**
	 * 获取订单有效时间时间
	 * @param orderCode
	 * 		订单编号
	 * @return 交易取消的时间（单位分钟）
	 */
	public int getOrderValidTime(String orderCode);
	
	/**
	 * 获取支付流水编号
	 * @param orderCode
	 * 		订单编号
	 * @return 支付流水号
	 */
	public String getPaySequenceNO(String orderCode);
	
	/**
	 * 根据大订单号查询用支付宝的支付成功信息
	 * @param bigOrderCode
	 * 		大订单号
	 * @return
	 */
	public MDataMap getAlipayPaymentInfo(String bigOrderCode);
	
	/**
	 * 根据大订单号查询用微信支付的支付成功信息
	 * @param bigOrderCode
	 * 		大订单号
	 * @return
	 */
	public MDataMap getWechatPaymentInfo(String bigOrderCode);
	
	/**
	 * 根据用户编码查询用户基本信息
	 * @param memberCode
	 * 		用户编码
	 * @return
	 */
	public MDataMap getMemberInfo(String memberCode);
}
