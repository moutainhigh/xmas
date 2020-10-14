package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询商品信息
 * 
 * @author jlin
 *
 */
public class PlusModelProductQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "商品编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelProductQuery(String code) {
		super();
		this.code = code;
	}
	
}
