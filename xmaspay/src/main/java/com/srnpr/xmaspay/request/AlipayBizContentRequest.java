package com.srnpr.xmaspay.request;

/**
 * 支付宝业务内容请求
 * @author pang_jhui
 *
 */
public class AlipayBizContentRequest extends AlipayUnifyRequest {
	
	/*商户交易号 */
	private String trade_no = "";
	
	/*商户订单编号*/
	private String out_trade_no = "";

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	

}
