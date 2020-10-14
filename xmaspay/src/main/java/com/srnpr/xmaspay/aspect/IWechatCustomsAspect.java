package com.srnpr.xmaspay.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.response.WechatCustomResponse;

/**
 * 微信报关相关业务处理接口
 * @author pang_jhui
 *
 */
public interface IWechatCustomsAspect extends IPayAspect {
	
	/**
	 * 微信报关之前
	 */
	public void doBefore(JoinPoint joinPoint);
	
	/**
	 * 微信报关之后
	 * @throws Exception 
	 */
	public void doAfter(JoinPoint joinPoint) throws Exception;
	
	/**
	 * 环绕微信报关
	 * @param joinPoint
	 * @throws Throwable 
	 */
	public WechatCustomResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;

}
