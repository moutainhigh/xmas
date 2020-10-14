package com.srnpr.xmasorder.channel.model;

import java.math.BigDecimal;

import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;

/**
 * 商品详情信息
 * @remark 
 * @author 任宏斌
 * @date 2019年12月2日
 */
public class PorscheModelGoodsInfo {


	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 商户编号
	 */
	private String smallSellerCode = "";
	
	
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
     * 仓库代码 
     */
    private String storeCode="";
	
    /**  
     * 赠品标示  1:不是赠品    0：是赠品
     */
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
    
    /**
     * sku的属性名称
     */
    private String sku_keyValue = "";
    
    /**
     * sku最小起购数量
     */
    private long miniOrder = 1;
    
    /**
     * 每单限购数量
     */
	private int limit_order_num = 99;
    
    /**
     * 商品状态
     */
	private String productStatus = "";
    
    /**
     * 当前sku是否可售
     */
	private String saleYn = "";
    
    /**
     * 商品限购地区
     */
    PlusModelTemplateAeraCode areaCodes = new PlusModelTemplateAeraCode();
    
	/**
	 * 抄底价商品
	 * 449747110001:否，449747110002:是
	 */
	private String lowGood="";
	 
	/**
	 * 贸易类型
	 */
	private String productTradeType = "";
	
	/**
	 * 库存
	 */
	private long stockNumSum = 0;		

	/**
	 * 购买上限
	 */
	private long maxBuyCount = 0;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
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

	public String getSaleYn() {
		return saleYn;
	}

	public void setSaleYn(String saleYn) {
		this.saleYn = saleYn;
	}

	public PlusModelTemplateAeraCode getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(PlusModelTemplateAeraCode areaCodes) {
		this.areaCodes = areaCodes;
	}

	public String getLowGood() {
		return lowGood;
	}

	public void setLowGood(String lowGood) {
		this.lowGood = lowGood;
	}

	public String getProductTradeType() {
		return productTradeType;
	}

	public void setProductTradeType(String productTradeType) {
		this.productTradeType = productTradeType;
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

}
