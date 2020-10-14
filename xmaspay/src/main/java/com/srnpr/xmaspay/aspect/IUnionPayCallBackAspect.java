package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.response.UnionPayCallBackResponse;

/**
 * 银联支付回调相关业务处理接口
 * @author pang_jhui
 *
 */
public interface IUnionPayCallBackAspect extends IPayAspect {
	
	/**
	 * 银联支付回调之前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	/**
	 * 支付网关回调之后
	 * @throws Exception 
	 */
	public void doAfter(JoinPoint joinPoint) throws Exception;
	
	/**
	 * 环绕银联支付回调
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public UnionPayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;

}
