package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 活动信息排除商品
 */
public class PlusModelEventExclude implements IPlusModel {

	private List<String> productCodeList = new ArrayList<String>();

	public List<String> getProductCodeList() {
		return productCodeList;
	}

	public void setProductCodeList(List<String> productCodeList) {
		this.productCodeList = productCodeList;
	}
	
}
