package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSkuSpread;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;
/**
 * 缓存sku扩展信息
 * @author shiyz
 *
 */
public class LoadSkuInfoSpread extends LoadTop<PlusModelSkuInfoSpread, PlusModelSkuQuery> {

	public PlusModelSkuInfoSpread topInitInfo(PlusModelSkuQuery query) {
		
        String productCode = query.getCode();

		PlusModelSkuInfoSpread plusSku = new PlusSupportProduct().initBaseInfoFromSP(productCode);
		
		return plusSku;
		
	}
	
	
	private final static PlusConfigSkuSpread PLUS_CONFIG = new PlusConfigSkuSpread();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
