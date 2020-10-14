package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 内购实体
 * @author zhouguohui
 *
 */
public class PlusModelEventPurchase implements IPlusModel{

	 List<PlusModelPurchase> listPurchase = new ArrayList<PlusModelPurchase>();

	/**
	 * @return the listPurchase
	 */
	public List<PlusModelPurchase> getListPurchase() {
		return listPurchase;
	}

	/**
	 * @param listPurchase the listPurchase to set
	 */
	public void setListPurchase(List<PlusModelPurchase> listPurchase) {
		this.listPurchase = listPurchase;
	}
	 
}
