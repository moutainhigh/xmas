package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.response.AlipayUnifyResponse;

/**
 * 支付宝交易取消
 * @author pang_jhui
 *
 */
public interface IAlipayTradeCancelAspect {
	
	/**
	 * 交易取消响应前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	
	/**
	 * 环绕交易取消
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public AlipayUnifyResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;
	
	/**
	 * 环绕交易取消之后
	 * @param response
	 * 		接口响应信息
	 */
	public void doAfter(String order_code, AlipayUnifyResponse response);

}
