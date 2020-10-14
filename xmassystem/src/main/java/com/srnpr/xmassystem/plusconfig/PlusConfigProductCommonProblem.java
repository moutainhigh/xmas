package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelCommonProblems;

public class PlusConfigProductCommonProblem extends ConfigTop {

	public EKvSchema getSchema() {
		return EKvSchema.ProductCommonProblem;
	}

	public Class<?> getPlusClass() {
		return PlusModelCommonProblems.class;
	}

}
