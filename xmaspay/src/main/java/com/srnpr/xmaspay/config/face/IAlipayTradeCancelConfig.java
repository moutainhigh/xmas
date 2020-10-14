package com.srnpr.xmaspay.config.face;

/**
 * 支付宝交易取消配置信息
 * @author pang_jhui
 *
 */
public interface IAlipayTradeCancelConfig extends IAlipayConfig{
	
	/**
	 * 获取请求接口
	 * @return 接口名称
	 */
	public String getMethod();

}
