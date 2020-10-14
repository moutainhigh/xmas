package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelSkuInfo implements IPlusModel, IPlusModelRefresh {

	/** 增加这个属性的值可在加载缓存时自动刷新旧版缓存数据 */
	public static final int _VERSION = 6;
	/** 版本号 */
	@ZapcomApi(value = "数据版本号")
	private int v = 0;
	
	public PlusModelSkuInfo() {
		this.v = _VERSION;
	}

	@Override
	public boolean isRefresh() {
		return v < _VERSION;
	}
	/** 增加这个属性的值可在加载缓存时自动刷新旧版缓存数据 */
	
	@ZapcomApi(value = "SKU编号")
	private String skuCode = "";
	@ZapcomApi(value = "销售价格", remark = "实际销售价格", demo = "5.01")
	private BigDecimal sellPrice = BigDecimal.ZERO;
	@ZapcomApi(value = "拼团价格", remark = "拼团商品才会有的价格", demo = "5.01")
	private BigDecimal groupBuyingPrice = BigDecimal.ZERO;
	@ZapcomApi(value = "销售价格名称", remark = "实际销售价格的名称 ", demo = "销售价，现价")
	private String sellNote = "";
	@ZapcomApi(value = "商品原价", remark = "商品原价，通常用于划掉的那个价格", demo = "5.01")
	private BigDecimal sourcePrice = BigDecimal.ZERO;
	@ZapcomApi(value = "商品原价名称", remark = "商品原先的价格 ", demo = "市场价，原价")
	private String sourceNote = "";

	@ZapcomApi(value = "SKU价格", remark = "SKU价格，该字段存的是正常情况下的SKU的售价，不参与任何活动时的价格，仅用于显示使用", demo = "5.01")
	private BigDecimal skuPrice=BigDecimal.ZERO;
	
	@ZapcomApi(value = "购买按钮显示文字", remark = "购买按钮显示的文字", demo = "立即购买，抢购")
	private String buttonText = "";

	@ZapcomApi(value = "购买状态", remark = "购买的状态 只有该字段等于1时才可以允许购买按钮点击    状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位),6(商品下架),7(已打限购上限)", verify = "in=0,1,2,3,4,5,6")
	private int buyStatus = 0;

	@ZapcomApi(value = "本次最多购买数量", remark = "用户最多购买的数量，默认情况下和库存数量一致  有活动时是限制数量", demo = "99")
	private long maxBuy = 99;

	@ZapcomApi(value = "本次最少购买数量", remark = "用户最少购买的数量，默认情况下为1", demo = "1")
	private long minBuy = 1;

	@ZapcomApi(value = "剩余促销库存", remark = "该活动下剩余的促销库存")
	private long limitStock = 0;
	
	@ZapcomApi(value="非活动剩余库存",remark="存放不参加活动的sku正常售价的库存")
	private long limitSellStock = 0;

	@ZapcomApi(value = "每用户限制购买", remark = "该字段仅用于展示,无实际业务逻辑用途")
	private long limitBuy = 99;

	@ZapcomApi(value = "剩余秒数", remark = "剩余时间，用于各种倒计时的显示")
	private long limitSecond = -1;

	@ZapcomApi(value = "活动明细编号")
	private String itemCode = "";

	@ZapcomApi(value = "活动信息编号")
	private String eventCode = "";
	
	@ZapcomApi(value="活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场),(4497472600010008为满减)")
	private String eventType="";

	@ZapcomApi(value="广告图")
	private String descriptionUrlHref="";
	
	@ZapcomApi(value = "商品编号")
	private String productCode = "";

	@ZapcomApi(value = "商品主图")
	private String productPicUrl = "";

	@ZapcomApi(value = "商户编号")
	private String smallSellerCode = "";

	@ZapcomApi(value = "是否虚拟商品 Y是 N否")
	private String validateFlag = "";
	
	@ZapcomApi(value = "sku属性信息")
	private String skuKeyvalue = "";
	
	@ZapcomApi(value = "sku属性值")
	private String skuKey = "";

	@ZapcomApi(value = "sku图片")
	private String skuPicUrl = "";
	
	@ZapcomApi(value = "sku名称")
	private String skuName = "";

	@ZapcomApi(value = "是否显示限购数，0：不显示，1显示")
	private int showLimitNum=0;
	
	@ZapcomApi(value = "当前Sku是否可售")
	private String saleYn = "";
	
    /** 是否仅支持在线支付 */
    private String onlinepayFlag;
    /** 是否仅支持在线支付 (开始时间) */
    private String onlinepayStart;
    /** 是否仅支持在线支付 (结束时间) */
    private String onlinepayEnd;
    
    @ZapcomApi(value="是否拼团", remark="0: 不拼团, 1: 拼团")
    private String isCollage = "0";
    
    @ZapcomApi(value="该团需要几人拼", remark="返回结果为拼好的汉子，例如:2人团")
    private String collagePersonCount = "";
    
    @ZapcomApi(value="拼团活动结束时间")
    private String collageEndTime = "";
    
    @ZapcomApi(value="拼团活动类型：4497473400050001普通团  4497473400050002邀新团")
    private String collageType = "";
    
	@ZapcomApi(value="成本价，一般用于第三方成本价（协议价）")
    private BigDecimal costPrice = BigDecimal.ZERO;
    
    @ZapcomApi(value = "SKU编号 一般为第三方系统中的编号")
	private String sellProductcode = "";
    
    
    public String getCollageType() {
		return collageType;
	}

	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}
	
	public BigDecimal getGroupBuyingPrice() {
		return groupBuyingPrice;
	}

	public void setGroupBuyingPrice(BigDecimal groupBuyingPrice) {
		this.groupBuyingPrice = groupBuyingPrice;
	}

	public String getOnlinepayFlag() {
		return onlinepayFlag;
	}

	public void setOnlinepayFlag(String onlinepayFlag) {
		this.onlinepayFlag = onlinepayFlag;
	}

	public String getOnlinepayStart() {
		return onlinepayStart;
	}

	public void setOnlinepayStart(String onlinepayStart) {
		this.onlinepayStart = onlinepayStart;
	}

	public String getOnlinepayEnd() {
		return onlinepayEnd;
	}

	public void setOnlinepayEnd(String onlinepayEnd) {
		this.onlinepayEnd = onlinepayEnd;
	}

	public String getSkuPicUrl() {
		return skuPicUrl;
	}

	public void setSkuPicUrl(String skuPicUrl) {
		this.skuPicUrl = skuPicUrl;
	}

	public long getLimitSecond() {
		return limitSecond;
	}

	public void setLimitSecond(long limitSecond) {
		this.limitSecond = limitSecond;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public int getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}

	public long getMaxBuy() {
		return maxBuy;
	}

	public void setMaxBuy(long maxBuy) {
		this.maxBuy = maxBuy;
	}

	public String getSkuCode() {

		return skuCode;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getSellNote() {
		return sellNote;
	}

	public void setSellNote(String sellNote) {
		this.sellNote = sellNote;
	}

	public BigDecimal getSourcePrice() {
		return sourcePrice;
	}

	public void setSourcePrice(BigDecimal sourcePrice) {
		this.sourcePrice = sourcePrice;
	}

	public String getSourceNote() {
		return sourceNote;
	}

	public void setSourceNote(String sourceNote) {
		this.sourceNote = sourceNote;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public long getLimitStock() {
		return limitStock;
	}

	public void setLimitStock(long limitStock) {
		this.limitStock = limitStock;
	}

	public long getMinBuy() {
		return minBuy;
	}

	public void setMinBuy(long minBuy) {
		this.minBuy = minBuy;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(String validateFlag) {
		this.validateFlag = validateFlag;
	}

	public String getSkuKeyvalue() {
		return skuKeyvalue;
	}

	public void setSkuKeyvalue(String skuKeyvalue) {
		this.skuKeyvalue = skuKeyvalue;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public long getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(long limitBuy) {
		this.limitBuy = limitBuy;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}
	
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}

	public String getDescriptionUrlHref() {
		return descriptionUrlHref;
	}

	public void setDescriptionUrlHref(String descriptionUrlHref) {
		this.descriptionUrlHref = descriptionUrlHref;
	}

	public String getSaleYn() {
		return saleYn;
	}

	public void setSaleYn(String saleYn) {
		this.saleYn = saleYn;
	}

	public long getLimitSellStock() {
		return limitSellStock;
	}

	public void setLimitSellStock(long limitSellStock) {
		this.limitSellStock = limitSellStock;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	
	public String getIsCollage() {
		return isCollage;
	}

	public void setIsCollage(String isCollage) {
		this.isCollage = isCollage;
	}

	public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
	}

	public String getCollageEndTime() {
		return collageEndTime;
	}

	public void setCollageEndTime(String collageEndTime) {
		this.collageEndTime = collageEndTime;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getSellProductcode() {
		return sellProductcode;
	}

	public void setSellProductcode(String sellProductcode) {
		this.sellProductcode = sellProductcode;
	}
	
}
