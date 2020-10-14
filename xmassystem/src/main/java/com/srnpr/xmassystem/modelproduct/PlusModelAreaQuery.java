package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询第三级行政地址编号
 * 
 * @author xiegj
 *
 */
public class PlusModelAreaQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "商品编号", require = 1)
	private String code = "areaCode";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
