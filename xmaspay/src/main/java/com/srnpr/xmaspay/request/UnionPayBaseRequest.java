package com.srnpr.xmaspay.request;

/**
 * 银联支付基类
 * @author pangjh
 *
 */
public class UnionPayBaseRequest extends BasePayRequest {
	
	/*版本号*/
	private String version = "";
	
	/*字符编码*/
	private String encoding = "";
	
	/*签名证书私钥号*/
	private String certId = "";
	
	/*签名方法*/
	private String signMethod = "";
	
	/*签名*/
	private String signature = "";
	
	/*交易类型*/
	private String txnType = "";
	
	/*交易子类型*/
	private String txnSubType = "";
	
	/*产品类型*/
	private String bizType = "";
	
	/*商户编号*/
	private String merId = "";
	
	/*接入类型*/
	private String accessType = "";
	
	/*商户订单编号*/
	private String orderId = "";
	
	/*交易币种*/
	private String currencyCode = "";
	
	/*交易金额*/
	private String txnAmt ="";
	
	/*订单发送时间，去系统当前时间，否则会报无效*/
	private String txnTime = "";

	/**
	 * 获取版本编号
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置版本编号
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取字符编码
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 设置字符编码
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取加密证书私钥序列号
	 * @return
	 */
	public String getCertId() {
		return certId;
	}

	/**
	 * 设置加密证书私钥序列号
	 * @param certId
	 */
	public void setCertId(String certId) {
		this.certId = certId;
	}

	/**
	 * 获取签名方法
	 * @return
	 */
	public String getSignMethod() {
		return signMethod;
	}

	/**
	 * 设置签名方法
	 * @param signMethod
	 */
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	/**
	 * 获取签名
	 * @return
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 设置签名
	 * @param signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 获取交易类型
	 * @return
	 */
	public String getTxnType() {
		return txnType;
	}

	/**
	 * 设置交易类型
	 * @param txnType
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	/**
	 * 获取交易子类型
	 * @return
	 */
	public String getTxnSubType() {
		return txnSubType;
	}

	/**
	 * 设置交易子类型
	 * @param txnSubType
	 */
	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	/**
	 * 获取产品类型
	 * @return
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * 设置产品类型
	 * @param bizType
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	/**
	 * 获取商户编号
	 * @return
	 */
	public String getMerId() {
		return merId;
	}

	/**
	 * 设置商户编号
	 * @param merId
	 */
	public void setMerId(String merId) {
		this.merId = merId;
	}

	/**
	 * 获取接入类型
	 * @return
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * 设置接入类型
	 * @param accessType
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 设置订单编号
	 * @param orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 获取交易币种
	 * @return
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * 设置交易币种
	 * @param currencyCode
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 获取交易金额
	 * @return
	 */
	public String getTxnAmt() {
		return txnAmt;
	}

	/**
	 * 设置交易金额
	 * @param txnAmt
	 */
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}

	/**
	 * 获取订单发送时间
	 * @return
	 */
	public String getTxnTime() {
		return txnTime;
	}

	/**
	 * 设置订单发送时间
	 * @param txnTime
	 */
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

}
