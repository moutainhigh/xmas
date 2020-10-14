package com.srnpr.xmaspay.process.prepare;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmaspay.AbstractPaymentProcess;
import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.config.OrderConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 请求支付网关获取支付信息基类
 * @author zhaojunling
 */
public abstract class PreparePayProcess<I extends PreparePayProcess.PaymentInput, R extends PreparePayProcess.PaymentResult> extends AbstractPaymentProcess<I, R>{

	@Override
	public R process(I input) {
		R result = getResult();
		
		if(input == null){
			result.setResultMessage("无输入参数");
			return result;
		}
		
		String bigOrderCode = input.bigOrderCode;

		// 检查订单是否满足支付条件
		checkOrderPreparePay(bigOrderCode, result);
		
		if(result.getResultCode() != PaymentResult.SUCCESS){
			return result;
		}
		
		return doProcess(input);
	}
	
	protected abstract R doProcess(I input);
	
	
	/**
	 * 检查订单是否满足支付条件
	 * @param bigOrderCode
	 * @return
	 */
	private PaymentResult checkOrderPreparePay(String bigOrderCode, PaymentResult result){
		if(StringUtils.isBlank(bigOrderCode)){
			result.setResultCode(0);
			result.setResultMessage("bigOrderCode订单号为空");
			return result;
		}
		
		MDataMap bigOrderInfo = payService.getOrderInfoSupper(bigOrderCode);
		if(bigOrderInfo == null){
			result.setResultCode(0);
			result.setResultMessage("订单不存在[ "+bigOrderCode+"]");
			return result;
		}
		
		if(OrderConfig.PAY_TYPE_2.equals(bigOrderInfo.get("pay_type"))){
			result.setResultCode(0);
			result.setResultMessage("请勿支付货到付款的订单");
			return result;
		}
		
		if(new BigDecimal(bigOrderInfo.get("payed_money")).compareTo(new BigDecimal(bigOrderInfo.get("due_money"))) >= 0){
			result.setResultCode(0);
			result.setResultMessage("订单已支付");
			return result;
		}
		
		MDataMap supperPayment = payService.getOrderInfoSupperPayment(bigOrderCode);
		if(supperPayment != null){
			result.setResultCode(0);
			result.setResultMessage("订单已支付");
			return result;
		}
		
		List<MDataMap> orderInfoList = payService.getOrderInfoList(bigOrderCode);
		if(orderInfoList.isEmpty()){
			result.setResultCode(0);
			result.setResultMessage("订单异常");
			return result;
		}
		
		// 0元单问题，检查是否订单列表里面包含未支付的订单
		boolean payed = true;
		for(MDataMap orderInfo : orderInfoList){
			if(OrderConfig.ORDER_STATUS_1.equals(orderInfo.get("order_status"))){
				payed = false;
				break;
			}
			
			if(OrderConfig.ORDER_STATUS_6.equals(orderInfo.get("order_status"))){
				result.setResultCode(0);
				result.setResultMessage("订单已取消");
				return result;
			}
		}
		
		// 如果有未支付的订单，则还可以继续支付
		if(payed){
			result.setResultCode(0);
			result.setResultMessage("订单已支付");
			return result;
		}
		return null;
	}
	
	/**
	 * 获取支付请求输入对象
	 */
	public abstract static class PaymentInput extends com.srnpr.xmaspay.PaymentInput {
		public PaymentChannel payChannel;
		public String bigOrderCode;
		public String userAgent;
		// 版本号,区分支付宝是否走新参数
		public int v = 0;
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public abstract static class PaymentResult extends com.srnpr.xmaspay.PaymentResult {}
}
