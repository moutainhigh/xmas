package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询第三级行政地址编号
 * 
 * @author xiegj
 *
 */
public class PlusSaleProductModelQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "活动编号", require = 1)
	private String code = "CX10000000000000";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
