package com.srnpr.xmassystem.load;


import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigEvent;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.top.LoadTopMain;

/**
 * 加载活动信息
 * 
 * @author srnpr
 *
 */
public class LoadEventInfo extends LoadTopMain<PlusModelEventInfo, PlusModelEventQuery> {

	static PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
	
	public PlusModelEventInfo topInitInfo(PlusModelEventQuery q) {

		PlusModelEventInfo plusModelEventInfo = new PlusModelEventInfo();

		plusSupportEvent.upEventInfoFromDB(plusModelEventInfo, q.getCode());

		return plusModelEventInfo;
	}

	private final static PlusConfigEvent PLUS_CONFIG = new PlusConfigEvent();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

	@Override
	public PlusModelEventInfo topInitInfoMain(PlusModelEventQuery q) {

		PlusModelEventInfo plusModelEventInfo = new PlusModelEventInfo();
		plusSupportEvent.upEventInfoFromDB(plusModelEventInfo, q.getCode());

		return plusModelEventInfo;
	}

}
