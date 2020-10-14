package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 会员日折扣实体
 */
public class PlusModelEventVipDiscount extends IPlusAbstractModel implements IPlusModel {

	List<PlusModelVipDiscount> listVipDiscount = new ArrayList<PlusModelVipDiscount>();
	
	// 活动编号对应的排除直播品日期列表
	Map<String,List<String>> excludeDayMap = new HashMap<String, List<String>>();

	public List<PlusModelVipDiscount> getListVipDiscount() {
		return listVipDiscount;
	}

	public void setListVipDiscount(List<PlusModelVipDiscount> listVipDiscount) {
		this.listVipDiscount = listVipDiscount;
	}

	public Map<String, List<String>> getExcludeDayMap() {
		return excludeDayMap;
	}

	public void setExcludeDayMap(Map<String, List<String>> excludeDayMap) {
		this.excludeDayMap = excludeDayMap;
	}

	@Override
	public int getCurrentVersion() {
		return 1;
	}
	
}
