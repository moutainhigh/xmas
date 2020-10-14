package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.face.IPayService;

/**
 * 支付相关基类
 * @author pang_jhui
 *
 */
public abstract class BasePayProcess implements IPayProcess {
	
	/*支付业务处理类*/
	private IPayService payService;
	
	/*支付配置信息*/
	private IPayConfig payConfig;

	/**
	 * 获取支付网关业务实现
	 * @return 支付网关业务实现
	 */
	public IPayService getPayService() {
		return payService;
	}

	/**
	 * 设置支付网关业务实现
	 * @param payService
	 */
	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	/**
	 * 获取支付网关配置信息
	 * @return 支付网关配置信息
	 */
	public IPayConfig getPayConfig() {
		return payConfig;
	}

	/**
	 * 设置支付网关配置信息
	 * @param payConfig
	 */
	public void setPayConfig(IPayConfig payConfig) {
		this.payConfig = payConfig;
	}

	
	
	
	

}
