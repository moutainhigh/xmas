package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.modelbean.PayTypeInfo;

/**
 * 支付类型信息
 */
public class PlusModelPayTypeInfo implements IPlusModel {

	private List<PayTypeInfo> typeInfoList = new ArrayList<PayTypeInfo>();

	public List<PayTypeInfo> getTypeInfoList() {
		return typeInfoList;
	}

	public void setTypeInfoList(List<PayTypeInfo> typeInfoList) {
		this.typeInfoList = typeInfoList;
	}
	
}
