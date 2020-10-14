package com.srnpr.xmassystem.service;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.support.PlusSupportSeller;

/**
 * 商户
 *
 * @author jlin
 *
 */
public class PlusServiceSeller {
	
	private static PlusSupportSeller supportSeller = new PlusSupportSeller();

	/**
	 * 是否跨境商户 4497478100050002|4497478100050003
	 *
	 * @param small_seller_code
	 * @return
	 */
	public boolean isKJSeller(String small_seller_code) {

		PlusModelSellerInfo plusModelSellerInfo = new LoadSellerInfo().upInfoByCode(new PlusModelSellerQuery(small_seller_code));

		if (plusModelSellerInfo != null) {

			if (plusModelSellerInfo.getUc_seller_type().equals("4497478100050002") || plusModelSellerInfo.getUc_seller_type().equals("4497478100050003")) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 根据商户的smallSellerCode返回该商户的类型.
	 * @param smallSellerCode
	 * @author zht
	 * @return
	 */
	public String getSellerType(String smallSellerCode) {
		return supportSeller.getSellerType(smallSellerCode);
	}

}
