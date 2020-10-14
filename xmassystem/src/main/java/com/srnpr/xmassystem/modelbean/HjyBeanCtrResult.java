package com.srnpr.xmassystem.modelbean;

import com.srnpr.zapcom.topapi.RootResult;

/**
 * 操作惠豆  调用用户中心操作惠豆返回参数
 * @author fq
 *
 */
public class HjyBeanCtrResult extends RootResult{

	/*
	 * 积分订单编号
	 */
	private String centerserialno = "";
	

	public String getCenterserialno() {
		return centerserialno;
	}

	public void setCenterserialno(String centerserialno) {
		this.centerserialno = centerserialno;
	}
	
	
}
