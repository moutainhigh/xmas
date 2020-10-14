package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusProductModelQuery implements IPlusQuery {

	@ZapcomApi(value = "活动编号", remark = "秒杀活动编号", require = 1)
	private String code = "";
	@Override
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
