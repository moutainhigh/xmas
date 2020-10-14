package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IAlipayConfig;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.RSA;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝配置信息
 * @author pang_jhui
 *
 */
public class AlipayConfig extends BaseClass implements IAlipayConfig {
	
	
	public String getPirvateKey(){
		
		return bConfig("xmaspay.alipay_private_key");
		
	}
	
	public String getPublicKey(){
		
		return bConfig("xmaspay.alipay_public_key");
		
	}
	
	public String getPartner(){
		
		return bConfig("xmaspay.alipay_merchant_partner");
		
	}
	
	/**
	 * 获取RSA签名算法
	 * @return 签名算法
	 */
	public String getSignRSAAlgorithm(){
		
		return bConfig("xmaspay.alipay_sign_rsa_algorithm");
		
	}
	

	public String getRequestCharset(){
		
		return bConfig("xmaspay.alipay_request_charset");
		
	}
	
	public String getSignType(){
		
		return bConfig("xmaspay.alipay_sign_type");
		
	}

	@Override
	public String getSign(MDataMap mDataMap) {
		
		String content = BeanComponent.getInstance().sortParam(Constants.SIGN_PARAM_SPLIT_AND, mDataMap);

		return RSA.sign(content, getPirvateKey(), getRequestCharset(), getSignRSAAlgorithm());

	}

	@Override
	public String getVersion() {
		
		return bConfig("xmaspay.alipay_version");
		
	}

	@Override
	public String getRequestUrl() {
		
		return bConfig("xmaspay.alipay_request_url");
	}

	@Override
	public String getVerifyKey() {
		
		return bConfig("xmaspay.alipay_verify_key");
	}
}
