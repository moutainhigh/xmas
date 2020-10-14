package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;

/**
 * 银联支付相关配置信息接口
 * @author pang_jhui
 *
 */
public interface IUnionPayConfig extends IPayConfig {
	
	/**
	 * 获取请求路径
	 * @return
	 */
	public String getRequestUrl();
	
	/**
	 * 获取classes路径
	 * @return
	 */
	public String getClassPath();
	
	/**
	 * 获取签名证书密码
	 * @return
	 */
	public String getSignCertPath();
	
	/**
	 * 获取签名证书密码
	 * @return
	 */
	public String getSignCertPwd();
	
	/**
	 * 获取签名证书类型
	 * @return
	 */
	public String getSignCertType();
	
	/**
	 * 获取验证签名所在路径
	 * @return
	 */
	public String getValidateCertDir();
	
	/**
	 * 获取敏感信息加密证书路径
	 * @return
	 */
	public String getEncryptCertPath();
	
	/**
	 * 判断是否为单证模式
	 * @return
	 */
	public boolean getSingleMode();
	
	/**
	 * 获取版本号
	 * @return
	 */
	public String getVersion();
	
	/**
	 * 获取字符编码
	 * @return
	 */
	public String getEncoding();
	
	/**
	 * 获取交易类型（默认：消费）
	 * @return
	 */
	public String getTnxType();
	
	/**
	 * 获取交易类型
	 * @return
	 */
	public String getTxnSubType();
	
	/**
	 * 获取产品类型，默认：B2C
	 * @return
	 */
	public String getBizType();
	
	/**
	 * 获取渠道类型，默认：移动
	 * @return
	 */
	public String getChannelType();
	
	/**
	 * 获取接入类型，默认：商户接入
	 * @return
	 */
	public String getAccessType();
	
	/**
	 * 获取商户编号
	 * @return
	 */
	public String getMerId();
	
	/**
	 * 获取支付成功回调地址
	 * @return
	 */
	public String getBackUrl();
	
	/**
	 * 获取币种类型
	 * @return
	 */
	public String getCurrencyCode();

}
