package com.srnpr.xmasorder.model;

import com.srnpr.xmasorder.enumer.ETeslaExec;

public class TeslaModelStatus {

	private ETeslaExec execStep = ETeslaExec.Undefined;

	public ETeslaExec getExecStep() {
		return execStep;
	}

	public void setExecStep(ETeslaExec execStep) {
		this.execStep = execStep;
	}

}
