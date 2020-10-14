package com.srnpr.xmaspay.process.refund;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.entity.StringEntity;

import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;

public class PayGateRefundReqProcess extends RefundReqProcess<PayGateRefundReqProcess.PaymentInput, PayGateRefundReqProcess.PaymentResult>{

	@Override
	protected void doProcess(PaymentInput input, PaymentResult result) {
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",input.orderCode);
		BigDecimal dueMoney = new BigDecimal(orderInfo.get("due_money"));
		
		if(input.money.compareTo(dueMoney) > 0) {
			result.setResultCode(0);
			result.setResultMessage("退款金额不能大于订单支付金额");
			return;
		}
		
		// 优先使用退款编号
		String cRefundOrder = input.returnMoneyCode;
		// 其次是订单号加序号（兼容LD系统退款）
		if(StringUtils.isBlank(cRefundOrder)) {
			cRefundOrder = input.orderCode + "#" + input.orderSeq;
		}
		
		JSONObject postData = new JSONObject();
		postData.put("c_mid", XmasPayConfig.getPayGateMid());
		postData.put("c_ymd", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		postData.put("c_order", orderInfo.get("big_order_code"));
		postData.put("c_refund_order", cRefundOrder);
		postData.put("c_refund_reason", StringUtils.isBlank(input.remark) ? "系统退款" : input.remark);
		postData.put("refund_amount", input.money);
		postData.put("callbackurl", XmasPayConfig.getPayGateRefundNotifyURL());
		String postDataText = postData.toJSONString();
		
		StringBuilder text = new StringBuilder();
		text.append("RefundCreateOrderV1").append(postDataText).append(XmasPayConfig.getPayGatePass());
		String sign = DigestUtils.md5Hex(text.toString()).toLowerCase();
		String url = XmasPayConfig.getPayGateRefundURL()+"?sign="+sign;
		
		MDataMap logMap = new MDataMap();
		logMap.put("action", "submitRefund");
		logMap.put("target", input.orderCode);
		logMap.put("method", "com.srnpr.xmaspay.process.refund.PayGateRefundReqProcess#doProcess");
		logMap.put("request", postDataText);
		logMap.put("request_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		logMap.put("exception", "");
		logMap.put("response", "");
		
		JSONObject resp = null;
		try {
			String content = WebClientSupport.poolRequest(url, new StringEntity(postDataText, "UTF-8"));
			logMap.put("response_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("response", content);
			
			resp = JSONObject.parseObject(content);
			
			logMap.put("flag_success", "1");
		} catch (Exception e) {
			logMap.put("response_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("flag_success", "0");
			logMap.put("exception", ExceptionUtils.getStackTrace(e));
		} finally {
			logMap.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 记录请求支付网关的返回结果日志
			payService.saveXmasPayLog(logMap);
		}
		
		if(resp == null){
			result.setResultCode(0);
			result.setResultMessage("退款申请失败");
			return;
		}
		
		if(resp.getIntValue("result") == 1){
			result.setResultCode(1);
			result.setResultMessage("退款申请成功");
			return;
		}
		
		result.setResultCode(0);
		result.setResultMessage("退款申请失败："+resp.getString("descrption"));
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
