package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.helper.MoneyHelper;


/**   
*    
* 项目名称：   
* 类名称：ProductSkuInfoForApi   
* 类描述：   
* 创建人：李国杰
*
* @version    
*    
*/
public class PlusModelProductSkuInfo  {
    
	@ZapcomApi(value = "sku编码")
    private String skuCode  = ""  ;

	@ZapcomApi(value = "sku名称")
    private String skuName  = ""  ;

	@ZapcomApi(value = "sku规格")
    private String skuKey  = ""  ;
	
	@ZapcomApi(value = "sku规格型号文字")
    private String skuKeyValue  = ""  ;

	@ZapcomApi(value = "销售价")
	private BigDecimal sellPrice=BigDecimal.ZERO;

	@ZapcomApi(value = "市场价")
	private BigDecimal marketPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value = "成本价")
	private BigDecimal costPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value = "起订数量",remark="最少购买数，默认为1")
	private int miniOrder = 1;
	
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public BigDecimal getSellPrice() {
		return MoneyHelper.roundHalfUp(sellPrice);
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getMarketPrice() {
		return MoneyHelper.roundHalfUp(marketPrice);
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public int getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(int miniOrder) {
		this.miniOrder = miniOrder;
	}

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}

	public String getSkuKeyValue() {
		return skuKeyValue;
	}

	public void setSkuKeyValue(String skuKeyValue) {
		this.skuKeyValue = skuKeyValue;
	}
	
}

