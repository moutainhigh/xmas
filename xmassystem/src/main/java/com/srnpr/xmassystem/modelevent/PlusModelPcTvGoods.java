package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 节目单商品编号列表
 */
public class PlusModelPcTvGoods implements IPlusModel {

	private List<String> goods = new ArrayList<String>();
	
	public List<String> getGoods() {
		return goods;
	}

	public void setGoods(List<String> goods) {
		this.goods = goods;
	}
}
