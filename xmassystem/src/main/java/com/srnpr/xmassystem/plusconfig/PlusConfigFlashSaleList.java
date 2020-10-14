package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelevent.PlusModelFlashSaleList;

public class PlusConfigFlashSaleList extends ConfigTop {
	@Override
	public EKvSchema getSchema() {
		return EKvSchema.FlashSaleList;
	}

	@Override
	public Class<?> getPlusClass() {
		return PlusModelFlashSaleList.class;
	}

	public int getExpireSecond()
	{
		return 300;//缓存时间一分钟刷新一次
	}

}
