package com.srnpr.xmaspay.response;

import com.srnpr.xmaspay.common.Constants;

/**
 * 支付网关回调响应对象
 * @author pang_jhui
 *
 */
public class PayGateWayCallBackResponse extends BasePayResponse {
	
	/*返回结果*/
	private String result = "";
	
	/*商户处理路径*/
	private String reURL = "";
	
	/**
	 * 判断是否处理成功
	 * @return true|成功 false|失败
	 */
	public boolean upFlagTrue(){
		
		return getResult() == Constants.PAYGATEWAY_CALLBACK_RESULT_SUCESS;
		
	}

	/**
	 * 获取处理结果
	 * @return 处理结果
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置处理结果
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 商户处理后的路径
	 * @return 路径
	 */
	public String getReURL() {
		return reURL;
	}

	/**
	 * 设置商户处理路径
	 * @param reURL
	 */
	public void setReURL(String reURL) {
		this.reURL = reURL;
	}

}
