package com.srnpr.xmassystem.invoke.ref;

import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 用户积分、储值金、暂存款、惠币查询、使用
 */
public interface CustRelAmtRef {

	public static final String NAME = "bean_"+CustRelAmtRef.class.getName().replaceAll("\\.", "_");
	
	/**
	 * 查询
	 * @param custId
	 * @return
	 */
	public GetCustAmtResult getCustAmt(String custId);
	
	/**
	 * 占用、使用、取消、退货’
	 * @return
	 */
	public RootResult updateCustAmt(UpdateCustAmtInput input);
}
