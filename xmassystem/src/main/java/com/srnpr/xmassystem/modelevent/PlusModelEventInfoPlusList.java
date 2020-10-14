package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 活动信息
 */
public class PlusModelEventInfoPlusList extends IPlusAbstractModel implements IPlusModel {

	@Override
	public int getCurrentVersion() {
		return 1;
	}
	
	private List<PlusModelEventInfoPlus> plusEventInfoList = new ArrayList<PlusModelEventInfoPlus>();

	public List<PlusModelEventInfoPlus> getPlusEventInfoList() {
		return plusEventInfoList;
	}

	public void setPlusEventInfoList(List<PlusModelEventInfoPlus> plusEventInfoList) {
		this.plusEventInfoList = plusEventInfoList;
	}
	
}
