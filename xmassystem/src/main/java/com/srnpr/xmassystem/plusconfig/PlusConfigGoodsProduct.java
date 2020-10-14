package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelEventGoodsProduct;

public class PlusConfigGoodsProduct  extends ConfigTop{

	
	public EKvSchema getSchema() {
		
		return EKvSchema.GoodsProduct;
	}

	public Class<?> getPlusClass() {
		// TODO Auto-generated method stub
		return PlusModelEventGoodsProduct.class;
	}
}
