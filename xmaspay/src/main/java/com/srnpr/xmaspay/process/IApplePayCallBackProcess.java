package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * applePay回调解析
 * @author pang_jhui
 *
 */
public interface IApplePayCallBackProcess extends IPayProcess {
	
	/**
	 * applePay支付解析
	 * @param mDataMap
	 * 		请求参数集合
	 * @return 响应信息
	 */
	public String process(MDataMap mDataMap);

}
