package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 银联支付回调
 * @author pang_jhui
 *
 */
public interface IUnionPayCallBackProcess extends IPayProcess {
	
	/**
	 * 银联支付回调解析
	 * @param mDataMap
	 * 		请求信息集合
	 * @return 响应信息
	 */
	public String process(MDataMap mDataMap);

}
