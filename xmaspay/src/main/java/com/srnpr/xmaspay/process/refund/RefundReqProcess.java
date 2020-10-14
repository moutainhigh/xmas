package com.srnpr.xmaspay.process.refund;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmaspay.AbstractPaymentProcess;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 根据退款单发送退款请求
 */
public abstract class RefundReqProcess<I extends RefundReqProcess.PaymentInput, R extends RefundReqProcess.PaymentResult> extends AbstractPaymentProcess<I, R>{

	@Override
	public R process(I input) {
		R result = getResult();
		MDataMap returnMoney = null;
		// 退款单号存在则优先使用退款单的金额
		if(input != null && StringUtils.isNotBlank(input.returnMoneyCode)){
			returnMoney = DbUp.upTable("oc_return_money").one("return_money_code", input.returnMoneyCode);
			input.money = new BigDecimal(returnMoney.get("online_money"));
			input.orderCode = returnMoney.get("order_code");
		}
		
		if(StringUtils.isBlank(input.orderCode)){
			result.setResultCode(0);
			result.setResultMessage("退款订单不存在");
			return result;
		}
		
		if(input.money == null || input.money.compareTo(BigDecimal.ZERO) <= 0) {
			result.setResultCode(0);
			result.setResultMessage("退款金额不能低于0");
			return result;
		}
		
		if(returnMoney != null && !"4497153900040003".equals(returnMoney.get("status"))){
			result.setResultCode(0);
			result.setResultMessage("退款单不是待退款状态");
			return result;
		}
		
		String lockUid = KvHelper.lockCodes(60, "PAYREFUND-"+input.orderCode);
		if(StringUtils.isBlank(lockUid)){
			result.setResultCode(0);
			result.setResultMessage("正在处理退款中");
			return warpResult(input,result);
		}
		
		try {
			// 发送退款请求
			doProcess(input, result);
		} finally {
			KvHelper.unLockCodes(lockUid, "PAYREFUND-"+input.orderCode);
		}
		
		return warpResult(input, result);
	}
	
	/** 可能需要对结果做些额外操作,设置返回值等 */
	protected abstract void doProcess(I input, R result);
	
	/** 可能需要对结果做些额外操作,设置返回值等 */
	protected R warpResult(I input, R result){
		return result;
	}

	/**
	 * 获取支付请求输入对象 <br>
	 * 惠家有订单传入退款单号，LD订单传入订单号和订单序号
	 */
	public abstract static class PaymentInput extends com.srnpr.xmaspay.PaymentInput {
		/** 退款单号 */
		public String returnMoneyCode;
		/** 备注 */
		public String remark;
		
		/** 订单号 */
		public String orderCode;
		/** 订单序号 */
		public String orderSeq;
		/** 退款金额 */
		public BigDecimal money = BigDecimal.ZERO;
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public abstract static class PaymentResult extends com.srnpr.xmaspay.PaymentResult {
		
	}

}
