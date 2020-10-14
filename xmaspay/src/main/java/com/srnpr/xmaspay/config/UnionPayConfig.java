package com.srnpr.xmaspay.config;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 银联支付配置信息
 * @author pang_jhui
 *
 */
public class UnionPayConfig extends BaseClass implements IUnionPayConfig {

	@Override
	public String getClassPath() {
		
		String path = this.getClass().getResource("/").getPath();
		
		return path;
		
	}
	
	@Override
	public String getSignCertPath() {
		
		String path = bConfig("unionpay.signCert_path");
		
		path = getClassPath() + path;
		
		return path;
		
	}

	@Override
	public String getSignCertPwd() {
		
		String pwd = bConfig("unionpay.signCert_pwd");
		
		return pwd;
		
	}

	@Override
	public String getSignCertType() {
		
		String certType = bConfig("unionpay.signCert_type");
		
		return certType;
		
	}

	@Override
	public String getValidateCertDir() {
		
		String path = bConfig("unionpay.validateCert_dir");
		
		path = getClassPath() + path;
		
		return path;
		
	}

	@Override
	public String getEncryptCertPath() {
		
		String path = bConfig("unionpay.encryptCert_path");
		
		path = getClassPath() + path;
		
		return path;
	}

	@Override
	public boolean getSingleMode() {
		
		boolean mode = true;
		
		String modeStr = bConfig("unionpay.singleMode");
		
		if(StringUtils.isNotBlank(modeStr)){
			
			mode = Boolean.valueOf(modeStr);
			
		}		
		
		return mode;
		
	}

	@Override
	public String getVersion() {
		
		return bConfig("unionpay.version");
		
	}

	@Override
	public String getEncoding() {
		
		return bConfig("unionpay.encoding");
		
	}

	@Override
	public String getTnxType() {
		
		return bConfig("unionpay.tnxtype");
		
	}

	@Override
	public String getTxnSubType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBizType() {
		
		return bConfig("unionpay.bizType");
		
	}

	@Override
	public String getChannelType() {
		
		return bConfig("unionpay.channelType");
		
	}

	@Override
	public String getAccessType() {
		
		return bConfig("unionpay.accessType");
		
	}

	@Override
	public String getMerId() {
		
		return bConfig("unionpay.merId");
		
	}

	@Override
	public String getBackUrl() {
		
		return bConfig("unionpay.backUrl");
		
	}

	@Override
	public String getCurrencyCode() {
		
		return bConfig("unionpay.currencyCode");
		
	}

	@Override
	public String getRequestUrl() {
		
		return bConfig("unionpay.request_url");
	}
	
	

}
