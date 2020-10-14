package com.srnpr.xmaspay;

import com.srnpr.xmaspay.service.XmasPayService;

/**
 * 支付接口服务抽象超类，继承此类可减少部分重复代码
 * @author zhaojunling
 *
 * @param <Input>
 * @param <Result>
 */
public abstract class AbstractPaymentProcess<Input extends PaymentInput, Result extends PaymentResult> implements PaymentProcess<Input, Result>{

	protected XmasPayService payService;
	
	public void setPayService(XmasPayService payService) {
		this.payService = payService;
	}
	
	/**
	 * 返回具体的结果对象
	 * @return
	 */
	public abstract Result getResult();
}
