package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigTemplateAreaCode;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;


/** 
* @ClassName: LoadTemplateAreaCode 
* @Description: 模板对应的城市
* @author 张海生
* @date 2015-12-22 下午3:32:26 
*  
*/
public class LoadTemplateAreaCode extends LoadTop<PlusModelTemplateAeraCode, PlusModelTemplateAreaQuery> {

	public PlusModelTemplateAeraCode topInitInfo(PlusModelTemplateAreaQuery query) {
		
		return new PlusSupportProduct().initTemplateAreaCode(query.getCode());

	}

	private final static PlusConfigTemplateAreaCode PLUS_CONFIG = new PlusConfigTemplateAreaCode();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
