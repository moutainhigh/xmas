package com.srnpr.xmasproduct.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopUp;

public class SkuInfos extends RootResult {

	@ZapcomApi(value = "SKU信息", remark = "根据编号返回的结果集")
	private List<PlusModelSkuInfo> skus = new ArrayList<PlusModelSkuInfo>();
	
	@ZapcomApi(value = "商品活动信息", remark = "sku上活动的合集")
	private List<ProductActivity> events=new ArrayList<ProductActivity>();
	
	@ZapcomApi(value = "按钮显示类型", remark = "1：加入购物车，立即购买，2：电话订购+加入购物车+立即购买，3：电话订购+立即购买，4：立即购买，5：按钮上带倒计时的立即购买，6：单人购买，拼团购买")
	private int buttonControl=1;
	
	@ZapcomApi(value = "满减广告信息")
	List<PlusModelFullCutMessage> saleMessage = new ArrayList<PlusModelFullCutMessage>();

	
	@ZapcomApi(value = "市场价", remark = "市场价", demo = "5.01")
	private String marketPrice="0.00";
	
	@ZapcomApi(value = "赋予积分", remark = "购买可获得积分", demo = "0")
	private int integral = 0;
	@ZapcomApi(value = "赋予积分说明文字", remark = "")
	private String integralTips = "";
	
	@ZapcomApi(value = "赋予惠币", remark = "购买可获得惠币，支持2位小数", demo = "0")
	private float hjycoin = 0;
	@ZapcomApi(value = "赋予惠币说明文字", remark = "")
	private String hjycoinTips = "";
	
	@ZapcomApi(value = "是否显示价格曲线", remark = "标识是否显示价格曲线,显示:Y,不显示:N")
	private String showPriceCurve = "";
	
	@ZapcomApi(value="拼团人信息")
	private List<CollagePersonInfo> collagePersonList = new ArrayList<CollagePersonInfo>();
	
	@ZapcomApi(value="拼团规则页面", remark="仅当拼团时返回数据")
	private String collageRuleUrl = "";
	
	@ZapcomApi(value="拼团玩法", remark="仅当拼团时返回数据")
	private String collagePlayWay = "";
	
	@ZapcomApi(value="该团需要几人拼", remark="返回结果为拼好的汉子，例如:2人团")
    private String collagePersonCount = "";
    
    @ZapcomApi(value="拼团活动结束时间")
    private String collageEndTime = "";
    
    @ZapcomApi(value="拼团类型",remark="拼团类型：（4497473400050001：普通团，4497473400050002：邀新团）")
	private String collageType = "";
    
    @ZapcomApi(value="闪购/秒杀预告开始时间")
    private String noticeStartTime = "";
    
    
    @ZapcomApi(value="分期提示语", demo="该商品支持银联分期付款")
    private String installmentTitle = "";
    
    @ZapcomApi(value="分期展示信息", remark="两行文字用<br/>隔开")
    private List<String> installmentContent = new ArrayList<String>();
    
    @ZapcomApi(value="分期展示标题", demo="以下为建设银行信用卡分期支付信息，实际分期情况以支付页面为准")
    private String installmentContentTitle = "";
    
    @ZapcomApi(value="此商品分销收益", demo="0.1")
    private String fxMoney = "";
    
    @ZapcomApi(value="是否展示分销商品券后加", demo="1是 0否")
    private String ifShowFXPrice = "";
    
    @ZapcomApi(value="开通橙意卡后节约金钱以及提示语，橙意卡商品编号等信息",demo = "",remark="")
	private CykDiscountInfo cykInfo = new CykDiscountInfo();
	
	
	@ZapcomApi(value="视频模板是否开启直播互动标识",remark="0:不开启,1:开启")
	private String ifShowLiveInteractionFlag=TopUp.upConfig("familyhas.ifShowLiveInteractionFlag");
	
	@ZapcomApi(value="活动折扣", remark="暂时仅打折促销使用，取sku中折扣最低的", demo="7折：eventDiscount=7，  88折：eventDiscount=88")
    private int eventDiscount = 0;

	public String getIfShowLiveInteractionFlag() {
		return ifShowLiveInteractionFlag;
	}

	public void setIfShowLiveInteractionFlag(String ifShowLiveInteractionFlag) {
		this.ifShowLiveInteractionFlag = ifShowLiveInteractionFlag;
	}
    
    
	public String getFxMoney() {
		return fxMoney;
	}

	public void setFxMoney(String fxMoney) {
		this.fxMoney = fxMoney;
	}
    
