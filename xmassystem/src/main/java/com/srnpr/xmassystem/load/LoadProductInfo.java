package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigProduct;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 加载商品的信息
 * @author jlin
 *
 */
public class LoadProductInfo extends LoadTop<PlusModelProductInfo, PlusModelProductQuery> {

	public PlusModelProductInfo topInitInfo(PlusModelProductQuery query) {
		
		String productCode = query.getCode();

		PlusModelProductInfo productInfo = new PlusSupportProduct().initProductInfoFromDb(productCode);

		return productInfo;

	}

	private final static PlusConfigProduct PLUS_CONFIG = new PlusConfigProduct();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
