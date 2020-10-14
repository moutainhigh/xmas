package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessageQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigHomeScrollMessage;
import com.srnpr.xmassystem.support.PlusSupportHomeScrollMessage;
import com.srnpr.xmassystem.top.LoadTop;

/**
 * 加载首页滚动消息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月22日
 */
public class LoadHomeScrollMessage extends LoadTop<PlusModelHomeScrollMessage, PlusModelHomeScrollMessageQuery> {

	public PlusModelHomeScrollMessage topInitInfo(PlusModelHomeScrollMessageQuery query) {
		
		PlusModelHomeScrollMessage plusModel = new PlusSupportHomeScrollMessage().initHomeScrollMessageFromDb();

		return plusModel;

	}

	private final static PlusConfigHomeScrollMessage PLUS_CONFIG = new PlusConfigHomeScrollMessage();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
