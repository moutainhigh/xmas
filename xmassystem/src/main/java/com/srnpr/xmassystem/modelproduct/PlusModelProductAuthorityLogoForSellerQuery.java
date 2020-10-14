package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * @description: 用于查询权威标志
 *
 * @author Yangcl
 * @date 2017年3月3日 下午2:23:44 
 * @version 1.0.0
 */
public class PlusModelProductAuthorityLogoForSellerQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "商品编号", require = 1)
	private String productCode = "";
	

	
	public String getCode() {
		return productCode;
	}

	public void setCode(String code) {
		this.productCode = code;
	}

	public PlusModelProductAuthorityLogoForSellerQuery(String productCode) {
		super();
		this.productCode = productCode;
	}
	
}
