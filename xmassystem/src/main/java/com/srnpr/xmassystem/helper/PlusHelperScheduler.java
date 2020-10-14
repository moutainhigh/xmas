package com.srnpr.xmassystem.helper;

import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapdata.helper.KvHelper;

public class PlusHelperScheduler {

	/**
	 * 发送一条执行任务到队列中
	 * 
	 * @param ePlusScheduler
	 * @param sField
	 * @param sValue
	 * @return
	 */
	public static boolean sendSchedler(EPlusScheduler ePlusScheduler, String sField,
			Object oValue) {
		return KvHelper.sendSchedulerInfo(ePlusScheduler.toString(), sField,
				GsonHelper.toJson(oValue));
	}

}
