package com.srnpr.xmaspay.process.notify;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmaspay.config.OrderConfig;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.util.ApplePayUtils;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关的支付完成通知处理
 * @author zhaojunling
 */
public class ApplePayNotifyPayProcess extends NotifyPayProcess<ApplePayNotifyPayProcess.PaymentInput, ApplePayNotifyPayProcess.PaymentResult>{

	@Override
	protected boolean verifyInput(PaymentInput input, PaymentResult result) {
		if(input.dataParam == null) input.dataParam = new HashMap<String, String>();
		if(!ApplePayUtils.verifySign(input.dataParam, XmasPayConfig.getApplePayKeyMd5())){
			result.setResultCode(0);
			result.setResultMessage("签名异常");
			return false;
		}
		
		return true;
	}

	@Override
	protected NotifyPay getNotifyPay(PaymentInput input) {
		NotifyPay item = new NotifyPay();
		item.success = "SUCCESS".equalsIgnoreCase(input.dataParam.get("result_pay"));
		item.bigOrderCode = input.dataParam.get("no_order");
		item.orderPayType = OrderConfig.PAY_TYPE_13;
		item.tradeNo = input.dataParam.get("oid_paybill");
		item.payedMoney = input.dataParam.get("money_order");
		return item;
	}
	
	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
	
	@Override
	protected String getFromType() {
		return "lianlianpay";
	}
	
	@Override
	protected void savePayment(PaymentInput input, NotifyPay notify) {
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("oid_partner", StringUtils.trimToEmpty(input.dataParam.get("oid_partner")));
		mDataMap.put("sign_type", StringUtils.trimToEmpty(input.dataParam.get("sign_type")));
		mDataMap.put("sign", StringUtils.trimToEmpty(input.dataParam.get("sign")));
		mDataMap.put("dt_order", StringUtils.trimToEmpty(input.dataParam.get("dt_order")));
		mDataMap.put("no_order", StringUtils.trimToEmpty(input.dataParam.get("no_order")));
		mDataMap.put("oid_paybill", StringUtils.trimToEmpty(input.dataParam.get("oid_paybill")));
		mDataMap.put("money_order", StringUtils.trimToEmpty(input.dataParam.get("money_order")));
		mDataMap.put("result_pay", StringUtils.trimToEmpty(input.dataParam.get("result_pay")));
		mDataMap.put("settle_date", StringUtils.trimToEmpty(input.dataParam.get("settle_date")));
		mDataMap.put("info_order", StringUtils.trimToEmpty(input.dataParam.get("info_order")));
		mDataMap.put("pay_type", StringUtils.trimToEmpty(input.dataParam.get("pay_type")));
		mDataMap.put("bank_code", StringUtils.trimToEmpty(input.dataParam.get("bank_code")));
		mDataMap.put("no_agree", StringUtils.trimToEmpty(input.dataParam.get("no_agree")));
		mDataMap.put("id_type", StringUtils.trimToEmpty(input.dataParam.get("id_type")));
		mDataMap.put("id_no", StringUtils.trimToEmpty(input.dataParam.get("id_no")));
		mDataMap.put("acct_name", StringUtils.trimToEmpty(input.dataParam.get("acct_name")));
		mDataMap.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		payService.saveApplePayment(mDataMap);
	}
	
	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends NotifyPayProcess.PaymentInput {
		/** 苹果支付通知参数 */
		public Map<String,String> dataParam;

		@Override
		public String getBigOrderCode() {
			return dataParam == null ? null : StringUtils.trimToEmpty(dataParam.get("no_order"));
		}
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends NotifyPayProcess.PaymentResult {}

}
