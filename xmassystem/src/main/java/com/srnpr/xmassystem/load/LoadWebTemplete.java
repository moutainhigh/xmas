package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelwebtemplete.PlusModelWebTempleteQuery;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;
import com.srnpr.xmassystem.plusconfig.PlusConfigWebTemplateCode;
import com.srnpr.xmassystem.support.PlusSupportWebTemplete;
import com.srnpr.xmassystem.top.LoadTop;

public class LoadWebTemplete extends LoadTop<WebTempletePage,PlusModelWebTempleteQuery>{

	@Override
	public WebTempletePage topInitInfo(PlusModelWebTempleteQuery tQuery) {
		
		return new PlusSupportWebTemplete().initWebTempletePageInfoFromDb(tQuery.getCode());
	}
	
	private final static PlusConfigWebTemplateCode PLUS_CONFIG = new PlusConfigWebTemplateCode();

	@Override
	public IPlusConfig topConfig() {
		
		return PLUS_CONFIG;
	}

}
