package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.common.WechatCustomEnum;
import com.srnpr.xmaspay.config.face.IWechatCustomConfig;
import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.request.WechatCustomRequest;
import com.srnpr.xmaspay.response.WechatUnifyResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信通关状态解析
 * @author pang_jhui
 *
 */
public interface IWechatCustomProcess extends IPayProcess {
	
	/**
	 * 根据订单编号报关
	 * @param orderCode
	 * 		订单编号
	 * @param customs
	 * 		海关
	 * @return 微信响应信息
	 */
	public WechatUnifyResponse process(String orderCode, WechatCustomEnum customs,MDataMap mDataMap);
	
	/**
	 * 初始化请求信息
	 * @param orderCode
	 * 		订单编号
	 * @param config
	 * 		配置信息
	 * @param customs
	 * 		海关 	
	 * @return 请求信息
	 * @throws Exception 
	 */
	public WechatCustomRequest initRequest(String orderCode,WechatCustomEnum customs,IWechatCustomConfig config) throws Exception;

}
