package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

/**
 * 支付信息
 * @author jlin
 *
 */
public class TeslaModelOrderPay {

	private String uid  = ""  ;
	/**
     * 订单编号
     */
    private String orderCode  = ""  ;
    /**
     * 流水编号，可以是礼品卡卡号，优惠券号，银行流水号，支付宝流水号
     */
    private String paySequenceid  = ""  ;
    
    /**
     * 此流水支付了多少钱
     */
    private BigDecimal payedMoney = BigDecimal.ZERO   ;
    
    
    /**
     * 礼品卡密码
     */
    private String passWord = "";
    
 
    /**
     * 支付类型449746280001:礼品卡    449746280002:优惠券    449746280003:支付宝支付   449746280004:快钱支付 449746280005:微信支付 449746280006:储值金 (家有汇)449746280007:暂存款(家有汇) 449746280008:积分(家有汇)
     */
    private String payType  = ""  ;
    
    /**
     * 买家编号 
     */
    private String merchantId = "";
    
	/**
     * 支付备注
     */
    private String payRemark  = ""  ;
    
	/**
     * 创建时间
     */
    private String createTime  = ""  ;
    
	/**
     *总账号
     */
    private String phpCode  = ""  ;
    
    
	/**
     *总费用
     */
    private BigDecimal payedAllFee  =  BigDecimal.ZERO;
    
	/**
     *分费用
     */
    private BigDecimal payedFee  = BigDecimal.ZERO;
    
	/**
     *读取状态0 未读过 ,1  读过。
     */
    private int status  = 0  ;
    
	/**
     *支付单号
     */
    private String payCode  = ""  ;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPaySequenceid() {
		return paySequenceid;
	}

	public void setPaySequenceid(String paySequenceid) {
		this.paySequenceid = paySequenceid;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayRemark() {
		return payRemark;
	}

	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhpCode() {
		return phpCode;
	}

	public void setPhpCode(String phpCode) {
		this.phpCode = phpCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public BigDecimal getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(BigDecimal payedMoney) {
		this.payedMoney = payedMoney;
	}

	public BigDecimal getPayedAllFee() {
		return payedAllFee;
	}

	public void setPayedAllFee(BigDecimal payedAllFee) {
		this.payedAllFee = payedAllFee;
	}

	public BigDecimal getPayedFee() {
		return payedFee;
	}

	public void setPayedFee(BigDecimal payedFee) {
		this.payedFee = payedFee;
	}
	
}
