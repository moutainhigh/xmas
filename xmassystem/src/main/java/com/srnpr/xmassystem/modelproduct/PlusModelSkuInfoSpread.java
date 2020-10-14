package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * sku扩展信息
 * @author shiyz
 *
 */
public class PlusModelSkuInfoSpread implements IPlusModel, IPlusModelRefresh {

	/** 增加这个属性的值可在加载缓存时自动刷新旧版缓存数据 */
	public static final int _VERSION = 1;
	/** 版本号 */
	@ZapcomApi(value = "数据版本号")
	private int v = 0;
	
	public PlusModelSkuInfoSpread() {
		this.v = _VERSION;
	}

	@Override
	public boolean isRefresh() {
		return v < _VERSION;
	}
	
	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}



	@ZapcomApi(value="产品编号")
	private String productCode = "";
	
	@ZapcomApi(value="入库类型 00四地入库")
    private String prchType="";
    
	@ZapcomApi(value="入库地")
    private String siteNo = "";
	
	@ZapcomApi(value="供应商编号")
    private String dlrId = "";
	
	/**
	 * 跨境通历史数据：(0:直邮，1:自贸) 
	 * 其他：0为保税贸易，1为海外直邮，2为一般贸易
	 */
	@ZapcomApi(value="贸易类型")
    private String productTradeType = "";
	
	@ZapcomApi(value="配送仓库类别", remark="4497471600430001商家配送、4497471600430002家有配送")
	private String deliveryStoreType = "";
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPrchType() {
		return prchType;
	}

	public void setPrchType(String prchType) {
		this.prchType = prchType;
	}

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	public String getDlrId() {
		return dlrId;
	}

	public void setDlrId(String dlrId) {
		this.dlrId = dlrId;
	}

	public String getProductTradeType() {
		return productTradeType;
	}

	public void setProductTradeType(String productTradeType) {
		this.productTradeType = productTradeType;
	}

	public String getDeliveryStoreType() {
		return deliveryStoreType;
	}

	public void setDeliveryStoreType(String deliveryStoreType) {
		this.deliveryStoreType = deliveryStoreType;
	}

	
}
