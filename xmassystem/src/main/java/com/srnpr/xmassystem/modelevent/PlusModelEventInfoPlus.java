package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 橙意卡活动信息
 */
public class PlusModelEventInfoPlus extends PlusModelEventInfo {

	@ZapcomApi(value = "活动价格")
	private BigDecimal price = BigDecimal.ZERO;

	@ZapcomApi(value = "活动名称")
	private String showName = "";

	@ZapcomApi(value = "活动描述")
	private String showNotes = "";

	@ZapcomApi(value = "商品限定")
	private String productLimit = "";

	@ZapcomApi(value = "限定商品编号列表")
	private List<String> productCodes = new ArrayList<String>();
	
	@ZapcomApi(value = "类别限定")
	private String categoryLimit = "";

	@ZapcomApi(value = "限定类别编号列表")
	private List<String> categoryCodes = new ArrayList<String>();

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public void setShowNotes(String showNotes) {
		this.showNotes = showNotes;
	}

	public String getProductLimit() {
		return productLimit;
	}

	public void setProductLimit(String productLimit) {
		this.productLimit = productLimit;
	}

	public List<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}

	public String getCategoryLimit() {
		return categoryLimit;
	}

	public void setCategoryLimit(String categoryLimit) {
		this.categoryLimit = categoryLimit;
	}

	public List<String> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(List<String> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}
	
}
