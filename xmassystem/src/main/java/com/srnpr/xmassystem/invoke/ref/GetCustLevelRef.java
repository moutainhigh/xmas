package com.srnpr.xmassystem.invoke.ref;

import com.srnpr.xmassystem.invoke.ref.model.GetCustLevelResult;

/**
 * 查询用户等级
 */
public interface GetCustLevelRef {

	public static final String NAME = "bean_"+GetCustLevelRef.class.getName().replaceAll("\\.", "_");
	
	GetCustLevelResult getCustLevel(String phone);
}
