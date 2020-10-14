package com.srnpr.xmaspay.aspect;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.JoinPoint;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmaspay.PaymentInput;
import com.srnpr.xmaspay.PaymentResult;
import com.srnpr.xmaspay.service.XmasPayService;
import com.srnpr.zapcom.basemodel.MDataMap;

public abstract class AbstractAspect {

	private XmasPayService payService;
	
	public void setPayService(XmasPayService payService) {
		this.payService = payService;
	}
	
	/**
	 * 业务执行前
	 * @param point
	 * @param returnValue
	 */
	public void doBefore(JoinPoint joinPoint){
		PaymentInput input = (PaymentInput)joinPoint.getArgs()[0];
		if(input == null) return;
		input.aspectContext = new AspectContext();
		input.aspectContext.doBeforAt = new Date();
	}
	
	/**
	 * 业务执行完毕返回结果，且未抛出异常
	 * @param point
	 * @param returnValue
	 */
	public void doAfterReturn(JoinPoint joinPoint, Object returnValue){
		try {
			saveAspectLog(joinPoint, (PaymentResult)returnValue, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 业务抛出异常时
	 * @param joinPoint
	 * @param ex
	 */
	public void doThrowing(JoinPoint joinPoint,Throwable e){
		try {
			saveAspectLog(joinPoint, null, e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 获取操作标识
	 * @param mDataMap
	 */
	public abstract String getAction();
	
	/**
	 * 默认取当前操作的大订单号
	 * @param joinPoint
	 * @return
	 */
	public abstract String getTarget(JoinPoint joinPoint);
	
	private void saveAspectLog(JoinPoint joinPoint, PaymentResult result, Throwable e){
		PaymentInput input = (PaymentInput)joinPoint.getArgs()[0];
		if(input != null) {
			if(input.aspectContext == null) input.aspectContext = new AspectContext();
			input.aspectContext.doAfterAt = new Date();
			input.aspectContext.method = joinPoint.getTarget().getClass().getName()+"#"+joinPoint.getSignature().getName();
		} 
		
		MDataMap dataMap = new MDataMap();
		if(input != null && input.aspectContext != null && input.aspectContext.doBeforAt != null){
			dataMap.put("request_time", DateFormatUtils.format(input.aspectContext.doBeforAt, "yyyy-MM-dd HH:mm:ss"));
		}else{
			dataMap.put("request_time", "");
		}
		if(input != null && input.aspectContext != null && input.aspectContext.doAfterAt != null){
			dataMap.put("response_time", DateFormatUtils.format(input.aspectContext.doAfterAt, "yyyy-MM-dd HH:mm:ss"));
		}else{
			dataMap.put("response_time", "");
		}
		if(input != null && input.aspectContext != null && input.aspectContext.method != null){
			dataMap.put("method", input.aspectContext.method);
		}else{
			dataMap.put("method", "");
		}
		
		dataMap.put("action", getAction());
		if(StringUtils.isBlank(dataMap.get("action"))) {
			dataMap.put("action", joinPoint.getTarget().getClass().getName());
		}
		
		dataMap.put("target", StringUtils.trimToEmpty(getTarget(joinPoint)));
		
		if(e == null && result != null){
			dataMap.put("flag_success", result.getResultCode()+"");
		}else{
			dataMap.put("flag_success", "0");
		}
		
		// 不记录 aspectContext的内容
		if(input != null) input.aspectContext = null;

		dataMap.put("request", input == null ? "" : JSON.toJSONString(input));
		dataMap.put("response", result == null ? "" : JSON.toJSONString(result));
		dataMap.put("exception", e == null ? "" : ExceptionUtils.getStackTrace(e));
		dataMap.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		payService.saveXmasPayLog(dataMap);
	}
}
