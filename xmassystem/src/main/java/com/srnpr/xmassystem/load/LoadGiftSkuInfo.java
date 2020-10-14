package com.srnpr.xmassystem.load;


import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigGfitSku;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

public class LoadGiftSkuInfo extends LoadTop<PlusModelGitfSkuInfoList, PlusModelSkuQuery> {

	public PlusModelGitfSkuInfoList topInitInfo(PlusModelSkuQuery query) {
		
		    String productCode = query.getCode();

		    PlusModelGitfSkuInfoList plusSku = new PlusSupportProduct().getProductGiftsDetailList(productCode);
			
			return plusSku;
	}

	private final static PlusConfigGfitSku PLUS_CONFIG = new PlusConfigGfitSku();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
