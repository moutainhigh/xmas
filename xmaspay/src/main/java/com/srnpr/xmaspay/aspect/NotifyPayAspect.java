package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;

import com.srnpr.xmaspay.process.notify.NotifyPayProcess;

/**
 * 支付统计日志记录
 * @author zhaojunling
 */
public class NotifyPayAspect extends AbstractAspect {

	@Override
	public String getAction() {
		return "notifyPay";
	}

	@Override
	public String getTarget(JoinPoint joinPoint) {
		NotifyPayProcess.PaymentInput input = (NotifyPayProcess.PaymentInput)joinPoint.getArgs()[0];
		return input == null ? "" : input.getBigOrderCode();
	}

}
