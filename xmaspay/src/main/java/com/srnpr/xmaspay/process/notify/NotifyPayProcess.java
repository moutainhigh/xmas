package com.srnpr.xmaspay.process.notify;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmaspay.AbstractPaymentProcess;
import com.srnpr.xmaspay.config.OrderConfig;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 支付网关的支付完成通知处理
 * @author zhaojunling
 */
public abstract class NotifyPayProcess<I extends NotifyPayProcess.PaymentInput, R extends NotifyPayProcess.PaymentResult> extends AbstractPaymentProcess<I, R>{

	@Override
	public R process(I input) {
		R result = getResult();
		String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		if(input == null || StringUtils.isBlank(input.getBigOrderCode())){
			result.setResultCode(0);
			result.setResultMessage("无输入参数");
			return result;
		}
		
		// 验证签名
		if(!verifyInput(input, result)){
			result.setResultCode(0);
			return warpResult(input,result);
		}
		
		NotifyPay notify = getNotifyPay(input);
		result.notify = notify;
		
		if(!notify.success){
			result.setResultCode(0);
			result.setResultMessage("不处理支付失败的通知");
			return warpResult(input,result);
		}
		
		MDataMap orderInfoSupper = payService.getOrderInfoSupper(StringUtils.trimToEmpty(notify.bigOrderCode));
		if(orderInfoSupper == null){
			result.setResultCode(0);
			result.setResultMessage("订单不存在: "+notify.bigOrderCode);
			return warpResult(input,result);
		}
		
		BigDecimal payedMoney = new BigDecimal(notify.payedMoney);
		BigDecimal dueMoney = new BigDecimal(orderInfoSupper.get("due_money"));
		
		// 校验金额
		if((payedMoney.compareTo(dueMoney) != 0)){
			result.setResultCode(0);
			result.setResultMessage("支付金额和应付金额不一致");
			return warpResult(input,result);
		}
		
		String lockUid = KvHelper.lockCodes(180, "PAYNOTIFY-"+notify.bigOrderCode);
		if(StringUtils.isBlank(lockUid)){
			result.setResultCode(0);
			result.setResultMessage("支付通知并发限制");
			return warpResult(input,result);
		}
		
		// 已经存在的支付信息
		MDataMap payment = payService.getOrderInfoSupperPayment(notify.bigOrderCode);
		// 如果出现多次不同流水号的支付通知则只记录第一次通知的结果
		// 重复支付的情况如果出现则给客户线下退款
		if(payment != null && !payment.get("trade_no").equalsIgnoreCase(notify.tradeNo)){
			result.setResultCode(1);
			result.setResultMessage("重复支付");
			
			// 释放锁
			KvHelper.unLockCodes(lockUid, "PAYNOTIFY-"+notify.bigOrderCode);
			return warpResult(input,result);
		}
		
		if(payment == null){
			// 保存大订单的基础支付信息
			payment = new MDataMap();
			payment.put("big_order_code", notify.bigOrderCode);
			payment.put("pay_type", notify.orderPayType);
			payment.put("trade_no", notify.tradeNo);
			payment.put("payed_money", notify.payedMoney);
			payment.put("from_type", StringUtils.trimToEmpty(getFromType()).toLowerCase());
			payment.put("create_time", date);
			payService.saveOrderInfoUpperPayment(payment);
			
			// 更新主表的已支付信息
			BigDecimal payed_money = new BigDecimal(orderInfoSupper.get("payed_money")).add(new BigDecimal(notify.payedMoney));
			orderInfoSupper.put("payed_money", payed_money.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			orderInfoSupper.put("pay_type", notify.orderPayType);
			orderInfoSupper.put("update_time", date);
			payService.updateOrderSupperPayedMoney(orderInfoSupper);
			
			// 保存具体的支付详细数据
			savePayment(input, notify);
		}
		
		// 兼容原有程序，所以分表插入一下历史表数据
		MDataMap mDataMap = new MDataMap();
		if(OrderConfig.PAY_TYPE_3.equals(notify.orderPayType)){
			// 保存支付宝支付信息
			mDataMap.put("out_trade_no", notify.bigOrderCode);
			mDataMap.put("trade_status", "TRADE_SUCCESS");
			mDataMap.put("sign_type", "");
			mDataMap.put("sign", "");
			mDataMap.put("trade_no", notify.tradeNo);
			mDataMap.put("total_fee", orderInfoSupper.get("due_money"));
			mDataMap.put("gmt_payment", date);
			mDataMap.put("gmt_create", date);
			mDataMap.put("param_value", "");
			mDataMap.put("mark", "");
			mDataMap.put("create_time", date);
			mDataMap.put("process_time", date);
			mDataMap.put("process_result", "");
			mDataMap.put("payment_code", "");
			mDataMap.put("flag_success", "1");
			payService.saveOrderPayment(mDataMap);
		}else if(OrderConfig.PAY_TYPE_5.equals(notify.orderPayType)){
			// 保存微信支付信息
			mDataMap.put("appid", "");
			mDataMap.put("mch_id", "");
			mDataMap.put("out_trade_no", notify.bigOrderCode);
			mDataMap.put("transaction_id", notify.tradeNo);
			mDataMap.put("time_end", date);
			mDataMap.put("sign", "");
			
			mDataMap.put("trade_type", "");
			mDataMap.put("bank_type", "");
			mDataMap.put("result_code", "");
			
			mDataMap.put("total_fee", orderInfoSupper.get("due_money"));
			mDataMap.put("param_value", "");
			mDataMap.put("mark", "001");
			mDataMap.put("process_time", date);
			mDataMap.put("process_result", "");
			mDataMap.put("payment_code", "");
			mDataMap.put("flag_success", "1");
			payService.saveOrderPaymentWechat(mDataMap);
		}
		
		// 更新子表的已支付金额
		List<MDataMap> orderInfoList =  payService.getOrderInfoList(notify.bigOrderCode);
		MDataMap orderPay;
		MDataMap updateMap;
		for(MDataMap map : orderInfoList){
			if(map.get("payed_money").equals("0.00")){
				// 更新已支付金额
				updateMap = new MDataMap();
				updateMap.put("order_code", map.get("order_code"));
				updateMap.put("payed_money", map.get("due_money"));
				updateMap.put("update_time", date);
				payService.updateOrderInfoPayedMoney(updateMap);
			}
			
			orderPay = payService.getOrderPay(map.get("order_code"), notify.orderPayType);
			if(orderPay == null && !map.get("due_money").equals("0.00")) {
				orderPay = new MDataMap();
				orderPay.put("order_code", map.get("order_code"));
				orderPay.put("pay_sequenceid", notify.tradeNo);
				orderPay.put("payed_money", map.get("due_money"));
				orderPay.put("create_time", date);
				orderPay.put("pay_type", notify.orderPayType);
				orderPay.put("pay_bank", StringUtils.trimToEmpty(notify.orderPayBank));
				orderPay.put("pay_remark", "");
				orderPay.put("merchant_id", map.get("buyer_code"));
				orderPay.put("php_code", "");
				orderPay.put("payed_all_fee", map.get("due_money"));
				orderPay.put("payed_fee", "0.00");
				orderPay.put("status", "0");
				orderPay.put("pay_code", "");
				payService.saveOrderPay(orderPay);
			}
		}
		
		try {
			KvHelper.unLockCodes(lockUid, "PAYNOTIFY-"+notify.bigOrderCode);
		} catch (Exception e) {
			//e.printStackTrace();
			// 解锁失败不影响正常逻辑
		}
		
		return warpResult(input,result);
	}
	
	/** 可能需要对结果做些额外操作,设置返回值等 */
	protected R warpResult(I input, R result){
		return result;
	}
		
	/**
	 * 验证请求的参数签名
	 * @param inputParam
	 * @return true 签名成功， false 签名失败
	 */
	protected abstract boolean verifyInput(I input, R result);
	
	/**
	 * 从参数中解析订单的基础信息
	 * @param inputParam
	 * @return
	 */
	protected abstract NotifyPay getNotifyPay(I input);
	
	/**
	 * 获取支付来源，如：paygate通过网关支付、lianlianpay通过连连支付 
	 * @return
	 */
	protected abstract String getFromType();
	
	/**
	 * 保存交易记录
	 * @param input
	 * @param notify
	 */
	protected abstract void savePayment(I input, NotifyPay notify);
	
	/**
	 * 获取支付请求输入对象
	 */
	public abstract static class PaymentInput extends com.srnpr.xmaspay.PaymentInput {
		public abstract String getBigOrderCode();
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public abstract static class PaymentResult extends com.srnpr.xmaspay.PaymentResult {
		public NotifyPay notify;
	}
}