	public CykDiscountInfo getCykInfo() {
		return cykInfo;
	}

	public void setCykInfo(CykDiscountInfo cykInfo) {
		this.cykInfo = cykInfo;
	}

	public String getIfShowFXPrice() {
		return ifShowFXPrice;
	}

	public void setIfShowFXPrice(String ifShowFXPrice) {
		this.ifShowFXPrice = ifShowFXPrice;
	}

	public String getNoticeStartTime() {
		return noticeStartTime;
	}

	public void setNoticeStartTime(String noticeStartTime) {
		this.noticeStartTime = noticeStartTime;
	}

	public String getShowPriceCurve() {
		return showPriceCurve;
	}

	public void setShowPriceCurve(String showPriceCurve) {
		this.showPriceCurve = showPriceCurve;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getButtonControl() {
		return buttonControl;
	}

	public void setButtonControl(int buttonControl) {
		this.buttonControl = buttonControl;
	}

	public List<ProductActivity> getEvents() {
		return events;
	}

	public void setEvents(List<ProductActivity> events) {
		this.events = events;
	}

	public List<PlusModelSkuInfo> getSkus() {
		return skus;
	}

	public void setSkus(List<PlusModelSkuInfo> skus) {
		this.skus = skus;
	}

	@ZapcomApi(value = "销售价格", remark = "实际销售价格", demo = "5.01")
	private String sellPrice = "";
	
	
	@ZapcomApi(value = "SKU价格", remark = "仅用于展示，SKU的价格的集合", demo = "5.01")
	private String skuPrice = "";

	

	@ZapcomApi(value = "购买按钮显示文字", remark = "购买按钮显示的文字", demo = "立即购买，抢购")
	private String buttonText = "";

	@ZapcomApi(value = "购买状态", remark = "购买的状态 只有该字段等于1时才可以允许购买按钮点击    状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位),6(秒杀/闪购预告)", verify = "in=0,1,2,3,4,5,6")
	private int buyStatus = 0;

	@ZapcomApi(value = "剩余秒数", remark = "剩余时间，用于各种倒计时的显示")
	private long limitSecond = -1;

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
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

	public long getLimitSecond() {
		return limitSecond;
	}

	public void setLimitSecond(long limitSecond) {
		this.limitSecond = limitSecond;
	}
	
	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public List<PlusModelFullCutMessage> getSaleMessage() {
		return saleMessage;
	}

	public void setSaleMessage(List<PlusModelFullCutMessage> saleMessage) {
		this.saleMessage = saleMessage;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getIntegralTips() {
		return integralTips;
	}

	public void setIntegralTips(String integralTips) {
		this.integralTips = integralTips;
	}

	public float getHjycoin() {
		return hjycoin;
	}

	public void setHjycoin(float hjycoin) {
		this.hjycoin = hjycoin;
	}

	public String getHjycoinTips() {
		return hjycoinTips;
	}

	public void setHjycoinTips(String hjycoinTips) {
		this.hjycoinTips = hjycoinTips;
	}

	public List<CollagePersonInfo> getCollagePersonList() {
		return collagePersonList;
	}

	public void setCollagePersonList(List<CollagePersonInfo> collagePersonList) {
		this.collagePersonList = collagePersonList;
	}

	public String getCollageRuleUrl() {
		return collageRuleUrl;
	}

	public void setCollageRuleUrl(String collageRuleUrl) {
		this.collageRuleUrl = collageRuleUrl;
	}

	public String getCollagePlayWay() {
		return collagePlayWay;
	}

	public void setCollagePlayWay(String collagePlayWay) {
		this.collagePlayWay = collagePlayWay;
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

	public String getInstallmentTitle() {
		return installmentTitle;
	}

	public void setInstallmentTitle(String installmentTitle) {
		this.installmentTitle = installmentTitle;
	}

	public List<String> getInstallmentContent() {
		return installmentContent;
	}

	public void setInstallmentContent(List<String> installmentContent) {
		this.installmentContent = installmentContent;
	}

	public String getInstallmentContentTitle() {
		return installmentContentTitle;
	}

	public void setInstallmentContentTitle(String installmentContentTitle) {
		this.installmentContentTitle = installmentContentTitle;
	}

	public String getCollageType() {
		return collageType;
	}

	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}
	
	
	public int getEventDiscount() {
		return eventDiscount;
	}

	public void setEventDiscount(int eventDiscount) {
		this.eventDiscount = eventDiscount;
	}
	
}
