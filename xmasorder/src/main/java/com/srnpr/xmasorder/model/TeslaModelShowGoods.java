package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelevent.PlusModelFullMoney;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商品详情信息
 * @author xiegj
 *
 */
public class TeslaModelShowGoods {


	/**
	 * 订单编号
	 */
	private String orderCode = "";
	/**
	 * 商户编号
	 */
	private String smallSellerCode = "";
	/**
	 * 产品对应的产品活动编号
	 */
	private String skuActivityCode = "";
	
	/**
	 * 产品编号
	 */
	private String skuCode = "";
	/**
	 * 商品编号
	 */
	private String productCode = "";
	
	/**
	 * 产品名称
	 */
	private String skuName = "";
	
	/**
	 * 产品价格
	 */
	private BigDecimal skuPrice = BigDecimal.ZERO ;
	
	/**
	 * 产品数量
	 */
	private int skuNum = 0;

	/**
	 * 商品的主图url
	 */
	private String productPicUrl = "";
	
    /** 仓库代码 */
    private String storeCode="";
	
    /**  赠品标示  1:不是赠品    0：是赠品*/
    private String giftFlag="1";
    /**
     * 是否虚拟商品 Y是 N否
     */
    private String validateFlag="";
    /**
     * 入库类型 00四地入库
     */
    private String prchType="";
    /**
     * 入库地
     */
    private String siteNo = "";
    /**
     * 供应商编号
     */
    private String dlrId = "";
    
    @ZapcomApi(value = "是否海外购商品", remark = "1：是，0：否", demo = "1")
	private String flagTheSea = "0";
    
    @ZapcomApi(value = "活动名称", remark = "活动名称", demo = "闪购")
	private String activity_name = "";
    
    @ZapcomApi(value = "是否参加活动", remark = "true:是,false:否", demo = "false")
	private boolean is_activity = false;
    
    @ZapcomApi(value="sku的属性名称")
    private String sku_keyValue = "";
    
    @ZapcomApi(value="sku最小起购数量")
    private long miniOrder = 1;
    
    @ZapcomApi(value="活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场)")
	private String eventType="";
    
    @ZapcomApi(value = "活动编号", remark = "活动编号", demo = "CX123456")
    private String eventCode="";
    
    @ZapcomApi(value = "sku原价", remark = "SKU价格，该字段存的是正常情况下的SKU的售价，不参与任何活动时的价格，仅用于显示使用", demo = "88")
    private BigDecimal orig_sku_price = BigDecimal.ZERO;
    
    @ZapcomApi(value = "活动显示语展示信息", remark = "活动显示语展示信息", demo = "满120减20 or 特价满场减10")
    private PlusModelFullMoney fullMoneys= new PlusModelFullMoney();
    
    @ZapcomApi(value="活动跳转链接",remark="跳转活动信息的活动页")
    private String activityUrl = "";
    
    @ZapcomApi(value="满减类型")
    private String fullType = "";
    
    @ZapcomApi(value = "是否选择", remark = "是否选择", demo = "0")
	private String choose_flag = "1";
    
    @ZapcomApi(value = "每单限购数量", remark = "每单限购数量",require = 1, demo = "123456")
	private int limit_order_num = 99;
    
    @ZapcomApi(value = "分组标识", remark = "分组标识（活动编号CX）购物车使用", demo = "")
	private String item_code = "";
    
    @ZapcomApi(value = "商品状态")
	private String productStatus = "";
    
    @ZapcomApi(value = "当前sku是否可售")
	private String saleYn = "";
    
    @ZapcomApi(value = "商品限购地区")
    PlusModelTemplateAeraCode areaCodes = new PlusModelTemplateAeraCode();
    
    @ZapcomApi(value = "商品限购提示语", remark = "不支持配送", demo = "1")
	private String alert = "";
    
    @ZapcomApi(value = "开始时间", remark = "开始时间", demo = "2015-01-01 00:00:00")
	private String start_time = "";
	
	@ZapcomApi(value = "结束时间", remark = "结束时间", demo = "2015-01-01 00:00:00")
	private String end_time = "";
    
	@ZapcomApi(value="商品标签,已作废，390版本之后不再使用",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="抄底价商品",remark=" 449747110001:否，449747110002:是")
	private String lowGood="";
	 
	@ZapcomApi(value="贸易类型")
	private String productTradeType = "";
	
	@ZapcomApi(value = "库存", remark = "0")
	private long stockNumSum = 0;		

	@ZapcomApi(value = "购买上限", remark = "0")
	private long maxBuyCount = 0;
	
	@ZapcomApi(value = "每用户限购数",remark="")
    private long limitBuy = 99;
	
	@ZapcomApi(value = "是否显示限购数，0：不显示，1显示")
	private int showLimitNum=0;
	
	@ZapcomApi(value = "是否考拉商品，0：不是，1是")
	private int isKaolaGood=0;
	
