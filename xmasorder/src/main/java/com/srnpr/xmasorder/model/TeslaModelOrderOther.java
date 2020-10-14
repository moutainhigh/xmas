package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 存放一些不共用的订单附加信息
 * 
 * @author jlin
 *
 */
public class TeslaModelOrderOther {

	@ZapcomApi(value = "外部订单号",remark="")
	private String out_order_code = "";
	
	@ZapcomApi(value = "LD运费",remark="")
	private BigDecimal carriageMoney = BigDecimal.ZERO;

	@ZapcomApi(value = "分销系统运费",remark="")
	private BigDecimal carriageFXMoney = BigDecimal.ZERO;

	@ZapcomApi(value="是否需要校验身份证  3.8.8",remark="1-需要校验身份证  0-不需要校验身份证",demo="1")
	private String isVerifyIdNumber="0";
	
	@ZapcomApi(value="第三方订单运费",remark="暂时仅多彩宝订单使用")
	private BigDecimal thirdMoney = BigDecimal.ZERO;
	
	@ZapcomApi(value="第三方订单创建时间", remark="第三方订单创建时间")
	private String createTime = "";
	
	public String getOut_order_code() {
		return out_order_code;
	}

	public void setOut_order_code(String out_order_code) {
		this.out_order_code = out_order_code;
	}

	public BigDecimal getCarriageMoney() {
		return carriageMoney;
	}

	public void setCarriageMoney(BigDecimal carriageMoney) {
		this.carriageMoney = carriageMoney;
	}

	public BigDecimal getCarriageFXMoney() {
		return carriageFXMoney;
	}

	public void setCarriageFXMoney(BigDecimal carriageFXMoney) {
		this.carriageFXMoney = carriageFXMoney;
	}

	public String getIsVerifyIdNumber() {
		return isVerifyIdNumber;
	}

	public void setIsVerifyIdNumber(String isVerifyIdNumber) {
		this.isVerifyIdNumber = isVerifyIdNumber;
	}

	public BigDecimal getThirdMoney() {
		return thirdMoney;
	}

	public void setThirdMoney(BigDecimal thirdMoney) {
		this.thirdMoney = thirdMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
