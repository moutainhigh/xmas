package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSeller;
import com.srnpr.xmassystem.support.PlusSupportSeller;
import com.srnpr.xmassystem.top.LoadTop;

/**
 * 
 * 加载商户信息
 * 
 * @author jlin
 *
 */
public class LoadSellerInfo extends LoadTop<PlusModelSellerInfo, PlusModelSellerQuery> {

	public PlusModelSellerInfo topInitInfo(PlusModelSellerQuery query) {

		String small_seller_code = query.getCode();

		return new PlusSupportSeller().initSellerInfoFromDB(small_seller_code);
		
	}

	private final static PlusConfigSeller PLUS_CONFIG = new PlusConfigSeller();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
