package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;

public class PlusConfigWebTemplateCode extends ConfigTop{

	public EKvSchema getSchema() {
		return EKvSchema.WebTemplateCode;
	}

	public Class<?> getPlusClass() {
		return WebTempletePage.class;
	}

}
