package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelProObject {
	private String productCode="";
	private String skuCode="";
	private int skuNum=0;
	private String brandCode="";
	private List<String> categoryCodes = new ArrayList<String>();
	
	@ZapcomApi(value = "sku原价", remark = "SKU价格，该字段存的是正常情况下的SKU的售价，不参与任何活动时的价格，仅用于显示使用", demo = "88")
    private BigDecimal orig_sku_price = BigDecimal.ZERO;
	
	@ZapcomApi(value = "是否选择", remark = "1:是,0:否选择", demo = "0")
	private String choose_flag = "0";
	
	/**一下两个参数是返回用，调用时为null即可**/
	@ZapcomApi(value = "sku活动价格", remark = "sku参加活动后的价格", demo = "55")
	private BigDecimal sku_price = BigDecimal.ZERO;
	
	private String eventCode="";
	private PlusModelFullMoney fullMoneys= new PlusModelFullMoney();
	
	private List<PlusModelFullMoney> fullMoneysList= new ArrayList<PlusModelFullMoney>();
	
	@ZapcomApi(value="满减类型", remark = "(449747630001为满X减Y)，(449747630002为每满X减Y)，(449747630003为阶梯满减)，(449747630004为满X件减Y件)，(449747630005为满X元任选Y件)，(449747630006为满折-仅第X件打折)，(449747630007为满折-总价打折)")
	private String fullType="";
	
	@ZapcomApi(value="活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场),(4497472600010008为满减)")
	private String eventType="";
	
	@ZapcomApi(value = "活动类型名称", remark = "活动类型名称", demo = "满减 or 组合购")
	private String eventTypeTame;

	@ZapcomApi(value = "是否参加满减", remark = "是否参加满减", demo = "false")
	private boolean isEventTrue=false;
	
	@ZapcomApi(value = "分组标识", remark = "分组标识（活动编号CX）", demo = "")
	private String item_code = "";;
	
	@ZapcomApi(value = "活动开始时间")
	private String beginTime = "";
	@ZapcomApi(value = "活动结束时间")
	private String endTime = "";
	
	@ZapcomApi(value="活动跳转链接",remark="活动跳转专题页链接")
	private String activityUrl = "";
	
	@ZapcomApi(value = "所属商户编号")
	private String smallSellerCode = "";
	
	@ZapcomApi(value="最大满减金额",remark="满减类型为449747630008 时赋值")
	private BigDecimal fullCutMaxPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="满减价计算类型",remark="满减类型为449747630008 时赋值")
	private String fullCutCalType = "";
	
	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}
	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public int getSkuNum() {
		return skuNum;
	}
	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}
	/**
	 * @return the brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}
	/**
	 * @param brandCode the brandCode to set
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	/**
	 * @return the categoryCode
	 */
	public List<String> getCategoryCodes() {
		return categoryCodes;
	}
	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCodes(List<String> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}
	/**
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}
	/**
	 * @param eventCode the eventCode to set
	 */
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	/**
	 * @return the fullMoneys
	 */
	public PlusModelFullMoney getFullMoneys() {
		return fullMoneys;
	}
	/**
	 * @param fullMoneys the fullMoneys to set
	 */
	public void setFullMoneys(PlusModelFullMoney fullMoneys) {
		this.fullMoneys = fullMoneys;
	}
	/**
	 * @return the isEventTrue
	 */
	public boolean isEventTrue() {
		return isEventTrue;
	}
	/**
	 * @param isEventTrue the isEventTrue to set
	 */
	public void setEventTrue(boolean isEventTrue) {
		this.isEventTrue = isEventTrue;
	}
	public BigDecimal getOrig_sku_price() {
		return orig_sku_price;
	}
	public void setOrig_sku_price(BigDecimal orig_sku_price) {
		this.orig_sku_price = orig_sku_price;
	}
	public BigDecimal getSku_price() {
		return sku_price;
	}
	public void setSku_price(BigDecimal sku_price) {
		this.sku_price = sku_price;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventTypeTame() {
		return eventTypeTame;
	}
	public void setEventTypeTame(String eventTypeTame) {
		this.eventTypeTame = eventTypeTame;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the fullType
	 */
	public String getFullType() {
		return fullType;
	}
	/**
	 * @param fullType the fullType to set
	 */
	public void setFullType(String fullType) {
		this.fullType = fullType;
	}
	/**
	 * @return the fullMoneysList
	 */
	public List<PlusModelFullMoney> getFullMoneysList() {
		return fullMoneysList;
	}
	/**
	 * @param fullMoneysList the fullMoneysList to set
	 */
	public void setFullMoneysList(List<PlusModelFullMoney> fullMoneysList) {
		this.fullMoneysList = fullMoneysList;
	}
	public String getChoose_flag() {
		return choose_flag;
	}
	public void setChoose_flag(String choose_flag) {
		this.choose_flag = choose_flag;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getActivityUrl() {
		return activityUrl;
	}
	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}
	public String getSmallSellerCode() {
		return smallSellerCode;
	}
	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}
	public BigDecimal getFullCutMaxPrice() {
		return fullCutMaxPrice;
	}
	public void setFullCutMaxPrice(BigDecimal fullCutMaxPrice) {
		this.fullCutMaxPrice = fullCutMaxPrice;
	}
	public String getFullCutCalType() {
		return fullCutCalType;
	}
	public void setFullCutCalType(String fullCutCalType) {
		this.fullCutCalType = fullCutCalType;
	}
	
	
}
