package com.srnpr.xmaspay.request;

import java.math.BigDecimal;

/**
 * 支付网关回调请求对象
 * @author pang_jhui
 *
 */
public class PayGateWayCallBackRequest extends BasePayRequest {
	
	/*商户编号*/
	private String c_mid = "";
	
	/*订单编号*/
	private String c_order = "";
	
	/*订单金额*/
	private BigDecimal c_orderamount = BigDecimal.ZERO;
	
	/*订单产生日期:yyyymmdd*/
	private String c_ymd = "";
	
	/*支付币种*/
	private int c_moneytype;
	
	/*支付时间*/
	private String dealtime = "";
	
	/*交易流水号*/
	private String c_transnum = "";
	
	/*交易成功标志:Y-成功 N-失败*/
	private String c_succmark = "";
	
	/*失败原因*/
	private String c_cause = "";
	
	/*商户参数一*/
	private String c_memo1 = "";
	
	/*商户参数二*/
	private String c_memo2 = "";
	
	/*交易信息签名*/
	private String c_signstr = "";
	
	/*支付方式*/
	private int c_paygate;
	
	/*版本号*/
	private String c_version;
	
	/*实际流水号*/
	private String bankorderid;

	public String getC_mid() {
		return c_mid;
	}

	public void setC_mid(String c_mid) {
		this.c_mid = c_mid;
	}

	public String getC_order() {
		return c_order;
	}

	public void setC_order(String c_order) {
		this.c_order = c_order;
	}

	public BigDecimal getC_orderamount() {
		return c_orderamount;
	}

	public void setC_orderamount(BigDecimal c_orderamount) {
		this.c_orderamount = c_orderamount;
	}

	public String getC_ymd() {
		return c_ymd;
	}

	public void setC_ymd(String c_ymd) {
		this.c_ymd = c_ymd;
	}

	public int getC_moneytype() {
		return c_moneytype;
	}

	public void setC_moneytype(int c_moneytype) {
		this.c_moneytype = c_moneytype;
	}

	public String getDealtime() {
		return dealtime;
	}

	public void setDealtime(String dealtime) {
		this.dealtime = dealtime;
	}

	public String getC_transnum() {
		return c_transnum;
	}

	public void setC_transnum(String c_transnum) {
		this.c_transnum = c_transnum;
	}

	public String getC_succmark() {
		return c_succmark;
	}

	public void setC_succmark(String c_succmark) {
		this.c_succmark = c_succmark;
	}

	public String getC_cause() {
		return c_cause;
	}

	public void setC_cause(String c_cause) {
		this.c_cause = c_cause;
	}

	public String getC_memo1() {
		return c_memo1;
	}

	public void setC_memo1(String c_memo1) {
		this.c_memo1 = c_memo1;
	}

	public String getC_memo2() {
		return c_memo2;
	}

	public void setC_memo2(String c_memo2) {
		this.c_memo2 = c_memo2;
	}

	public String getC_signstr() {
		return c_signstr;
	}

	public void setC_signstr(String c_signstr) {
		this.c_signstr = c_signstr;
	}

	public int getC_paygate() {
		return c_paygate;
	}

	public void setC_paygate(int c_paygate) {
		this.c_paygate = c_paygate;
	}

	public String getC_version() {
		return c_version;
	}

	public void setC_version(String c_version) {
		this.c_version = c_version;
	}
	
	public String getBankorderid() {
		return bankorderid;
	}
	
	public void setBankorderid(String bankorderid) {
		this.bankorderid = bankorderid;
	}
}
