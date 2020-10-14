package com.srnpr.xmassystem.support;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.websupport.QrcodeSupport;

public class PlusSupportSystem {

	/**
	 * 获取二维码链接
	 * 
	 * @param sText
	 *            地址
	 * @param iSize
	 *            大小
	 * @return
	 */
	public String upQrCode(String sText, int iSize) {
		String sKey = String.valueOf(iSize) + WebConst.CONST_SPLIT_DOWN + sText;

		String sLink = XmasKv.upFactory(EKvSchema.Qrcode).get(sKey);

		if (StringUtils.isBlank(sLink)) {

			sLink = new QrcodeSupport().upQrcdoeLink(sText, iSize);
			XmasKv.upFactory(EKvSchema.Qrcode).set(sKey, sLink);
		}
		return sLink;

	}

}
