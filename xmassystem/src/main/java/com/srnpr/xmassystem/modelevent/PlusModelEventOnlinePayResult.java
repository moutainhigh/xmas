package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 活动信息
 * 
 * @author srnpr
 *
 */
public class PlusModelEventOnlinePayResult implements IPlusModel {

	private List<PlusModelEventOnlinePay> list = new ArrayList<PlusModelEventOnlinePay>();

	public List<PlusModelEventOnlinePay> getList() {
		return list;
	}

	public void setList(List<PlusModelEventOnlinePay> list) {
		this.list = list;
	}
	
}
