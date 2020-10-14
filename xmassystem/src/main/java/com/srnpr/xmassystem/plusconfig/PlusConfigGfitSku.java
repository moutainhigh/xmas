package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;

public class PlusConfigGfitSku extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Gift;
	}

	public Class<?> getPlusClass() {
		return PlusModelGitfSkuInfoList.class;
	}
	public int getExpireSecond()
	{
		//默认设置30天过期时间
		return 3600;
	}
}
