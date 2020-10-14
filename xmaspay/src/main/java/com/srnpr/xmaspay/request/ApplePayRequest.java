package com.srnpr.xmaspay.request;

/**
 * applePay请求信息
 * @author pang_jhui
 *
 */
public class ApplePayRequest extends BasePayRequest {
	
	/*订单编号*/
	private String orderCode = "";

	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * 设置订单编号
	 * @param orderCode
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	

}
