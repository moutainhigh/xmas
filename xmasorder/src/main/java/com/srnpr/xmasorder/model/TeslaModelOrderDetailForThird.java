package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

/**
 * 第三方订单详情信息(暂时仅多彩订单)
 * @author renhongbin
 *
 */
public class TeslaModelOrderDetailForThird {

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
	 * 产品数量
	 */
	private int skuNum = 1;
    
    /**
     * 第三方商品价格
     */
    private BigDecimal thirdSkuPrice = BigDecimal.ZERO;
    
    /**
     * 第三方商品供货价
     */
    private BigDecimal thirdSupplyPrice = BigDecimal.ZERO;
    
    /**
     * 第三方商品成本价
     */
    private BigDecimal thirdCostPrice = BigDecimal.ZERO;
	
    /** 
     * 仓库代码
     */
    private String storeCode="";
	
    /** 
     * 赠品标示  1:不是赠品    0：是赠品
     */
    private String giftFlag="1";
    
    /** 
     * 赠品类别
     */
    private String giftCd="";
    
    /**
     *商户编号
     */
    private String smallSellerCode = "";
    
    /**
     *订单类型
     */
    private String orderType = "";
    
    /**
     *订单类型
     */
    private String createTime = "";
    

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

	public String getGiftCd() {
		return giftCd;
	}

	public void setGiftCd(String giftCd) {
		this.giftCd = giftCd;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public BigDecimal getThirdSkuPrice() {
		return thirdSkuPrice;
	}

	public void setThirdSkuPrice(BigDecimal thirdSkuPrice) {
		this.thirdSkuPrice = thirdSkuPrice;
	}

	public BigDecimal getThirdCostPrice() {
		return thirdCostPrice;
	}

	public void setThirdCostPrice(BigDecimal thirdCostPrice) {
		this.thirdCostPrice = thirdCostPrice;
	}

	public BigDecimal getThirdSupplyPrice() {
		return thirdSupplyPrice;
	}

	public void setThirdSupplyPrice(BigDecimal thirdSupplyPrice) {
		this.thirdSupplyPrice = thirdSupplyPrice;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
