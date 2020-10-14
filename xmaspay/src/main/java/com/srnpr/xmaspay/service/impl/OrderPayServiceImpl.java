package com.srnpr.xmaspay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.SellerCodeEnum;
import com.srnpr.xmaspay.common.TradeStatusEnum;
import com.srnpr.xmaspay.service.IOrderPayService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;

/**
 * 订单支付相关业务处理
 * @author pang_jhui
 *
 */
public class OrderPayServiceImpl implements IOrderPayService {

	@Override
	public MDataMap getOcPaymentInfo(String orderCode) {
		
		return DbUp.upTable("oc_payment").one("out_trade_no",orderCode);
		
	}

	@Override
	public MDataMap getOrderInfo(String orderCode) {
		
		return DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		
	}
	

	@Override
	public MDataMap getOrderInfoUpper(String orderCode) {
		
		return DbUp.upTable("oc_orderinfo_upper").one("big_order_code",orderCode);
		
	}
	
	@Override
	public void updateOrderInfo(MDataMap mDataMap) {
		
		DbUp.upTable("oc_orderinfo").update(mDataMap);
		
	}

	@Override
	public void updateOrderInfoUpper(MDataMap mDataMap) {
		
		DbUp.upTable("oc_orderinfo_upper").update(mDataMap);
		
	}
	
	
	public MDataMap getOrderAddressInfo(String orderCode){
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_OS)){
			
			MDataMap mDataMap = DbUp.upTable("oc_orderinfo").oneWhere("order_code", "", "", "big_order_code",orderCode);
			
			if(mDataMap != null){
				
				orderCode = mDataMap.get("order_code");
				
			}
			
		}
		
		return DbUp.upTable("oc_orderadress").one("order_code",orderCode);
		
	}

	@Override
	public void saveOcpaymentInfo(MDataMap mDataMap) {
		
		DbUp.upTable("oc_payment").dataInsert(mDataMap);
		
	}

	@Override
	public MDataMap upOrderInfo(String orderCode) {
		
		MDataMap mDataMap = null;
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_DD)){
			
			mDataMap = getOrderInfo(orderCode);
			
		}
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_OS)){
			
			mDataMap = getOrderInfoUpper(orderCode);
			
		}
		
		return mDataMap;
		
	}

	@Override
	public void saveOrderPayInfo(MDataMap mDataMap) {

		String payType = mDataMap.get("pay_type");
		if(StringUtils.isBlank(payType)){
			payType = Constants.ORDER_PAY_TYPE_ALIPAY;
		}
		
		MDataMap orderPayMap = DbUp.upTable("oc_order_pay").one("order_code", mDataMap.get("order_code"), "pay_type", payType);
		
		if(orderPayMap == null){
			
			DbUp.upTable("oc_order_pay").dataInsert(mDataMap);
			
		}else{
			
			mDataMap.put("zid", orderPayMap.get("zid"));
			
			mDataMap.put("uid", orderPayMap.get("uid"));
			
			DbUp.upTable("oc_order_pay").update(mDataMap);
			
		}

	}

	@Override
	public void updatePayedMoney(String orderCode, BigDecimal payMoney) {
		
		BigDecimal payedMoney = BigDecimal.ZERO;
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_DD)){
			
			MDataMap orderInfo = getOrderInfo(orderCode);
			
			String payedMoneyStr = orderInfo.get("payed_money");
			
			payedMoney = new BigDecimal(payedMoneyStr);
			
			payedMoney = payedMoney.add(payMoney);
			
			orderInfo.put("payed_money", payedMoney.toString());
			
			updateOrderInfo(orderInfo);
			
		}
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_OS)){
			
			MDataMap orderInfoUpper = getOrderInfoUpper(orderCode);
			
			String payedMoneyStr = orderInfoUpper.get("payed_money");
			
			payedMoney = new BigDecimal(payedMoneyStr);
			
			payedMoney = payedMoney.add(payMoney);
			
			orderInfoUpper.put("payed_money", payedMoney.toString());
			
			updateOrderInfoUpper(orderInfoUpper);
			
		}
		
		
	}

	@Override
	public void createExecPayInfo(String orderCode) {
		List<MDataMap> orderInfoList = getOrderInfoList(orderCode);
		
		if(orderInfoList != null){
			for (MDataMap mDataMap : orderInfoList) {
				String smallSellerCode = mDataMap.get("small_seller_code");
				/*同步支付完成跨境通订单信息*/
				if (StringUtils.startsWith(smallSellerCode, "SF03KJT")) {
//					JobExecHelper.createExecInfo(Constants.ZA_EXEC_TYPE_SYNC_KJT, mDataMap.get("order_code"), "");
					JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, mDataMap.get("order_code"), "" , "OrderPayServiceImpl line 189");
				}
				
				/*将惠家有、家有汇的支付信息同步到ld*/
				if (StringUtils.equals(smallSellerCode, SellerCodeEnum.FAMILYHAS.getSellerCode()) || StringUtils.equals(smallSellerCode, SellerCodeEnum.HPOOL.getSellerCode())) {
					JobExecHelper.createExecInfo(Constants.ZA_EXEC_TYPE_SYNC_PAYINFO_LD, mDataMap.get("order_code"), "");
				}

			}
			
		}
		
		
	}

	@Override
	public List<MDataMap> getOrderInfoList(String orderCode) {
		
		List<MDataMap> orderInfoList = null;
		
		if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_OS)){
			
			orderInfoList = DbUp.upTable("oc_orderinfo").queryByWhere("big_order_code",orderCode);
			
		}else if(StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_DD)){
			
			orderInfoList = DbUp.upTable("oc_orderinfo").queryByWhere("order_code",orderCode);
			
		}
		
		return orderInfoList;
	}

	@Override
	public String getAlipayTradeNO(String orderCode) {
		
		String tradeNO = "";
		
		if (StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_DD)) {

			MDataMap mDataMap = getOcPaymentInfo(orderCode);
			
			if(mDataMap != null){
				
				tradeNO = mDataMap.get("trade_no");
				
			}else{
				
				MDataMap orderMap = getOrderInfo(orderCode);
				
				if(orderMap != null){
					
					String big_order_code = orderMap.get("big_order_code");
					
					mDataMap = getOcPaymentInfo(big_order_code);
					
					if(mDataMap != null){
						
						tradeNO = mDataMap.get("trade_no");
						
					}
					
					
				}
				
			}

		}

		if (StringUtils.startsWith(orderCode, Constants.ORDER_START_WITH_OS)) {

			MDataMap mDataMap = getOcPaymentInfo(orderCode);
			
			if(mDataMap != null){
				
				tradeNO = mDataMap.get("trade_no");
				
			}

		}
		
		return tradeNO;
	}
	
	
	@Override
	public int getOrderValidTime(String orderCode) {

		int time = 0;

		List<MDataMap> ics = DbUp.upTable("oc_order_activity").queryAll("activity_code,out_active_code", "",
				" activity_code like 'CX%' and order_code in (select order_code from oc_orderinfo where big_order_code=:order_code)",
				new MDataMap("order_code", orderCode));

		if (ics != null && !ics.isEmpty()) {
			List<String> cxCodes = new ArrayList<String>();
			for (int jj = 0; jj < ics.size(); jj++) {
				cxCodes.add(ics.get(jj).get("activity_code"));
			}

			String value = new PlusSupportProduct().getQrCodeAging(cxCodes);

			if (StringUtils.isNotEmpty(value)) {
				String type = value.split("&")[0];
				String va = value.split("&")[1];
				int temp = Integer.parseInt(va);
				if ("449747280001".equals(type)) {
					time = temp * 60;
				} else if ("449747280002".equals(type)) {
					time = temp;
				} else {
					time = temp / 60;
				}
			}

		}

		return time;

	}

	@Override
	public String getPaySequenceNO(String orderCode) {
		
		String trade_no = "";
		
		MDataMap paymentInfo = DbUp.upTable("oc_payment").oneWhere("trade_no", "", "", "out_trade_no",orderCode,"mark","449746280005");
		
		if(paymentInfo != null){
			
			trade_no = paymentInfo.get("trade_no");
			
		}else{
			
			MDataMap wechatPayInfo = DbUp.upTable("oc_payment_wechatNew").oneWhere("transaction_id", "", "", "out_trade_no",orderCode);
			
			if(wechatPayInfo != null){
				
				trade_no = wechatPayInfo.get("transaction_id");
				
			}
			
		}
		
		return trade_no;
		
	}

	@Override
	public MDataMap getAlipayPaymentInfo(String bigOrderCode) {
		return DbUp.upTable("oc_payment").one("out_trade_no",bigOrderCode,"flag_success",TradeStatusEnum.TRADE_SUCCESS.getFlag_success());
	}

	@Override
	public MDataMap getWechatPaymentInfo(String bigOrderCode) {
		return DbUp.upTable("oc_payment_wechatNew").one("out_trade_no",bigOrderCode,"flag_success",TradeStatusEnum.TRADE_SUCCESS.getFlag_success());
	}
	
	@Override
	public MDataMap getMemberInfo(String memberCode) {
		return DbUp.upTable("mc_member_info").one("member_code", memberCode);
	}

	@Override
	public void createExecOrderCustoms(String payOrderCode) {
		// TODO 待报关订单信息录入
		/*
		List<MDataMap> orderDataList = new ArrayList<MDataMap>();
		if(StringUtils.startsWith(payOrderCode, Constants.ORDER_START_WITH_OS)){
			orderDataList = DbUp.upTable("oc_orderinfo").queryByWhere("big_order_code",payOrderCode);
		}else if(StringUtils.startsWith(payOrderCode, Constants.ORDER_START_WITH_DD)){
			orderDataList.add(DbUp.upTable("oc_orderinfo").one("order_code",payOrderCode));
		}
		
		MerchantCustomsConfig config = PayConfigFactory.getInstance().getPayConfig(MerchantCustomsConfig.class);
		
		for(MDataMap dataMap : orderDataList){
		    // 只录入启用了报关的商户
			if(config.getCustomsEnabled(dataMap.get("small_seller_code"))){
				JobExecHelper.createExecInfo(Constants.ZA_EXEC_TYPE_SYNC_CUSTOMS, dataMap.get("order_code"), "");
			}
		}
		*/
	}

}
