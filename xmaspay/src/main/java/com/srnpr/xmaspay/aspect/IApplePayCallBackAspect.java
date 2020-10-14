package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.response.ApplePayCallBackResponse;

/**
 * applePay支付回调相关业务处理接口
 * @author pang_jhui
 *
 */
public interface IApplePayCallBackAspect extends IPayAspect {
	
	/**
	 * applePay支付回调之前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	/**
	 * applePay支付回调之后
	 * @throws Exception 
	 */
	public void doAfter(JoinPoint joinPoint) throws Exception;
	
	/**
	 * 环绕applePay支付回调
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public ApplePayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;

}
