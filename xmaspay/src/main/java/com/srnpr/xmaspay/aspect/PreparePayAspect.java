package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;

import com.srnpr.xmaspay.process.prepare.PreparePayProcess;

/**
 * 获取支付参数记录
 * @author zhaojunling
 */
public class PreparePayAspect extends AbstractAspect {

	@Override
	public String getAction() {
		return "preparePay";
	}

	@Override
	public String getTarget(JoinPoint joinPoint) {
		PreparePayProcess.PaymentInput input = (PreparePayProcess.PaymentInput)joinPoint.getArgs()[0];
		return input == null ? "" : input.bigOrderCode;
	}

}
