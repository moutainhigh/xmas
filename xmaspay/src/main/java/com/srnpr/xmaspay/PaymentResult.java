package com.srnpr.xmaspay;

/**
 * 支付接口响应超类
 */
public class PaymentResult{

	/** 操作成功结果码 */
	public static final int SUCCESS = 1;
	
	// 接口处理结果代码， 1 处理成功， 非 1 处理失败
	private int resultCode = SUCCESS;
	// 接口处理结果信息
	private String resultMessage;

	public boolean upFlagTrue() {
		return getResultCode() == SUCCESS;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}