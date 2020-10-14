package com.srnpr.xmassystem.invoke.ref.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 客户当前的积分、储值金、暂存款
 */
public class GetCustAmtResult extends RootResult{

	@ZapcomApi(value = "可用积分金额")
	private BigDecimal possAccmAmt = new BigDecimal(0);

	@ZapcomApi(value = "可用储值金金额")
	private BigDecimal possPpcAmt = new BigDecimal(0);
	
	@ZapcomApi(value = "可用暂存款金额")
	private BigDecimal possCrdtAmt = new BigDecimal(0);
	
	@ZapcomApi(value = "可用惠币金额")
	private BigDecimal possHcoinAmt = new BigDecimal(0);
	
	@ZapcomApi(value = "预估惠币金额")
	private BigDecimal preHcoinAmt = new BigDecimal(0);
	
	@ZapcomApi(value = "将要过期积分")
	private BigDecimal expireAccm = new BigDecimal(0);

	public BigDecimal getPossHcoinAmt() {
		return possHcoinAmt;
	}

	public void setPossHcoinAmt(BigDecimal possHcoinAmt) {
		this.possHcoinAmt = possHcoinAmt;
	}

	public BigDecimal getPreHcoinAmt() {
		return preHcoinAmt;
	}

	public void setPreHcoinAmt(BigDecimal preHcoinAmt) {
		this.preHcoinAmt = preHcoinAmt;
	}

	public BigDecimal getExpireAccm() {
		return expireAccm;
	}

	public void setExpireAccm(BigDecimal expireAccm) {
		this.expireAccm = expireAccm;
	}

	public BigDecimal getPossAccmAmt() {
		return possAccmAmt;
	}

	public void setPossAccmAmt(BigDecimal possAccmAmt) {
		this.possAccmAmt = possAccmAmt;
	}

	public BigDecimal getPossPpcAmt() {
		return possPpcAmt;
	}

	public void setPossPpcAmt(BigDecimal possPpcAmt) {
		this.possPpcAmt = possPpcAmt;
	}

	public BigDecimal getPossCrdtAmt() {
		return possCrdtAmt;
	}

	public void setPossCrdtAmt(BigDecimal possCrdtAmt) {
		this.possCrdtAmt = possCrdtAmt;
	}
	
}
