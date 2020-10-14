package com.srnpr.xmaspay.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import com.srnpr.xmaspay.process.refund.PayGateRefundNotifyProcess;

/**
 * 退款日志记录
 * @author zhaojunling
 */
public class RefundNotifyPayAspect extends AbstractAspect {

	@Override
	public String getAction() {
		return "refundNotifyPay";
	}

	@Override
	public String getTarget(JoinPoint joinPoint) {
		PayGateRefundNotifyProcess.PaymentInput input = (PayGateRefundNotifyProcess.PaymentInput)joinPoint.getArgs()[0];
		return StringUtils.trimToEmpty(input.returnMoneyCode);
	}

}
