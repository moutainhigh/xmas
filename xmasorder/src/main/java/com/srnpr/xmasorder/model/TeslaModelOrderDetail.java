package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 订单详情信息
 * @author jlin
 *
 */
public class TeslaModelOrderDetail {

	
    /**
     * 第三方商户商品编码
     */
    private String sell_productcode = "";
    
    /**
     * 第三方商户sku编码
     */
    private String sell_skucode = "";
    
	
	private String uid = "";
	
	/**
	 * 订单编号
	 */
	private String orderCode = "";
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
	 * 产品价格（所有优惠前的商品售价）
	 */
	private BigDecimal sellPrice = BigDecimal.ZERO;
	
	/**
	 * 产品数量
	 */
	private int skuNum = 1;

	
	/**
	 * 商品的主图url
	 */
	private String productPicUrl = "";
	
    /** 仓库代码 */
    private String storeCode="";
	
    /**  赠品标示  1:不是赠品    0：是赠品*/
    private String giftFlag="1";
    
    /**赠品类别*/
    private String giftCd="";
    
    /**
     *成本价 
     */
    private BigDecimal costPrice = BigDecimal.ZERO;
    
    /**
     * 使用优惠券额度
     */
    private BigDecimal couponPrice = BigDecimal.ZERO;
    
    /**
     *使用微公社额度 
     */
    private BigDecimal groupPrice = BigDecimal.ZERO;
    
    /**
     *显示
     */
    private BigDecimal showPrice = BigDecimal.ZERO;
    
    /**
     *活动优惠金额 
     */
    private BigDecimal saveAmt = BigDecimal.ZERO;
    
    /***外部商品编号*/
    private String productCodeOut="";
    
    /**
     * 积分商城活动ID
     */
    private String integralDetailId = "";
    
    /**
     *税率 
     */
    private BigDecimal taxRate=BigDecimal.ZERO;
    
    @ZapcomApi(value = "是否选择", remark = "是否选择", demo = "0")
	private String choose_flag = "1";
    
    @ZapcomApi(value="商户编号")
    private String smallSellerCode = "";
    
    @ZapcomApi(value="惠豆金额",remark="已经惠豆转为RMB")
    private BigDecimal hjyBean = BigDecimal.ZERO;
    
    /** 是否仅支持在线支付 */
    private String onlinepayFlag;
    /** 是否仅支持在线支付 (开始时间) */
    private String onlinepayStart;
    /** 是否仅支持在线支付 (结束时间) */
    private String onlinepayEnd;
    
    /**
     * 是否原价购买
     */
    private String isSkuPriceToBuy = "0";
    
    /**
     * 佣金百分比
     */
    private String skuCharge = "0.00";
    
    /**
     * 利润百分比
     */
    private String skuProfit = "0.00";
    
    @ZapcomApi(value="使用积分金额",remark="拆分到的积分总金额")
    private BigDecimal integralMoney = BigDecimal.ZERO;
    
    @ZapcomApi(value="赋予积分金额",remark="赋予到商品的积分金额")
    private BigDecimal giveIntegralMoney = BigDecimal.ZERO;
    
    @ZapcomApi(value="是否赋予积分",remark="Y：是  N：否")
    private String accmYn = "";
    
    @ZapcomApi(value="储值金金额",remark="拆分到的储值金总金额")
    private BigDecimal czjMoney = BigDecimal.ZERO;
    
    @ZapcomApi(value="暂存款金额",remark="拆分到的暂存款总金额")
    private BigDecimal zckMoney = BigDecimal.ZERO;
    
    @ZapcomApi(value="惠币金额",remark="拆分到的惠币总金额")
    private BigDecimal hjycoin = BigDecimal.ZERO;
    
    @ZapcomApi(value="赋予惠币金额",remark="赋予到商品的惠币金额")
    private BigDecimal giveHjycoin = BigDecimal.ZERO;
    
    @ZapcomApi(value="结算类型",remark="")
    private String settlementType = "";

    @ZapcomApi(value="是否是网易考拉商品",remark="1是0否")
    private int isKaolaGood = 0;
    
    /**
     * 第三方售价
     */
    private BigDecimal thirdSellPrice = BigDecimal.ZERO;
    /**
     * 第三方成本价
     */
    private BigDecimal thirdCostPrice = BigDecimal.ZERO;
    
    /**
     * 分销人编号
     */
    private String fxrcode = "";
    
    /**
     * 推广人编号
     */
    private String shareCode = "";
    
