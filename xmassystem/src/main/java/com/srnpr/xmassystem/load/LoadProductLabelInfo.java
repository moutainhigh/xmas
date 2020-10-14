package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabelsQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigProductLabels;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 获取商品标签信息
 * @author lgj
 *
 */
public class LoadProductLabelInfo extends LoadTop<PlusModelProductLabel, PlusModelProductLabelsQuery> {

	public PlusModelProductLabel topInitInfo(PlusModelProductLabelsQuery query) {
		
		String labelsCode = query.getCode();
		PlusModelProductLabel productLabel = new PlusModelProductLabel();
		productLabel = new PlusSupportProduct().getProductLabelInfo(labelsCode);
		return productLabel;
	}

	private final static PlusConfigProductLabels PLUS_CONFIG = new PlusConfigProductLabels();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
