package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelProductAuthorityLogoQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigProductAuthorityLogo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 加载权威标识信息
 * @author ligj
 *
 */
public class LoadProductAuthorityLogo extends LoadTop<PlusModelAuthorityLogos, PlusModelProductAuthorityLogoQuery> {

	public PlusModelAuthorityLogos topInitInfo(PlusModelProductAuthorityLogoQuery query) {
		return new PlusSupportProduct().initProductAuthorityLogoFromDb(query.getCode());
	}

	private final static PlusConfigProductAuthorityLogo PLUS_CONFIG = new PlusConfigProductAuthorityLogo();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
