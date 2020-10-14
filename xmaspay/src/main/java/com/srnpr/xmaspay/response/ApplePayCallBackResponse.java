package com.srnpr.xmaspay.response;

/**
 * applePay支付回调响应信息
 * @author pang_jhui
 *
 */
public class ApplePayCallBackResponse extends BasePayResponse {

	/*返回代码*/
	private String ret_code;

	/*返回信息*/
	private String ret_msg;

	/**
	 * 获取返回码
	 * @return
	 */
	public String getRet_code() {
		return ret_code;
	}

	/**
	 * 设置返回码
	 * @param ret_code
	 */
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	/**
	 * 获取返回信息
	 * @return
	 */
	public String getRet_msg() {
		return ret_msg;
	}

	/**
	 * 设置返回信息
	 * @param ret_msg
	 */
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

}
