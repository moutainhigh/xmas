package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询商户信息
 * 
 * @author jlin
 *
 */
public class PlusModelSellerQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "商户编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelSellerQuery(String code) {
		super();
		this.code = code;
	}
	
}
