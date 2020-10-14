package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelBlackList;

public class PlusConfigBlactList extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.Black;
	}

	public Class<?> getPlusClass() {
		return PlusModelBlackList.class;
	}

}
