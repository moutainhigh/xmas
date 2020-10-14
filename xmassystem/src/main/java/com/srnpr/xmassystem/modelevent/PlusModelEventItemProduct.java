package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.enumer.EEventPriceType;
import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelEventItemProduct extends IPlusAbstractModel implements IPlusModel {
	
	/** 当前版本号 */
	public static final int _VERSION = 1;
	
	@Override
	public int getCurrentVersion() {
		return _VERSION;
	}

	@ZapcomApi(value = "明细编号")
	private String itemCode = "";
	@ZapcomApi(value = "SKU编号")
	private String skuCode = "";

	@ZapcomApi(value = "商品编号")
	private String productCode = "";

	@ZapcomApi(value = "价格类型")
	private EEventPriceType priceType = EEventPriceType.Base;
	@ZapcomApi(value = "活动编号")
	private String eventCode = "";

	@ZapcomApi(value = "销售库存", remark = "请注意该数值只是该优惠活动的最开始设置的库存，如果调用该活动当前的库存数量请使用其他方法")
	private long salesStock = 0;

	@ZapcomApi(value = "活动价")
	private BigDecimal priceEvent = BigDecimal.ZERO;

	@ZapcomApi(value = "初始优惠价", remark = "该字段仅用于展示最开始的那个优惠价格，用于各种计算的初始值")
	private BigDecimal priceStart = BigDecimal.ZERO;

	@ZapcomApi(value = "阶梯价规则")
	private List<PlusModelEventPriceStep> priceSteps = new ArrayList<PlusModelEventPriceStep>();

	@ZapcomApi(value = "销售限制")
	private List<PlusModelEventSellScope> sellScopes = new ArrayList<PlusModelEventSellScope>();

	@ZapcomApi(value = "扩展规则限制")
	private List<PlusModelEventRuleExtend> ruleExtends = new ArrayList<PlusModelEventRuleExtend>();
	
	@ZapcomApi(value="活动是否可用",remark="默认为0 （0：不可用，1：可用）")
	private String itemFlagEnable = "0"; 
	
	@ZapcomApi(value="毛利率",remark="")
	private BigDecimal profitRate = BigDecimal.ZERO; 
	
	@ZapcomApi(value="优惠价计算方式",remark="默认为0 （0：不可用，1：可用）")
	private String favorablePriceType = ""; 

	public BigDecimal getPriceEvent() {
		return priceEvent;
	}

	public void setPriceEvent(BigDecimal priceEvent) {
		this.priceEvent = priceEvent;
	}

	public List<PlusModelEventPriceStep> getPriceSteps() {
		return priceSteps;
	}

	public void setPriceSteps(List<PlusModelEventPriceStep> priceSteps) {
		this.priceSteps = priceSteps;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public EEventPriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(EEventPriceType priceType) {
		this.priceType = priceType;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public long getSalesStock() {
		return salesStock;
	}

	public void setSalesStock(long salesStock) {
		this.salesStock = salesStock;
	}

	public List<PlusModelEventSellScope> getSellScopes() {
		return sellScopes;
	}

	public void setSellScopes(List<PlusModelEventSellScope> sellScopes) {
		this.sellScopes = sellScopes;
	}

	public BigDecimal getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(BigDecimal priceStart) {
		this.priceStart = priceStart;
	}

	public List<PlusModelEventRuleExtend> getRuleExtends() {
		return ruleExtends;
	}

	public void setRuleExtends(List<PlusModelEventRuleExtend> ruleExtends) {
		this.ruleExtends = ruleExtends;
	}

	public String getItemFlagEnable() {
		return itemFlagEnable;
	}

	public void setItemFlagEnable(String itemFlagEnable) {
		this.itemFlagEnable = itemFlagEnable;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public String getFavorablePriceType() {
		return favorablePriceType;
	}

	public void setFavorablePriceType(String favorablePriceType) {
		this.favorablePriceType = favorablePriceType;
	}

}
