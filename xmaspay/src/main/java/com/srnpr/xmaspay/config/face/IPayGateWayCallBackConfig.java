package com.srnpr.xmaspay.config.face;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关回调配置信息
 * @author pang_jhui
 *
 */
public interface IPayGateWayCallBackConfig extends IPayConfig {
	
	/**
	 * 生成签名
	 * @param mDataMap
	 * 		签名生成所需数据
	 * @return 签名
	 */
	public String getSign(MDataMap mDataMap);
	
	/**
	 * 回调接口返回给支付网关url
	 * @param payGate
	 * 		支付方式
	 * @return 网关回调url
	 */
	public String getReturnUrl(int payGate);
	
	/**
	 * 商户交易密钥
	 * @return 商户密钥
	 */
	public String getMerchantPwd();

}
