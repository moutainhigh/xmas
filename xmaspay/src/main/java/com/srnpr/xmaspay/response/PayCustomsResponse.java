package com.srnpr.xmaspay.response;

import com.srnpr.xmaspay.face.IPayResponse;

/**
 * 订单报关处理响应
 * @author zhaojunling
 *
 */
public class PayCustomsResponse implements IPayResponse {

	/*处理结果响应码 （SUCCESS 或者 FAIL）*/
	private String resultCode;
	
	/*错误代码：请求成功时，不存在本参数*/
	private String resultMsg;
	/*支付单据*/
	private String tradeNo;
	/*订单号*/
	private String orderCode;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
}
