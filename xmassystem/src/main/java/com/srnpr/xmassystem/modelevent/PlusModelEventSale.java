package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 全场销售类活动
 * @author srnpr
 *
 */
public class PlusModelEventSale implements IPlusModel {

	
	private List<PlusModelEventFull> eventFulls=new ArrayList<PlusModelEventFull>();

	public List<PlusModelEventFull> getEventFulls() {
		return eventFulls;
	}

	public void setEventFulls(List<PlusModelEventFull> eventFulls) {
		this.eventFulls = eventFulls;
	}
}
