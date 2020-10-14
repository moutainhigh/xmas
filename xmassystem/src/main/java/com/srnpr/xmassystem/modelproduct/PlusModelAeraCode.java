package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 *第三级行政地址编号
 * @author xiegj
 *
 */
public class PlusModelAeraCode implements IPlusModel {

	List<String> areaCodes = new ArrayList<String>();

	public List<String> getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(List<String> areaCodes) {
		this.areaCodes = areaCodes;
	}
	
}
