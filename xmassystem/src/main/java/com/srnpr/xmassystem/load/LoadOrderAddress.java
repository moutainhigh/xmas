package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelOrderAddress;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigOrderAddress;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

public class LoadOrderAddress extends LoadTop<PlusModelOrderAddress, PlusModelSkuQuery> {

	@Override
	public PlusModelOrderAddress topInitInfo(PlusModelSkuQuery query) {
		
		    String addressCode = query.getCode();

			PlusModelOrderAddress plusAddress = new PlusSupportProduct().initAdderssFrom(addressCode);
			
			return plusAddress;
	}

	
	private final static PlusConfigOrderAddress PLUS_CONFIG = new PlusConfigOrderAddress();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}


}
