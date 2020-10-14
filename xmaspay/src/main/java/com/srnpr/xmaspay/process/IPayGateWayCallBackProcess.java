package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关回调解析接口
 * @author pang_jhui
 *
 */
public interface IPayGateWayCallBackProcess extends IPayProcess {
	

	/**
	 * 支付网关回调入口
	 * @param mDataMap
	 * 		扩展参数
	 * @return 响应信息
	 */
	public String process(MDataMap mDataMap);
	
}
