package com.srnpr.xmasorder.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车
*    xiegj
*/
public class ShoppingCartCacheInfo  {
	private List<ShoppingCartCache> caches = new ArrayList<ShoppingCartCache>();

	@ZapcomApi(value="用户选择的加价购活动商品")
	private  List<TeslaModelJJG>  JJGList= new ArrayList<TeslaModelJJG>();


	public List<TeslaModelJJG> getJJGList() {
		return JJGList;
	}

	public void setJJGList(List<TeslaModelJJG> jJGList) {
		JJGList = jJGList;
	}

	public List<ShoppingCartCache> getCaches() {
		return caches;
	}

	public void setCaches(List<ShoppingCartCache> caches) {
		this.caches = caches;
	}
	
}

