package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelCouponTypeInfoQuery extends QueryTop {

	@ZapcomApi(value = "优惠券类型编号", remark = "优惠券类型编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlusModelCouponTypeInfoQuery(String code) {
		super();
		this.code = code;
	}
	
}
