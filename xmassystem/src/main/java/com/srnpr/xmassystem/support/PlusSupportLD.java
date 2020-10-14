package com.srnpr.xmassystem.support;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;

public class PlusSupportLD  extends BaseClass {

	/**
	 * 设置是否同步LD订单
	 * 
	 */
	public void fixSyncLdOrder(String isSyncLdOrder) {

		XmasKv.upFactory(EKvSchema.IsSyncLdOrder).set("IsSyncLdOrder", isSyncLdOrder);

	}

	/**
	 * 读取是否同步LD订单  
	 * 
	 */
	public String upSyncLdOrder() {

		return XmasKv.upFactory(EKvSchema.IsSyncLdOrder).get("IsSyncLdOrder");

	}
}
