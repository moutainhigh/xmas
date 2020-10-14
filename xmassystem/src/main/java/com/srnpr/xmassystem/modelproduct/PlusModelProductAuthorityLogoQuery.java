package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询权威标志
 * 
 * @author ligj
 *
 */
public class PlusModelProductAuthorityLogoQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "商品编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelProductAuthorityLogoQuery(String code) {
		super();
		this.code = code;
	}
	
}
