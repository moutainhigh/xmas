package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelBlackList;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigBlactList;
import com.srnpr.xmassystem.support.PlusSupportBlacklist;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 黑名单信息
 * @author xiegj
 *
 */
public class LoadBlackList extends LoadTop<PlusModelBlackList, PlusModelProductQuery> {

	public PlusModelBlackList topInitInfo(PlusModelProductQuery query) {
		
		String manageCode = "all";

		PlusModelBlackList productInfo = new PlusSupportBlacklist().initBlackListFromDb(manageCode);

		return productInfo;

	}

	private final static PlusConfigBlactList PLUS_CONFIG = new PlusConfigBlactList();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
