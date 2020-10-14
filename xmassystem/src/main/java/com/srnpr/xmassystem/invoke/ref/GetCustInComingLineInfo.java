package com.srnpr.xmassystem.invoke.ref;

import com.srnpr.xmassystem.invoke.ref.model.GetCustInComingLineInfoResult;

/**
 * 
 *<p>Description:用户进线信息接口 <／p> 
 * @author zb
 * @date 2020年9月15日
 *
 */

public interface GetCustInComingLineInfo {

	public static final String NAME = "bean_"+GetCustInComingLineInfo.class.getName().replaceAll("\\.", "_");
	
	/**
	 * 获取用户进线信息
	 */
	public GetCustInComingLineInfoResult sendCouponMessageForInComingLineUser();
	

}
