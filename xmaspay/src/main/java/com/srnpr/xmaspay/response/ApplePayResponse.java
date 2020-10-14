package com.srnpr.xmaspay.response;

/**
 * applePay响应信息
 * @author pang_jhui
 *
 */
public class ApplePayResponse extends BasePayResponse {
	
	/*商户号 */
	private String ap_merchant_id = "";
	
	/*第三方支付平台商户编号*/
	private String oid_partner = "";
	
	/*签名方式*/
	private String sign_type = "";
	
	/*签名*/
	private String sign = "";
	
	/*商户业务类型*/
	private String busi_partner = "";
	
	/*商户唯一订单编号*/
	private String no_order = "";
	
	/*商户订单时间*/
	private String dt_order = "";
	
	/*交易金额*/
	private String money_order = "";
	
	/*服务器异步通知地址*/
	private String notify_url = "";
	
	/*订单有效时间*/
	private String valid_order = "";
	
	/*风险控制参数（json格式）*/
	private String risk_item = "";
	
	/*商户平台唯一用户编号*/
	private String user_id = "";
	
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
	 * 获取第三方支付平台商户编号
	 * @return
	 */
	public String getOid_partner() {
		return oid_partner;
	}

	/**
	 * 设置第三方支付平台商户编号
	 * @param oid_partner
	 */
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	/**
	 * 获取签名类型
	 * @return
	 */
	public String getSign_type() {
		return sign_type;
	}

	/**
	 * 设置签名类型
	 * @param sign_type
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	/**
	 * 获取签名
	 * @return
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 设置签名
	 * @param sign
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * 获取商户业务类型
	 * @return
	 */
	public String getBusi_partner() {
		return busi_partner;
	}

	/**
	 * 设置商户业务类型
	 * @param busi_partner
	 */
	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	/**
	 * 获取商户唯一订单编号
	 * @return
	 */
	public String getNo_order() {
		return no_order;
	}

	/**
	 * 设置商户唯一订单编号
	 * @param no_order
	 */
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	/**
	 * 获取商户订单时间
	 * @return
	 */
	public String getDt_order() {
		return dt_order;
	}

	/**
	 * 设置商户订单时间
	 * @param dt_order
	 */
	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	/**
	 * 获取订单金额
	 * @return
	 */
	public String getMoney_order() {
		return money_order;
	}

	/**
	 * 设置订单金额
	 * @param money_order
	 */
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	/**
	 * 获取服务器异步通知地址
	 * @return
	 */
	public String getNotify_url() {
		return notify_url;
	}

	/**
	 * 设置服务器异步通知地址
	 * @param notify_url
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	/**
	 * 获取订单的有效时间
	 * @return
	 */
	public String getValid_order() {
		return valid_order;
	}

	/**
	 * 设置订单有效时间
	 * @param valid_order
	 */
	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}

	/**
	 * 获取风险控制参数（json格式）
	 * @return
	 */
	public String getRisk_item() {
		return risk_item;
	}

	/**
	 * 设置风险控制参数（json格式）
	 * @param risk_item
	 */
	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	/**
	 * 获取商户平台唯一用户编号
	 * @return
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * 设置商户平台唯一用户编号
	 * @param user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAp_merchant_id() {
		return ap_merchant_id;
	}

	public void setAp_merchant_id(String ap_merchant_id) {
		this.ap_merchant_id = ap_merchant_id;
	}

}
