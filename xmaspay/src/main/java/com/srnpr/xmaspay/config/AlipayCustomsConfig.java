package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.config.face.IAlipayCustomsConfig;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.MD5;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝报关配置信息
 * @author zhaojunling
 *
 */
public class AlipayCustomsConfig extends AlipayConfig implements IAlipayCustomsConfig {

	@Override
	public String getMethodCustoms() {
		return bConfig("xmaspay.alipay_method_customs");
	}

	@Override
	public String getSign(MDataMap mDataMap) {
		String content = BeanComponent.getInstance().sortParam(Constants.SIGN_PARAM_SPLIT_AND, mDataMap);
		return MD5.sign(content, getVerifyKey(), getRequestCharset());
	}
}
