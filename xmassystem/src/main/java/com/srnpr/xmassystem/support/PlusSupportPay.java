package com.srnpr.xmassystem.support;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;

public class PlusSupportPay extends BaseClass {

	/**
	 * 设置支付方式
	 * 
	 * @param sOrderCode
	 * @param sPayFrom
	 */
	public void fixPayFrom(String sOrderCode, String sPayFrom) {

		XmasKv.upFactory(EKvSchema.PayFrom).setex(sOrderCode, 3600*48, sPayFrom);

	}

	/**
	 * 读取支付方式 如果没有设置过 默认会返回空 返回空的时候将支付宝设置为默认支付
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public String upPayFrom(String sOrderCode) {

		return XmasKv.upFactory(EKvSchema.PayFrom).get(sOrderCode);

	}

}
