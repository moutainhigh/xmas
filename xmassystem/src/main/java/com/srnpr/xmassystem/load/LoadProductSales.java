package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;
import com.srnpr.xmassystem.plusconfig.PlusConfigProductSales;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 获取商品近30天的销量
 * @author lgj
 *
 */
public class LoadProductSales extends LoadTop<PlusModelProductSales, PlusModelProductQuery> {

	public PlusModelProductSales topInitInfo(PlusModelProductQuery query) {
		
		String productCode = query.getCode();
		PlusModelProductSales productSales = new PlusModelProductSales();
		productSales = new PlusSupportProduct().getProductSales(productCode);
		return productSales;
	}

	private final static PlusConfigProductSales PLUS_CONFIG = new PlusConfigProductSales();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
