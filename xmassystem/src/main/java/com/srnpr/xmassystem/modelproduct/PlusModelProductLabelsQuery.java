package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询标签信息
 * 
 * @author lgj
 *
 */
public class PlusModelProductLabelsQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "标签编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelProductLabelsQuery(String code) {
		super();
		this.code = code;
	}
	
}
