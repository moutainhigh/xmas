package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 查询节目单商品编号
 */
public class PlusModelPcTvGoodsQuery implements IPlusQuery {

	@ZapcomApi(value = "节目单播出日期", remark = "格式如：2018-08-12")
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
