package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 *商品销量
 * @author ligj
 *
 */
public class PlusModelProductSales implements IPlusModel {

	@ZapcomApi(value = "近30天虚拟销量")
	Integer fictitionSales30 = 0;
	
	@ZapcomApi(value = "虚拟销量基数")
	Integer fictition = 0;

	public Integer getFictitionSales30() {
		return fictitionSales30;
	}

	public void setFictitionSales30(Integer fictitionSales30) {
		this.fictitionSales30 = fictitionSales30;
	}

	public Integer getFictition() {
		return fictition;
	}

	public void setFictition(Integer fictition) {
		this.fictition = fictition;
	}
}
