package com.srnpr.xmaspay.request;

/**
 * 银联支付请求信息
 * @author pang_jhui
 *
 */
public class UnionPayRequest extends UnionPayBaseRequest {
	
	/*渠道类型*/
	private String channelType = "";
	
	/*支付成功后回调地址*/
	private String backUrl = "";
	
	/*支付超时时间*/
	private String payTimeout = "";

	/**
	 * 获取渠道类型
	 * @return
	 */
	public String getChannelType() {
		return channelType;
	}

	/**
	 * 设置渠道类型
	 * @param channelType
	 */
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	
	/**
	 * 获取支付成功回调地址
	 * @return
	 */
	public String getBackUrl() {
		return backUrl;
	}

	/**
	 * 设置支付回调地址
	 * @param backUrl
	 */
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	
	/**
	 * 获取支付超时时间
	 * @return
	 */
	public String getPayTimeout() {
		return payTimeout;
	}

	/**
	 * 设置支付超时时间
	 * @param payTimeout
	 */
	public void setPayTimeout(String payTimeout) {
		this.payTimeout = payTimeout;
	}

}
