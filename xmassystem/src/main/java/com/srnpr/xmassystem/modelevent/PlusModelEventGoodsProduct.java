package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;

/**
 * 缓存拼好货list实体对象
 * @author zhouguohui
 *
 */
public class PlusModelEventGoodsProduct implements IPlusModel{

	private List<PlusModelGoodsProduct> goodsProduct=new ArrayList<PlusModelGoodsProduct>();

	/**
	 * @return the goodsProduct
	 */
	public List<PlusModelGoodsProduct> getGoodsProduct() {
		return goodsProduct;
	}

	/**
	 * @param goodsProduct the goodsProduct to set
	 */
	public void setGoodsProduct(List<PlusModelGoodsProduct> goodsProduct) {
		this.goodsProduct = goodsProduct;
	}

	
	
}
