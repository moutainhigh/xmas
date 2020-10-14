package com.srnpr.xmaspay.request;

import java.math.BigDecimal;

/**
 * 支付宝通关请求信息
 * @author zhaojunling
 *
 */
public class AlipayCustomsRequest extends AlipayUnifyRequest {
	
	/*报关流水号*/
	private String out_request_no = "";
	
	/*支付宝交易号*/
	private String trade_no = "";
	
	/*商户海关备案号*/
	private String merchant_customs_code = "";
	
	/*商户海关备案名称*/
	private String merchant_customs_name = "";
	
	/*报关金额*/
	private BigDecimal amount;
	
	/*拆单标识 (T或者t触发拆单)*/
	private String is_split = "";
	
	/*子订单号(拆单时必传)*/
	private String sub_out_biz_no = "";
	
	/*海关编号*/
	private String customs_place = "";

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getMerchant_customs_code() {
		return merchant_customs_code;
	}

	public void setMerchant_customs_code(String merchant_customs_code) {
		this.merchant_customs_code = merchant_customs_code;
	}

	public String getMerchant_customs_name() {
		return merchant_customs_name;
	}

	public void setMerchant_customs_name(String merchant_customs_name) {
		this.merchant_customs_name = merchant_customs_name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getIs_split() {
		return is_split;
	}

	public void setIs_split(String is_split) {
		this.is_split = is_split;
	}

	public String getSub_out_biz_no() {
		return sub_out_biz_no;
	}

	public void setSub_out_biz_no(String sub_out_biz_no) {
		this.sub_out_biz_no = sub_out_biz_no;
	}

	public String getCustoms_place() {
		return customs_place;
	}

	public void setCustoms_place(String customs_place) {
		this.customs_place = customs_place;
	}
	
}
