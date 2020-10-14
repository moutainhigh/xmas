package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.config.face.IWechatCustomConfig;

/**
 * 微信通关配置信息
 * @author pang_jhui
 *
 */
public class WechatCustomConfig extends WechatConfig implements IWechatCustomConfig {

	@Override
	public String getRequestUrl() {
		
		return bConfig("xmaspay.wechat_custom_url");
		
	}

}
