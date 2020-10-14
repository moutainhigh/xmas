package com.srnpr.xmassystem.invoke.ref;

import java.util.List;

import com.srnpr.xmassystem.invoke.ref.model.GiftVoucherInfo;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 还原礼金券
 * @author cc
 *
 */
public interface RollbackLjqRef {

	public static final String NAME = "bean_"+RollbackLjqRef.class.getName().replaceAll("\\.", "_");
	
	public RootResult reWriteGiftVoucherToLD(List<GiftVoucherInfo> list);
}
