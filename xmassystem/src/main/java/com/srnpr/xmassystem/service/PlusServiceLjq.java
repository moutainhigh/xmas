package com.srnpr.xmassystem.service;

import java.util.List;

import com.srnpr.xmassystem.invoke.ref.RollbackLjqRef;
import com.srnpr.xmassystem.invoke.ref.model.GiftVoucherInfo;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 礼金券相关功能方法
 * @author cc
 *
 */
public class PlusServiceLjq {

	public RootResult reWriteGiftVoucherToLD(List<GiftVoucherInfo> list) {
		RollbackLjqRef beanRef = (RollbackLjqRef)BeansHelper.upBean(RollbackLjqRef.NAME);
		System.out.println("PlusServiceLjq().reWriteGiftVoucherToLD : reWriteLD.size() = " + list.size());
		return beanRef.reWriteGiftVoucherToLD(list);
	}
}
