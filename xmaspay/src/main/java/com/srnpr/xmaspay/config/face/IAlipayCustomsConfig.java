package com.srnpr.xmaspay.config.face;

import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝通关配置信息
 * @author zhaojunling
 *
 */
public interface IAlipayCustomsConfig extends IAlipayConfig {
	
	/**
	 * 获取报关服务名称
	 * @return 请求url
	 */
	public String getMethodCustoms();
	
	/**
	 * 获取签名
	 * @param manageCode
	 * @param mDataMap
	 * @return
	 */
	public String getSign(MDataMap mDataMap);
}
