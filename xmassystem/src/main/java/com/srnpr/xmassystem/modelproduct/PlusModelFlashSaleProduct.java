package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelFlashSaleProduct implements IPlusModel{
	
	@ZapcomApi(value = "商品列表", remark = "商品列表")
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();

	public List<Map<String, Object>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, Object>> items) {
		this.items = items;
	}

	
}
