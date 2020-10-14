package com.srnpr.xmaspay.request;

import java.math.BigDecimal;

/**
 * 支付网关请求信息
 * @author pang_jhui
 *
 */
public class PayGateWayRequest extends BasePayRequest {
	
	/*商户编号*/
	private String c_mid = "";
	
	/*商户订单号*/
	private String c_order = "";
	
	/*收获人姓名*/
	private String c_name = "";
	
	/*收货人地址*/
	private String c_address = "";
	
	/*收货人电话*/
	private String c_tel = "";
	
	/*收货人邮编*/
	private String c_post = "";
	
	/*收货人Email*/
	private String c_email = "";
	
	/*商户参数一*/
	private String c_memo1 = "";
	
	/*商户参数二*/
	private String c_memo2 = "";
	
	/*订单语种*/
	private int c_language ;
	
	/*订单总金额*/
	private BigDecimal c_orderamount = BigDecimal.ZERO;
	
	/*订单产生日期*/
	private String c_ymd = "";
	
	/*支付币种*/
	private String c_moneytype = "";
	
	/*返回标识*/
	private String c_retflag = "";
	
	/*支付结果页面URL*/
	private String c_returl = "";
	
	/*支付结果通知方式*/
	private String notifytype = "";
	
	/*交易信息签名*/
	private String c_signstr = "";
	
	/*支付方式*/
	private int c_paygate ;
	
	/*支付网关类型*/
	private int c_paygate_type ;
	
	/*支付网关帐号*/
	private int c_paygate_account ;
	
	/*版本号*/
	private String c_version = "";	

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

	public String getC_moneytype() {
		return c_moneytype;
	}

	public void setC_moneytype(String c_moneytype) {
		this.c_moneytype = c_moneytype;
	}

	public String getC_retflag() {
		return c_retflag;
	}

	public void setC_retflag(String c_retflag) {
		this.c_retflag = c_retflag;
	}

	public String getC_returl() {
		return c_returl;
	}

	public void setC_returl(String c_returl) {
		this.c_returl = c_returl;
	}

	public String getNotifytype() {
		return notifytype;
	}

	public void setNotifytype(String notifytype) {
		this.notifytype = notifytype;
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

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_address() {
		return c_address;
	}

	public void setC_address(String c_address) {
		this.c_address = c_address;
	}

	public String getC_post() {
		return c_post;
	}

	public void setC_post(String c_post) {
		this.c_post = c_post;
	}

	public String getC_email() {
		return c_email;
	}

	public void setC_email(String c_email) {
		this.c_email = c_email;
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

	public int getC_language() {
		return c_language;
	}

	public void setC_language(int c_language) {
		this.c_language = c_language;
	}

	public String getC_tel() {
		return c_tel;
	}

	public void setC_tel(String c_tel) {
		this.c_tel = c_tel;
	}

	public int getC_paygate_type() {
		return c_paygate_type;
	}

	public void setC_paygate_type(int c_paygate_type) {
		this.c_paygate_type = c_paygate_type;
	}

	public int getC_paygate_account() {
		return c_paygate_account;
	}

	public void setC_paygate_account(int c_paygate_account) {
		this.c_paygate_account = c_paygate_account;
	}

}
