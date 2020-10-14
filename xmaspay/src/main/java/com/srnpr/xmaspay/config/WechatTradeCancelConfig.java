package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.config.face.IWechatTradeCancelConfig;

/**
 * 微信交易取消相关配置信息
 * @author pang_jhui
 *
 */
public class WechatTradeCancelConfig extends WechatConfig implements IWechatTradeCancelConfig {

	@Override
	public String getRequestUrl() {

		return bConfig("xmaspay.wechat_trade_cancel_url");
	}

}
