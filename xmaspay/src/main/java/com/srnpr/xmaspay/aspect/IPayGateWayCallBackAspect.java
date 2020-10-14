package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.response.PayGateWayCallBackResponse;

/**
 * 支付网关回调相关业务处理接口
 * @author pang_jhui
 *
 */
public interface IPayGateWayCallBackAspect extends IPayAspect {
	
	/**
	 * 支付网关回调之前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	/**
	 * 支付网关回调之后
	 * @throws Exception 
	 */
	public void doAfter(JoinPoint joinPoint) throws Exception;
	
	/**
	 * 环绕支付网关回调
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public PayGateWayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;

}
