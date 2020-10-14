package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class PlusModelSkuResult extends RootResultWeb {

	@ZapcomApi(value = "SKU信息", remark = "根据SKU编号返回的结果集")
	private List<PlusModelSkuInfo> skus = new ArrayList<PlusModelSkuInfo>();

	public List<PlusModelSkuInfo> getSkus() {
		return skus;
	}

	public void setSkus(List<PlusModelSkuInfo> skus) {
		this.skus = skus;
	}
	

	
	

}
