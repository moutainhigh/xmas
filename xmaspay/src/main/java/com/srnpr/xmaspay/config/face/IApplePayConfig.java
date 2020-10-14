package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * applePay支付配置信息
 * @author pangjh
 *
 */
public interface IApplePayConfig extends IPayConfig{
	
	/**
	 * 获取商户编号
	 * @return
	 */
	public String getMerchantId();
	
	/**
	 * 获取签名类型
	 * @return
	 */
	public String getSignType();
	
	/**
	 * 获取商户业务类型
	 * @return
	 */
	public String getBusiType();
	
	/**
	 * 获取服务器异步通知路径
	 * @return
	 */
	public String getNotifyUrl();
	
	/**
	 * 获取applePay商户平台编号
	 * @return
	 */
	public String getApMerchantId();
	
	/**
	 * 获取md5加密key
	 * @return
	 */
	public String getMd5Key();
	
	/**
	 * 获取签名信息
	 * @param mDataMap
	 * 		请求参数集合
	 * @return 签名后数据
	 */
	public String getSign(MDataMap mDataMap);

}
