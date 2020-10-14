package com.srnpr.xmaspay.response;

/**
 * 银联支付回调响应信息
 * @author pang_jhui
 *
 */
public class UnionPayCallBackResponse extends BasePayResponse {

	/* 返回结果码 */
	private int resultcode = 1;

	/* 返回结果信息 */
	private String resultmsg = "";

	/**
	 * 获取结果码
	 */
	public int getResultcode() {
		return resultcode;
	}

	/**
	 * 设置结果码
	 * 
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
	 * 
	 * @param resultmsg
	 */
	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}

	/**
	 * 是否成功
	 * 
	 * @return
	 */
	public boolean upFlagTrue() {

		return getResultcode() == 1;

	}

}
