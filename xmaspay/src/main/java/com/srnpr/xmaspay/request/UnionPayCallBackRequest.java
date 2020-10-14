package com.srnpr.xmaspay.request;

/**
 * 银联支付回调请求信息
 * @author pang_jhui
 *
 */
public class UnionPayCallBackRequest extends UnionPayBaseRequest {
	
	/*查询流水号*/
	private String queryId = "";
	
	/*系统跟踪号*/
	private String traceNo = "";
	
	/*交易传输时间 MMDDHHmmss*/
	private String traceTime = "";
	
	/*清算日期 MMDD*/
	private String settleDate = "";
	
	/*清算币种*/
	private String settleCurrencyCode = "";
	
	/*清算金额*/
	private String settleAmt = "";
	
	/*应答码*/
	private String respCode = "";
	
	/*应答信息*/
	private String respMsg = "";
	
	/*支付卡名称*/
	private String payCardIssueName = "";
	
	/*支付卡类型*/
	private String payCardType = "";

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getSettleCurrencyCode() {
		return settleCurrencyCode;
	}

	public void setSettleCurrencyCode(String settleCurrencyCode) {
		this.settleCurrencyCode = settleCurrencyCode;
	}

	public String getSettleAmt() {
		return settleAmt;
	}

	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getPayCardIssueName() {
		return payCardIssueName;
	}

	public void setPayCardIssueName(String payCardIssueName) {
		this.payCardIssueName = payCardIssueName;
	}

	public String getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}
	
	
}
