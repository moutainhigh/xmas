package com.srnpr.xmaspay.response;

/**
 * 支付宝报关响应信息
 * @author pang_jhui
 *
 */
public class AlipayCustomsResponse extends AlipayUnifyResponse {
	
	/*错误代码：请求成功时，不存在本参数*/
	private String error = "";
	/*处理结果响应码 （SUCCESS 或者 FAIL）*/
	private String result_code = "";
	/*支付单据*/
	private String trade_no = "";
	/*报关流水号*/
	private String alipay_declare_no = "";
	/*详细错误码*/
	private String detail_error_code = "";
	/*详细错误描述*/
	private String detail_error_des = "";
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getAlipay_declare_no() {
		return alipay_declare_no;
	}
	public void setAlipay_declare_no(String alipay_declare_no) {
		this.alipay_declare_no = alipay_declare_no;
	}
	public String getDetail_error_code() {
		return detail_error_code;
	}
	public void setDetail_error_code(String detail_error_code) {
		this.detail_error_code = detail_error_code;
	}
	public String getDetail_error_des() {
		return detail_error_des;
	}
	public void setDetail_error_des(String detail_error_des) {
		this.detail_error_des = detail_error_des;
	}
	
}
