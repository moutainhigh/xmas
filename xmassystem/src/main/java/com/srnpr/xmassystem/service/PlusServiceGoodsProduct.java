package com.srnpr.xmassystem.service;

import com.srnpr.xmassystem.modelevent.PlusModelEventGoodsProduct;
import com.srnpr.xmassystem.support.PlusSupportEvent;

/**
 * 拼好货对外提供的方法
 * @author zhouguohui
 *
 */
public class PlusServiceGoodsProduct {
	
	/***
	 * 过滤拼好货活动
	 * @param sellerCode  系统编号
	 * @return
	 */
	public PlusModelEventGoodsProduct getEventGoodsProduct(String sellerCode){
		if(sellerCode==null){
			return null;
		}
		
		PlusModelEventGoodsProduct goodsProduct = new PlusSupportEvent().getGoodsProduct(sellerCode);
		return goodsProduct;
	  }
	
	
	
}
