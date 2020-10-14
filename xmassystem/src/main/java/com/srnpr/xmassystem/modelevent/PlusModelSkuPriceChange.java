package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 商品调价
 */
public class PlusModelSkuPriceChange implements IPlusModel {

	/**
	 * 商品调价记录列表，根据开始时间倒序排列
	 */
	private List<PlusModelSkuPriceFlow> itemList = new ArrayList<PlusModelSkuPriceFlow>();
	
	public List<PlusModelSkuPriceFlow> getItemList() {
		return itemList;
	}

	public void setItemList(List<PlusModelSkuPriceFlow> itemList) {
		this.itemList = itemList;
	}
}
