package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.response.ApplePayResponse;

/**
 * 苹果支付业务解析接口
 * @author pangjh
 *
 */
public interface IApplePayProcess extends IPayProcess {
	
	/**
	 * applePay支付解析业务
	 * @param orderCode
	 * 		订单编号
	 * @return IPayResponse
	 */
	public ApplePayResponse process(String orderCode);

}
