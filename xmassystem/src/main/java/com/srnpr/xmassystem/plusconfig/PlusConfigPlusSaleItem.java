package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;

public class PlusConfigPlusSaleItem extends ConfigTop{

	@Override
	public EKvSchema getSchema() {
		return EKvSchema.PlusSaleProductList;
	}

	@Override
	public Class<?> getPlusClass() {
		return PlusModelFlashSaleProduct.class;
	}
	
}
