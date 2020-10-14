package com.srnpr.xmaspay.process.refund;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.AbstractPaymentProcess;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class PayGateRefundNotifyProcess extends AbstractPaymentProcess<PayGateRefundNotifyProcess.PaymentInput, PayGateRefundNotifyProcess.PaymentResult>{
	@Override
	public PaymentResult process(PaymentInput input) {
		PaymentResult result = getResult();
		JSONObject postObj = null;
		try {
			postObj = JSONObject.parseObject(input.postData);
		} catch (Exception e) {
			//e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("解析JSON失败");
			return result;
		}
		
		String text = DigestUtils.md5Hex(input.postData+XmasPayConfig.getPayGatePass());
		if(!text.equalsIgnoreCase(input.sign)){
			result.setResultCode(0);
			result.setResultMessage("签名校验失败");
			return result;
		}
		
		String refundOrderCode = postObj.getString("c_refund_order"); // 退款单号
		
		// 插入退款通知记录
		if(DbUp.upTable("oc_payment_refund").count("c_refund_order",refundOrderCode) == 0){
			MDataMap refundMap = new MDataMap();
			refundMap.put("c_order", StringUtils.trimToEmpty(postObj.getString("c_order")));
			refundMap.put("c_mid", StringUtils.trimToEmpty(postObj.getString("c_mid")));
			refundMap.put("bank_refund_order",StringUtils.trimToEmpty(postObj.getString("bank_refund_order")));
			refundMap.put("version", StringUtils.trimToEmpty(postObj.getString("Version")));
			refundMap.put("c_refund_order", StringUtils.trimToEmpty(postObj.getString("c_refund_order")));
			refundMap.put("c_refund_order_batch", StringUtils.trimToEmpty(postObj.getString("c_refund_order_batch")));
			refundMap.put("c_ymd", StringUtils.trimToEmpty(postObj.getString("c_ymd")));
			refundMap.put("refund_amount", StringUtils.trimToEmpty(postObj.getString("refund_amount")));
			refundMap.put("refund_order", StringUtils.trimToEmpty(postObj.getString("refund_order")));
			refundMap.put("refund_order_batch", StringUtils.trimToEmpty(postObj.getString("refund_order_batch")));
			refundMap.put("refund_order_status", StringUtils.trimToEmpty(postObj.getString("refund_order_status")));
			refundMap.put("ymd", StringUtils.trimToEmpty(postObj.getString("ymd")));
			refundMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("oc_payment_refund").dataInsert(refundMap);
		}
		
		result.setResultCode(1);
		result.setResultMessage(StringUtils.trimToEmpty(postObj.getString("descrption")));
		return result;
	}

	@Override
	public PaymentResult getResult() {
		return new PayGateRefundNotifyProcess.PaymentResult();
	}
	
	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends com.srnpr.xmaspay.PaymentInput {
		/** 退款通知JSON内容 */
		public String postData;
		/** 退款通知签名内容 */
		public String sign;
		
		public String returnMoneyCode;
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends com.srnpr.xmaspay.PaymentResult {
	}

}
