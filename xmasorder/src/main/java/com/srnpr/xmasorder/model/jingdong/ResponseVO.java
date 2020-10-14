package com.srnpr.xmasorder.model.jingdong;

/**
 * 接口响应统一VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class ResponseVO {


	private boolean success;
	
	private String resultCode;
	
	private String resultMessage;
	
	private String result;
	
	private String code;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
