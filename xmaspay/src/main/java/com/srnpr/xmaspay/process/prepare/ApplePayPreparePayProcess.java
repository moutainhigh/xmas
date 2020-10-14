package com.srnpr.xmaspay.process.prepare;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.util.ApplePayUtils;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 获取苹果支付的待支付信息
 * @author zhaojunling
 */
public class ApplePayPreparePayProcess extends PreparePayProcess<ApplePayPreparePayProcess.PaymentInput, ApplePayPreparePayProcess.PaymentResult> {

	@Override
	protected PaymentResult doProcess(PaymentInput input) {
		PaymentResult result = new PaymentResult();
		String bigOrderCode = input.bigOrderCode;
		
		MDataMap bigOrderInfo = payService.getOrderInfoSupper(bigOrderCode);
		List<MDataMap> orderInfoList = payService.getOrderInfoList(bigOrderCode);
		MDataMap orderAddressMap = payService.getOrderAddress(orderInfoList.get(0).get("order_code"));
		
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("oid_partner", XmasPayConfig.getApplePayOidPartner());
		dataMap.put("sign_type", "MD5");
		dataMap.put("busi_partner", "109001");
		dataMap.put("no_order", bigOrderCode);
		try {
			dataMap.put("dt_order", DateFormatUtils.format(DateUtils.parseDate(bigOrderInfo.get("create_time"), "yyyy-MM-dd HH:mm:ss"), "yyyyMMddHHmmss"));
		} catch (ParseException e) {
			e.printStackTrace();
			dataMap.put("dt_order", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		}
		
		dataMap.put("money_order", bigOrderInfo.get("due_money"));
		dataMap.put("notify_url", XmasPayConfig.getApplePayNotifyUrl());
		
		// 风控参数
		MDataMap memberInfoMap = payService.getMemberInfo(bigOrderInfo.get("buyer_code"));
		Map<String,String> riskMap = new HashMap<String, String>();
		riskMap.put("frms_ware_category", "4001");
		riskMap.put("user_info_dt_registe", StringUtils.trimToEmpty(memberInfoMap.get("create_time")).replaceAll("[-:\\s]", ""));
		riskMap.put("delivery_addr_province", orderAddressMap.get("area_code").substring(0, 2)+"0000");
		riskMap.put("delivery_addr_city", orderAddressMap.get("area_code").substring(0, 4)+"00");
		riskMap.put("delivery_phone", orderAddressMap.get("mobilephone"));
		riskMap.put("logistics_mode", "2");
		riskMap.put("delivery_cycle", "24h");
		
		// 设置风控参数
		dataMap.put("risk_item", new JSONObject(riskMap).toJSONString());
		// 订单有效期
		dataMap.put("valid_order", "1440");
		// 设置签名
		dataMap.put("sign", ApplePayUtils.createSign(dataMap, XmasPayConfig.getApplePayKeyMd5()));
		
		// 不参与签名的字段
		dataMap.put("ap_merchant_id", XmasPayConfig.getApplePayApMerchantId());
		dataMap.put("user_id", bigOrderInfo.get("buyer_code"));
		
		// 设置返回值
		result.oid_partner = dataMap.get("oid_partner");
		result.sign_type = dataMap.get("sign_type");
		result.busi_partner = dataMap.get("busi_partner");
		result.no_order = dataMap.get("no_order");
		result.dt_order = dataMap.get("dt_order");
		result.money_order = dataMap.get("money_order");
		result.notify_url = dataMap.get("notify_url");
		result.ap_merchant_id = dataMap.get("ap_merchant_id");
		result.user_id = dataMap.get("user_id");
		result.risk_item = dataMap.get("risk_item");
		result.valid_order = dataMap.get("valid_order");
		result.sign = dataMap.get("sign");
		
		result.setResultCode(PaymentResult.SUCCESS);
		return result;
	}
	
	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends PreparePayProcess.PaymentInput {}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends PreparePayProcess.PaymentResult {
		public String oid_partner;
		public String sign_type;
		public String busi_partner;
		public String no_order;
		public String dt_order;
		public String money_order;
		public String notify_url;
		public String ap_merchant_id;
		public String user_id;
		public String risk_item;
		public String valid_order;
		public String sign;
	}

	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
}
