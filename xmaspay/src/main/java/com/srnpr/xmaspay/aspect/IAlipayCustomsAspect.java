package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;

/**
 * 支付宝报关相关业务处理接口
 * @author pang_jhui
 *
 */
public interface IAlipayCustomsAspect extends IPayAspect {
	
	/**
	 * 支付宝报关之前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	/**
	 * 支付宝报关之后
	 * @throws Exception 
	 */
	public void doAfter(JoinPoint joinPoint) throws Exception;
	
	/**
	 * 环绕支付宝报关
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public AlipayCustomsResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;

}
