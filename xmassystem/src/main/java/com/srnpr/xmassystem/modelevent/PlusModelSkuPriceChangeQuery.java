package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 查询SKU的调价记录
 */
public class PlusModelSkuPriceChangeQuery implements IPlusQuery {

	@ZapcomApi(value = "SKU编号", remark = "")
	private String code = "";

	public PlusModelSkuPriceChangeQuery(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
