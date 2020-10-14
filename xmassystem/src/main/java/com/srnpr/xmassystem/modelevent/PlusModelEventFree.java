package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

public class PlusModelEventFree implements IPlusModel{
	
	private List<PlusModelFreeShipping> eventFree=new ArrayList<PlusModelFreeShipping>();

	/**
	 * @return the eventFree
	 */
	public List<PlusModelFreeShipping> getEventFree() {
		return eventFree;
	}

	/**
	 * @param eventFree the eventFree to set
	 */
	public void setEventFree(List<PlusModelFreeShipping> eventFree) {
		this.eventFree = eventFree;
	}
	
}
