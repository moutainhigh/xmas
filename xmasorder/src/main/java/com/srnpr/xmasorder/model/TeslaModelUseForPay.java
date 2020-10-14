package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 各种用于订单支付的支付类型
 * @author xiegj
 *
 */
public class TeslaModelUseForPay {
	
	@ZapcomApi(value = "优惠券编号（此处为list，为以后扩充留用）", remark = "优惠券编号", demo = "yhq123456")
	private List<String> coupon_codes = new ArrayList<String>();
	
	@ZapcomApi(value = "优惠券金额")
	private	List<BigDecimal> coupon_Moneys = new ArrayList<BigDecimal>();
	
	@ZapcomApi(value = "优惠券类型", remark = "优惠券类型", demo = "449748120001")
	private List<String> coupon_types = new ArrayList<String>();
	
	@ZapcomApi(value = "优惠券是否可叠加使用", remark = "优惠券是否可叠加使用", demo = "Y/N")
	private List<String> is_multi_uses = new ArrayList<String>();

	@ZapcomApi(value = "微公社金额")
	private BigDecimal wgs_money = BigDecimal.ZERO;

	@ZapcomApi(value = "人品奖励")
	private BigDecimal moral_character_money = BigDecimal.ZERO;
	
	@ZapcomApi(value = "满减金额")
	private BigDecimal full_money = BigDecimal.ZERO;
	
	@ZapcomApi(value = "储值金使用金额", remark = "储值金使用金额", demo = "123456.00")
	private double czj_money = 0.00; 
	
	@ZapcomApi(value = "暂存款使用金额", remark = "暂存款使用金额", demo = "123456.00")
	private double zck_money = 0.00;
	
	@ZapcomApi(value = "惠币使用金额", remark = "惠币使用金额", demo = "123456.00")
	private double hjycoin = 0.00;
	
	@ZapcomApi(value = "积分使用个数",  demo = "200")
	private int integral = 0;
	
	@ZapcomApi(value = "储值金、暂存款占用状态", remark = "1:成功。0:失败", demo = "0")
	private String amt_status = "0";
	
	@ZapcomApi(value="惠豆")
	private BigDecimal hjyBean = BigDecimal.ZERO;

	public List<String> getCoupon_codes() {
		return coupon_codes;
	}

	public void setCoupon_codes(List<String> coupon_codes) {
		this.coupon_codes = coupon_codes;
	}

	public BigDecimal getWgs_money() {
		return wgs_money;
	}

	public void setWgs_money(BigDecimal wgs_money) {
		this.wgs_money = wgs_money;
	}	

	public List<BigDecimal> getCoupon_Moneys() {
		return coupon_Moneys;
	}

	public void setCoupon_Moneys(List<BigDecimal> coupon_Moneys) {
		this.coupon_Moneys = coupon_Moneys;
	}

	public BigDecimal getMoral_character_money() {
		return moral_character_money;
	}

	public void setMoral_character_money(BigDecimal moral_character_money) {
		this.moral_character_money = moral_character_money;
	}

	public BigDecimal getFull_money() {
		return full_money;
	}

	public void setFull_money(BigDecimal full_money) {
		this.full_money = full_money;
	}

	public double getHjycoin() {
		return hjycoin;
	}

	public void setHjycoin(double hjycoin) {
		this.hjycoin = hjycoin;
	}

	public double getCzj_money() {
		return czj_money;
	}

	public void setCzj_money(double czj_money) {
		this.czj_money = czj_money;
	}

	public double getZck_money() {
		return zck_money;
	}

	public void setZck_money(double zck_money) {
		this.zck_money = zck_money;
	}

	public String getAmt_status() {
		return amt_status;
	}

	public void setAmt_status(String amt_status) {
		this.amt_status = amt_status;
	}

	public BigDecimal getHjyBean() {
		return hjyBean;
	}

	public void setHjyBean(BigDecimal hjyBean) {
		this.hjyBean = hjyBean;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public List<String> getCoupon_types() {
		return coupon_types;
	}

	public void setCoupon_types(List<String> coupon_types) {
		this.coupon_types = coupon_types;
	}

	public List<String> getIs_multi_uses() {
		return is_multi_uses;
	}

	public void setIs_multi_uses(List<String> is_multi_uses) {
		this.is_multi_uses = is_multi_uses;
	}
	
}
