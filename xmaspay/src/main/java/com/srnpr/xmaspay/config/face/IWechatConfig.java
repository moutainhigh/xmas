package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信配置信息
 * @author pang_jhui
 *
 */
public interface IWechatConfig extends IPayConfig {
	
	/**
	 * 获取微信分配给商户的appid
	 * @param manageCode
	 * 		渠道编号
	 * @return appid
	 */
	public String getMerchantAppId(String manageCode);
	
	/**
	 * 根据渠道编号获取分配商户的编号
	 * @param manageCode
	 * 		渠道编号
	 * @return 商户编号
	 */
	public String getMechantId(String manageCode);
	
	/**
	 * 根据渠道编号获取签名密钥
	 * @param manageCode
	 *		渠道编号
	 * @return 签名密钥
	 */
	public String getSignKey(String manageCode);
	
	/**
	 * 获取签名
	 * @param mDataMap
	 * 		签名信息
	 * @param manageCode
	 * 		渠道编号
	 * @return 签名
	 */
	public String getSign(String manageCode, MDataMap mDataMap);

}
