package com.srnpr.xmaspay.config;

import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.MessageDigestUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * applePay配置信息
 * @author pangjh
 *
 */
public class ApplePayConfig extends BaseClass implements IApplePayConfig {

	@Override
	public String getMerchantId() {
		
		return bConfig("applepay.oid_partner");
	}

	@Override
	public String getSignType() {
		
		return bConfig("applepay.sign_type");
	}

	@Override
	public String getBusiType() {
		
		return bConfig("applepay.busi_partner");
	}

	@Override
	public String getNotifyUrl() {
		
		return bConfig("applepay.notify_url");
	}

	@Override
	public String getApMerchantId() {
		
		return bConfig("applepay.ap_merchant_id");
	}

	@Override
	public String getMd5Key() {
		
		return bConfig("applepay.key_md5");
	}

	@Override
	public String getSign(MDataMap mDataMap) {
		
		if(mDataMap.containsKey("sign")){
			
			mDataMap.remove("sign");
			
		}
		
		String signData = BeanComponent.getInstance().sortParam("&", mDataMap);
		
		signData = signData+"&key="+getMd5Key();
		
		return MessageDigestUtil.Md5Encode(signData, null);
	}

}
