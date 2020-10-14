package com.srnpr.xmassystem.invoke.ref;

import com.srnpr.xmassystem.invoke.ref.model.ValidateStockInput;
import com.srnpr.xmassystem.invoke.ref.model.ValidateStockResult;

/**
 * 校验库存
 */
public interface ValidateStockRef {

	public static final String NAME = "bean_"+ValidateStockRef.class.getName().replaceAll("\\.", "_");
	
	public ValidateStockResult stockCheck(ValidateStockInput input);
}