	@ZapcomApi(value="配送仓库类别", remark="4497471600430001商家配送、4497471600430002家有配送")
	private String deliveryStoreType = "";
	
	@ZapcomApi(value="加价购商品标识 0否 1是")
	private String ifJJGFlag = "0";
	
	@ZapcomApi(value="分销人编号")
    private String fxrcode = "";
	
	@ZapcomApi(value="推广赚推广人编号",remark = "")
	private String tgzUserCode = "";
	
	@ZapcomApi(value="推广赚买家秀编号",remark = "")
	private String tgzShowCode = "";
	
	@ZapcomApi(value="推广人编号")
    private String shareCode = "";
	
	@ZapcomApi(value="是否分销商品标识 0否 1是")
    private int fxFlag = 0;
	
	
	public String getIfJJGFlag() {
		return ifJJGFlag;
	}

	public void setIfJJGFlag(String ifJJGFlag) {
		this.ifJJGFlag = ifJJGFlag;
	}
	
	public String getFxrcode() {
		return fxrcode;
	}

	public void setFxrcode(String fxrcode) {
		this.fxrcode = fxrcode;
	}

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}

	public String getTgzUserCode() {
		return tgzUserCode;
	}

	public void setTgzUserCode(String tgzUserCode) {
		this.tgzUserCode = tgzUserCode;
	}

	public String getTgzShowCode() {
		return tgzShowCode;
	}

	public void setTgzShowCode(String tgzShowCode) {
		this.tgzShowCode = tgzShowCode;
	}

	public int getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(int fxFlag) {
		this.fxFlag = fxFlag;
	}

	public long getStockNumSum() {
		return stockNumSum;
	}

	public void setStockNumSum(long stockNumSum) {
		this.stockNumSum = stockNumSum;
	}

	public long getMaxBuyCount() {
		return maxBuyCount;
	}

	public void setMaxBuyCount(long maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}

	public long getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(long limitBuy) {
		this.limitBuy = limitBuy;
	}

	/**
     * 是否原价购买
     */
    private String isSkuPriceToBuy = "0";
	
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

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(String giftFlag) {
		this.giftFlag = giftFlag;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getSkuActivityCode() {
		return skuActivityCode;
	}

	public void setSkuActivityCode(String skuActivityCode) {
		this.skuActivityCode = skuActivityCode;
	}

	public String getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(String validateFlag) {
		this.validateFlag = validateFlag;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public boolean isIs_activity() {
		return is_activity;
	}

	public void setIs_activity(boolean is_activity) {
		this.is_activity = is_activity;
	}

	public String getSku_keyValue() {
		return sku_keyValue;
	}

	public void setSku_keyValue(String sku_keyValue) {
		this.sku_keyValue = sku_keyValue;
	}

	public long getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(long miniOrder) {
		this.miniOrder = miniOrder;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public BigDecimal getOrig_sku_price() {
		return orig_sku_price;
	}

	public void setOrig_sku_price(BigDecimal orig_sku_price) {
		this.orig_sku_price = orig_sku_price;
	}

	public PlusModelFullMoney getFullMoneys() {
		return fullMoneys;
	}

	public void setFullMoneys(PlusModelFullMoney fullMoneys) {
		this.fullMoneys = fullMoneys;
	}

	public String getChoose_flag() {
		return choose_flag;
	}

	public void setChoose_flag(String choose_flag) {
		this.choose_flag = choose_flag;
	}

	public int getLimit_order_num() {
		return limit_order_num;
	}

	public void setLimit_order_num(int limit_order_num) {
		this.limit_order_num = limit_order_num;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public PlusModelTemplateAeraCode getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(PlusModelTemplateAeraCode areaCodes) {
		this.areaCodes = areaCodes;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public String getLabelsPic() {
		return labelsPic;
	}

	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}

	public String getLowGood() {
		return lowGood;
	}

	public void setLowGood(String lowGood) {
		this.lowGood = lowGood;
	}

	public String getFullType() {
		return fullType;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public String getSaleYn() {
		return saleYn;
	}

	public void setSaleYn(String saleYn) {
		this.saleYn = saleYn;
	}

	public String getProductTradeType() {
		return productTradeType;
	}

	public void setProductTradeType(String productTradeType) {
		this.productTradeType = productTradeType;
	}
	
	public String getIsSkuPriceToBuy() {
		return isSkuPriceToBuy;
	}

	public void setIsSkuPriceToBuy(String isSkuPriceToBuy) {
		this.isSkuPriceToBuy = isSkuPriceToBuy;
	}

	public int getIsKaolaGood() {
		return isKaolaGood;
	}

	public void setIsKaolaGood(int isKaolaGood) {
		this.isKaolaGood = isKaolaGood;
	}

	public String getDeliveryStoreType() {
		return deliveryStoreType;
	}

	public void setDeliveryStoreType(String deliveryStoreType) {
		this.deliveryStoreType = deliveryStoreType;
	}

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}
	
}
