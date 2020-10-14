package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于运费模板查询
 * 
 * @author jlin
 *
 */
public class PlusModelFreightQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "运费模板uid", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelFreightQuery(String code) {
		super();
		this.code = code;
	}
	
}
