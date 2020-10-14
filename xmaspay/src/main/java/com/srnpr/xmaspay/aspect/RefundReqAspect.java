package com.srnpr.xmaspay.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import com.srnpr.xmaspay.process.refund.RefundReqProcess;

/**
 * 申请退款日志记录
 * @author zhaojunling
 */
public class RefundReqAspect extends AbstractAspect {

	@Override
	public String getAction() {
		return "refundReq";
	}

	@Override
	public String getTarget(JoinPoint joinPoint) {
		RefundReqProcess.PaymentInput input = (RefundReqProcess.PaymentInput)joinPoint.getArgs()[0];
		return StringUtils.trimToEmpty(input.orderCode);
	}

}
