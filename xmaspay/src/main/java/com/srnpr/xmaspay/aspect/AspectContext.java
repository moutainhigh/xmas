package com.srnpr.xmaspay.aspect;

import java.util.Date;

/**
 * 提供方法的执行开始和结束时间的记录
 * @author zhaojunling
 */
public class AspectContext {

	/** 方法开始执行时间 */
	Date doBeforAt;
	/** 方法执行结束时间 */
	Date doAfterAt;
	/** 拦截类方法 */
	String method;
}
