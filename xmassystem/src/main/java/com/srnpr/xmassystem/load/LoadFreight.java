package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelFreight;
import com.srnpr.xmassystem.modelproduct.PlusModelFreightQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigFreight;
import com.srnpr.xmassystem.support.PlusSupportFreight;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 加载运费模板信息
 * @author jlin
 *
 */
public class LoadFreight extends LoadTop<PlusModelFreight, PlusModelFreightQuery> {

	public PlusModelFreight topInitInfo(PlusModelFreightQuery query) {
		
		String transportTemplateUid = query.getCode();

		PlusModelFreight plusModelFreight = new PlusSupportFreight().initFreightFromDb(transportTemplateUid);

		return plusModelFreight;

	}

	private final static PlusConfigFreight PLUS_CONFIG = new PlusConfigFreight();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
