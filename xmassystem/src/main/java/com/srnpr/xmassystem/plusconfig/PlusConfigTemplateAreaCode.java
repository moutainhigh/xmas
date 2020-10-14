package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;

public class PlusConfigTemplateAreaCode extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.templateAreaCode;
	}

	public Class<?> getPlusClass() {
		return PlusModelTemplateAeraCode.class;
	}

}
