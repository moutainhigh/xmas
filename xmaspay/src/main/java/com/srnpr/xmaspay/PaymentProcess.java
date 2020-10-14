package com.srnpr.xmaspay;

/**
 * 外部调用的支付接口超类，所有支持对外调用的支付服务统一实现此接口
 * 
 * @author zhaojunling
 */
public interface PaymentProcess<Input, Result>{

	public Result process(Input input);
	
}