    /**
     * 是否分销商品标识: 0 否, 1是
     */
    private int fxFlag = 0;
    
	/**
	 * 推广赚推广人编号
	 */
	private String tgzUserCode = "";
	
	/**
	 * 推广赚买家秀编号
	 */
	private String tgzShowCode = "";
    
    /**
     *加价购商品标识 0否 1是
     */
    private String ifJJGFlag = "0";
    
    
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

	public int getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(int fxFlag) {
		this.fxFlag = fxFlag;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(BigDecimal couponPrice) {
		this.couponPrice = couponPrice;
	}

	public BigDecimal getGroupPrice() {
		return groupPrice;
	}

	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}

	public BigDecimal getSaveAmt() {
		return saveAmt;
	}

	public void setSaveAmt(BigDecimal saveAmt) {
		this.saveAmt = saveAmt;
	}

	public String getGiftCd() {
		return giftCd;
	}

	public void setGiftCd(String giftCd) {
		this.giftCd = giftCd;
	}

	public BigDecimal getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}

	public String getProductCodeOut() {
		return productCodeOut;
	}

	public void setProductCodeOut(String productCodeOut) {
		this.productCodeOut = productCodeOut;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getChoose_flag() {
		return choose_flag;
	}

	public void setChoose_flag(String choose_flag) {
		this.choose_flag = choose_flag;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public BigDecimal getHjyBean() {
		return hjyBean;
	}

	public void setHjyBean(BigDecimal hjyBean) {
		this.hjyBean = hjyBean;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getIsSkuPriceToBuy() {
		return isSkuPriceToBuy;
	}

	public void setIsSkuPriceToBuy(String isSkuPriceToBuy) {
		this.isSkuPriceToBuy = isSkuPriceToBuy;
	}

	public String getSkuCharge() {
		return skuCharge;
	}

	public void setSkuCharge(String skuCharge) {
		this.skuCharge = skuCharge;
	}

	public String getSkuProfit() {
		return skuProfit;
	}

	public void setSkuProfit(String skuProfit) {
		this.skuProfit = skuProfit;
	}

	public BigDecimal getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(BigDecimal integralMoney) {
		this.integralMoney = integralMoney;
	}

	public String getIntegralDetailId() {
		return integralDetailId;
	}

	public void setIntegralDetailId(String integralDetailId) {
		this.integralDetailId = integralDetailId;
	}

	public BigDecimal getCzjMoney() {
		return czjMoney;
	}

	public void setCzjMoney(BigDecimal czjMoney) {
		this.czjMoney = czjMoney;
	}

	public BigDecimal getZckMoney() {
		return zckMoney;
	}

	public void setZckMoney(BigDecimal zckMoney) {
		this.zckMoney = zckMoney;
	}
	
	public BigDecimal getGiveIntegralMoney() {
		return giveIntegralMoney;
	}

	public void setGiveIntegralMoney(BigDecimal giveIntegralMoney) {
		this.giveIntegralMoney = giveIntegralMoney;
	}

	public String getAccmYn() {
		return accmYn;
	}

	public void setAccmYn(String accmYn) {
		this.accmYn = accmYn;
	}

	public void setThirdSellPrice(BigDecimal thirdSellPrice) {
		this.thirdSellPrice = thirdSellPrice;
	}

	public BigDecimal getThirdCostPrice() {
		return thirdCostPrice;
	}

	public void setThirdCostPrice(BigDecimal thirdCostPrice) {
		this.thirdCostPrice = thirdCostPrice;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public BigDecimal getHjycoin() {
		return hjycoin;
	}

	public void setHjycoin(BigDecimal hjycoin) {
		this.hjycoin = hjycoin;
	}

	public BigDecimal getGiveHjycoin() {
		return giveHjycoin;
	}

	public void setGiveHjycoin(BigDecimal giveHjycoin) {
		this.giveHjycoin = giveHjycoin;
	}

	public int getIsKaolaGood() {
		return isKaolaGood;
	}

	public void setIsKaolaGood(int isKaolaGood) {
		this.isKaolaGood = isKaolaGood;
	}
	public String getSell_skucode() {
		return sell_skucode;
	}

	public void setSell_skucode(String sell_skucode) {
		this.sell_skucode = sell_skucode;
	}
	public String getSell_productcode() {
		return sell_productcode;
	}

	public void setSell_productcode(String sell_productcode) {
		this.sell_productcode = sell_productcode;
	}

	public BigDecimal getThirdSellPrice() {
		return thirdSellPrice;
	}

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}
	
}
