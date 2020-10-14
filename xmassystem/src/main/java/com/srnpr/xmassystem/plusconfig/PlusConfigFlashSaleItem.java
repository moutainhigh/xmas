package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;

public class PlusConfigFlashSaleItem extends ConfigTop{

	@Override
	public EKvSchema getSchema() {
		return EKvSchema.FlashSaleProductList;
	}

	@Override
	public Class<?> getPlusClass() {
		return PlusModelFlashSaleProduct.class;
	}
	
	public int getExpireSecond()
	{
		return 300;//缓存时间一分钟刷新一次
	}

}
