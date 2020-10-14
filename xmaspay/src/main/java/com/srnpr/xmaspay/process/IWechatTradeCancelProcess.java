package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.config.face.IWechatTradeCancelConfig;
import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatUnifyResponse;

/**
 * 微信交易取消解析
 * @author pang_jhui
 *
 */
public interface IWechatTradeCancelProcess extends IPayProcess {
	
	/**
	 * 根据订单编号取消交易
	 * @param orderCode
	 * 		订单编号
	 * @return 微信响应信息
	 */
	public WechatUnifyResponse process(String orderCode);
	
	/**
	 * 初始化请求信息
	 * @param orderCode
	 * 		订单编号
	 * @param config
	 * 		配置信息
	 * @return 请求信息
	 * @throws Exception 
	 */
	public WechatRequest initRequest(String orderCode,IWechatTradeCancelConfig config) throws Exception;

}
