package com.srnpr.xmaspay.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import com.srnpr.xmaspay.face.IPayAspect;
import com.srnpr.xmaspay.request.PayGateWayRequest;
import com.srnpr.xmaspay.response.PayGateWayResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

public interface IPayGateWayAspect extends IPayAspect {
	
	/**
	 * 支付网关请求前执行，判断该订单是否已经支付完成
	 * @param payGateWayRequest
	 * 		支付网关请求信息
	 * @param mDataMap 扩展参数
	 * 		
	 * @return 返回参数
	 * 
	 */
	public Map<String,Object> doProcessBefore(PayGateWayRequest payGateWayRequest, MDataMap mDataMap);
	
	/**
	 * 支付网关请求后
	 * @param payGateWayRequest
	 * 		支付网关请求信息
	 * @param mDataMap 扩展参数
	 */
	public void doProcessAfter(PayGateWayRequest payGateWayRequest, MDataMap mDataMap);
	
	/**
	 * 环绕支付网关
	 * @param joinPoint
	 * 		切入点
	 * @throws Throwable 
	 */
	public PayGateWayResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable;



}
