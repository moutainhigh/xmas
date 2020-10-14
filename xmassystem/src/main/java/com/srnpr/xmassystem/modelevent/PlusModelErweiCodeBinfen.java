package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/***
 * 缤纷扫码购明细
 */
public class PlusModelErweiCodeBinfen extends IPlusAbstractModel implements IPlusModel {

	/** 当前版本号 */
	public static final int _VERSION = 0;
	
	@Override
	protected int getCurrentVersion() {
		return _VERSION;
	}

	// 商品编号
	private String productCode = "";
	// 通路编号
	private String mclassId = "";

	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getMclassId() {
		return mclassId;
	}
	public void setMclassId(String mclassId) {
		this.mclassId = mclassId;
	}
	
}
