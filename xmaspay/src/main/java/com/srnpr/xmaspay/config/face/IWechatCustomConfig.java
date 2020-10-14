package com.srnpr.xmaspay.config.face;

/**
 * 微信通关配置信息
 * @author pang_jhui
 *
 */
public interface IWechatCustomConfig extends IWechatConfig {
	
	/**
	 * 获取微信通关配置信息
	 * @return 请求url
	 */
	public String getRequestUrl();

}
