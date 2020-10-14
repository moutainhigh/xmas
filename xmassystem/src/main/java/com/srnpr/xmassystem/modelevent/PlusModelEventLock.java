package com.srnpr.xmassystem.modelevent;

public class PlusModelEventLock {

	/**
	 * 锁定库存开始时间
	 */
	private String beginTime="";
	
	
	/*
	 * 锁定库存结束时间
	 */
	private String endTime="";
	
	/*
	 * 锁定库存数量
	 */
	private int lockNumber=0;
	
	/*
	 * 锁定来源编号
	 */
	private String lockSource="";

	/**
	 * @return the lockSource
	 */
	public String getLockSource() {
		return lockSource;
	}

	/**
	 * @param lockSource the lockSource to set
	 */
	public void setLockSource(String lockSource) {
		this.lockSource = lockSource;
	}

	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the lockNumber
	 */
	public int getLockNumber() {
		return lockNumber;
	}

	/**
	 * @param lockNumber the lockNumber to set
	 */
	public void setLockNumber(int lockNumber) {
		this.lockNumber = lockNumber;
	}
	
	
	
	
	
}
