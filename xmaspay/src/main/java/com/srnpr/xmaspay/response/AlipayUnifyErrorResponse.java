package com.srnpr.xmaspay.response;

/**
 * 支付宝异常响应信息
 * @author pang_jhui
 *
 */
public class AlipayUnifyErrorResponse extends AlipayUnifyResponse {
	
	/*错误代码：请求成功时，不存在本参数*/
	private String error = "";

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
