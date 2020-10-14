package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 商户的报关配置信息
 * @author zhaojunling
 *
 */
public class MerchantCustomsConfig extends BaseClass implements IPayConfig {

	/**
	 * 获取商户的报关海关编号
	 * @param smallSellerCode
	 * @return
	 */
	public String getCustomsCode(String smallSellerCode) {
		return bConfig("xmaspay.customs_code_"+smallSellerCode);
	}

	/**
	 * 获取商户的海关备案编号
	 * @param smallSellerCode
	 * @return
	 */
	public String getMerchantCustomsCode(String smallSellerCode){
		return bConfig("xmaspay.customs_mchcode_"+smallSellerCode);
	}
	
	/**
	 * 获取商户的海关备案名称
	 * @param smallSellerCode
	 * @return
	 */
	public String getMerchantCustomsName(String smallSellerCode){
		return bConfig("xmaspay.customs_mchname_"+smallSellerCode);
	}

	/**
	 * 获取商户是否启用了海关推送
	 * @param smallSellerCode
	 * @return
	 */
	public boolean getCustomsEnabled(String smallSellerCode){
		return Boolean.parseBoolean(bConfig("xmaspay.customs_enabled_"+smallSellerCode));
	}
}
