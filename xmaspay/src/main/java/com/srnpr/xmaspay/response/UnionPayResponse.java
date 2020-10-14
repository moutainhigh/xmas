package com.srnpr.xmaspay.response;

/**
 * 银联支付响应信息
 * @author pang_jhui
 *
 */
public class UnionPayResponse extends BasePayResponse {
	
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
	
	/*订单发送时间，去系统当前时间，否则会报无效*/
	private String txnTime = "";
	
	/*应答码*/
	private String respCode = "";
	
	/*应答信息*/
	private String respMsg = "";
	
	/*银联受理订单号*/
	private String tn = "";
	
	/*返回结果码*/
	private int resultcode = 1;
	
	/*返回结果信息*/
	private String resultmsg = "";

	/**
	 * 获取结果码
	 */
	public int getResultcode() {
		return resultcode;
	}

	/**
	 * 设置结果码
	 * @param resultcode
	 */
	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}

	/**
	 * 获取结果信息
	 */
	public String getResultmsg() {
		return resultmsg;
	}

	/**
	 * 设置结果信息
	 * @param resultmsg
	 */
	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean upFlagTrue() {
		
		return getResultcode() == 1;
		
	}
	
	/**
	 * 获取版本号
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置版本号
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取编码
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 设置编码
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取加密证书序列号
	 * @return
	 */
	public String getCertId() {
		return certId;
	}

	/**
	 * 设置加密证书序列号
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

	/**
	 * 获取应答码
	 * @return
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * 设置应答码
	 * @param respCode
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * 获取应答信息
	 * @return
	 */
	public String getRespMsg() {
		return respMsg;
	}

	/**
	 * 设置应答信息
	 * @param respMsg
	 */
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	/**
	 * 获取银联受理订单号
	 * @return
	 */
	public String getTn() {
		return tn;
	}

	/**
	 * 设置银联受理订单号
	 * @param tn
	 */
	public void setTn(String tn) {
		this.tn = tn;
	}

}
