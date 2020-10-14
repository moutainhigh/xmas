package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

public interface IPayGateWayConfig extends IPayConfig {
	
	/**
	 * 获取请求的url
	 * @return
	 */
	public String getRquestUrl();
	
	/**
	 * 生成签名
	 * @param mDataMap
	 * 		签名生成所需数据
	 * @return 签名
	 */
	public String getSign(MDataMap mDataMap);
	
	/**
	 * 获取接口版本
	 * @return 版本号
	 */
	public String getVersion();
	
	/**
	 * 获取请求使用的协议
	 * @return 协议名称
	 */
	public String getProtocol();
	
	/**
	 * 获取请求的IP地址
	 * @return IP地址
	 */
	public String getRequestIp();
	
	/**
	 * 获取请求路径
	 * @return 请求路径
	 */
	public String getRequestPath();
	
	/**
	 * 获取商户编号
	 * @return 商户编号
	 */
	public String getMerchantCode();
	
	/**
	 * 商户交易密钥
	 * @return 商户密钥
	 */
	public String getMerchantPwd();
	
	/**
	 * 获取支付回调路径
	 * @param contextPath
	 * 		上下文路径
	 * @return 支付回调
	 */
	public String getPayCallBackUrl(String contextPath);

}
