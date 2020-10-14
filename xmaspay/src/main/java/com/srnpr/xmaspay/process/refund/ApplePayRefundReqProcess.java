package com.srnpr.xmaspay.process.refund;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApplePayRefundReqProcess extends RefundReqProcess<ApplePayRefundReqProcess.PaymentInput, ApplePayRefundReqProcess.PaymentResult>{

	@Override
	protected void doProcess(PaymentInput input, PaymentResult result) {
		MDataMap returnMoney = DbUp.upTable("oc_return_money").one("return_money_code", input.returnMoneyCode);
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",returnMoney.get("order_code"));
		MDataMap payment = DbUp.upTable("oc_orderinfo_upper_payment").one("big_order_code",orderInfo.get("big_order_code"),"pay_type","449746280013");
		
		BigDecimal onlineMoney = new BigDecimal(returnMoney.get("online_money"));
		BigDecimal dueMoney = new BigDecimal(returnMoney.get("due_money"));
		
		// 退款金额不能超过订单的应付款
		BigDecimal refundMoney = onlineMoney.compareTo(dueMoney) < 0 ? onlineMoney : dueMoney;
		
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("sign_type", "version");
		dataMap.put("merchant_id", XmasPayConfig.getApplePayOidPartner());
		dataMap.put("oid_billno", payment.get("trade_no"));
		dataMap.put("col_custid", XmasPayConfig.getApplePayApMerchantId());
		dataMap.put("col_amt_refund", refundMoney.toString());
		dataMap.put("col_cur_code", "RMB");
		
		
		
		result.setResultCode(0);
	}
	
	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}

	public static class PaymentInput extends RefundReqProcess.PaymentInput {
	}

	public static class PaymentResult extends RefundReqProcess.PaymentResult {
	}

}
