package com.srnpr.xmaspay.config.face;

/**
 * 微信交易取消配置信息
 * @author pang_jhui
 *
 */
public interface IWechatTradeCancelConfig extends IWechatConfig {
	
	/**
	 * 获取微信交易取消配置信息
	 * @return 请求url
	 */
	public String getRequestUrl();

}
