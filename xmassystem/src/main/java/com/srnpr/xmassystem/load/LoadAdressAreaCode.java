package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelAreaQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigAreaCode;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 加载地址第三级行政编号信息
 * @author xiegj
 *
 */
public class LoadAdressAreaCode extends LoadTop<PlusModelAeraCode, PlusModelAreaQuery> {

	public PlusModelAeraCode topInitInfo(PlusModelAreaQuery query) {
		
		return new PlusSupportProduct().initAreaCodeInfosFromDb(query.getCode());

	}

	private final static PlusConfigAreaCode PLUS_CONFIG = new PlusConfigAreaCode();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
