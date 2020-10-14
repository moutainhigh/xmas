package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IWechatConfig;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.MD5;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信相关配置信息
 * @author pang_jhui
 *
 */
public class WechatConfig extends BaseClass implements IWechatConfig {

	@Override
	public String getMerchantAppId(String manageCode) {
		
		return bConfig("xmaspay.wechat_merchant_appid_"+manageCode);
	}

	@Override
	public String getMechantId(String manageCode) {
		
		return bConfig("xmaspay.wechat_merchant_id_"+manageCode);
	}

	@Override
	public String getSignKey(String manageCode) {
		
		return bConfig("xmaspay.wechat_sign_key_"+manageCode);
	}

	@Override
	public String getSign(String manageCode, MDataMap mDataMap) {
		
		String content = BeanComponent.getInstance().sortParam(Constants.SIGN_PARAM_SPLIT_AND, mDataMap);
		
		content = content + Constants.SIGN_PARAM_SPLIT_AND + "key=" + getSignKey(manageCode);

		return MD5.sign(content, "UTF-8").toUpperCase();
		
	}

}
