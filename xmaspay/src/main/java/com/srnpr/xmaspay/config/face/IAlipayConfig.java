package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝配置信息
 * @author pang_jhui
 *
 */
public interface IAlipayConfig extends IPayConfig {
	
	/**
	 * 获取开放平台请求路径
	 * @return 路径
	 */
	public String getRequestUrl();
	
	/**
	 * 获取私玥
	 * @return 私钥
	 */
	public String getPirvateKey();
	
	/**
	 * 获取公玥
	 * @return 公钥
	 */
	public String getPublicKey();
	
	/**
	 * 获取支付宝分配给商户的partner
	 * @return partner
	 */
	public String getPartner();
	
	/**
	 * 获取RSA签名算法
	 * @return 签名算法
	 */
	public String getSignRSAAlgorithm();
	
	/**
	 * 获取支付宝请求参数字符编码
	 * @return 字符编码
	 */
	public String getRequestCharset();
	
	/**
	 * 获取签名类型
	 * @return 签名类型
	 */
	public String getSignType();
	
	/**
	 * 获取签名信息(api1.0 不包含sign signtype)
	 * @param mDataMap
	 * 		签名内容
	 * @return 签名信息
	 */
	public String getSign(MDataMap mDataMap);
	
	/**
	 * 获取接口版本号
	 * @return 版本号
	 */
	public String getVersion();
	
	/**
	 * 获取支付宝的安全校验码
	 * @return
	 */
	public String getVerifyKey();

}
