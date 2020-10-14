package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IWechatCustomConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.WechatCustomRequest;
import com.srnpr.xmaspay.response.WechatCustomResponse;

/**
 * 微信通关状态业务实现
 * @author pang_jhui
 *
 */
public interface IWechatCustomService extends IPayService {
	
	/**
	 * 微信通关实现
	 * @param request
	 * 		请求信息
	 * @param config
	 * 		配置信息
	 * @return 接口响应信息
	 */
	public WechatCustomResponse doProcess(WechatCustomRequest request, IWechatCustomConfig customConfig);



}
