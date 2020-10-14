package com.srnpr.xmasorder.channel.model;

import com.srnpr.xmasorder.channel.enumer.EPorscheExec;

public class PorscheModelStatus {

	private EPorscheExec execStep = EPorscheExec.Undefined;

	public EPorscheExec getExecStep() {
		return execStep;
	}

	public void setExecStep(EPorscheExec execStep) {
		this.execStep = execStep;
	}


}
