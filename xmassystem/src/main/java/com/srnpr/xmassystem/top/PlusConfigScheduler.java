package com.srnpr.xmassystem.top;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.zapweb.webface.IKvSchedulerConfig;

public class PlusConfigScheduler implements IKvSchedulerConfig {

	public PlusConfigScheduler(EPlusScheduler scheduler) {
		this.schedulerName = scheduler;
	}

	private EPlusScheduler schedulerName;

	public String getExecType() {
		return schedulerName.toString();
	}

	public EPlusScheduler getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(EPlusScheduler schedulerName) {
		this.schedulerName = schedulerName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srnpr.zapweb.webface.IKvSchedulerConfig#getNoticeOnce()
	 */
	public int getNoticeOnce() {

		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srnpr.zapweb.webface.IKvSchedulerConfig#getExecJobLock()
	 */
	public int getExecJobLock() {

		return 600;
	}

}
