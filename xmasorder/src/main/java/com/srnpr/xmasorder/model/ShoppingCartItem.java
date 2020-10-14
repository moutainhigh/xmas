package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车商品分组
*    xiegj
*/
public class ShoppingCartItem  {
	
	@ZapcomApi(value = "是否选择", remark = "是否选择：1：是，0：否", demo = "1")
	private String chooseFlag = "0";
	
	@ZapcomApi(value = "活动编号", remark = "", demo = "cx123456")
	private String eventCode = "";
	
	@ZapcomApi(value = "开始时间", remark = "开始时间", demo = "2015-01-01 00:00:00")
	private String start_time = "";
	
	@ZapcomApi(value = "结束时间", remark = "结束时间", demo = "2015-01-01 00:00:00")
	private String end_time = "";
	
	@ZapcomApi(value = "减免金额", remark = "该类型的所有商品总减免金额", demo = "1")
	private Double derateMoney = 0.00;
	
	@ZapcomApi(value = "plus折扣金额", remark = "该类型的所有商品总减免金额", demo = "1")
	private BigDecimal plusMoney = BigDecimal.ZERO;
	
	@ZapcomApi(value = "活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场)", demo = "0")
	private String type = "";
	
	@ZapcomApi(value = "活动类型", remark = "是否显示勾选 1-显示勾选 0-不显示勾选", demo = "0")
	private String  showChoose = "0";
	
	@ZapcomApi(value = "小计金额", remark = "该类型的所有商品总金额（应付金额）", demo = "0")
	private Double payMoney = 0.00;
	
	@ZapcomApi(value = "活动", remark = "活动", demo = "0")
	private ShoppingCartEvent event = new ShoppingCartEvent();
	
	@ZapcomApi(value = "购物车中商品", remark = "购物车中商品", demo = "0")
	private List<ShoppingCartGoodsInfo> goods = new ArrayList<ShoppingCartGoodsInfo>();
	
	@ZapcomApi(value="是否去凑单",remark="展示购物车是否需要去凑单（10000：凑单；10001：不凑单）")
	private String flagAddOrder = "10000";

	public String getChooseFlag() {
		return chooseFlag;
	}

	public void setChooseFlag(String chooseFlag) {
		this.chooseFlag = chooseFlag;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public Double getDerateMoney() {
		return derateMoney;
	}

	public void setDerateMoney(Double derateMoney) {
		this.derateMoney = derateMoney;
	}

	public BigDecimal getPlusMoney() {
		return plusMoney;
	}

	public void setPlusMoney(BigDecimal plusMoney) {
		this.plusMoney = plusMoney;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShowChoose() {
		return showChoose;
	}

	public void setShowChoose(String showChoose) {
		this.showChoose = showChoose;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public ShoppingCartEvent getEvent() {
		return event;
	}

	public void setEvent(ShoppingCartEvent event) {
		this.event = event;
	}

	public List<ShoppingCartGoodsInfo> getGoods() {
		return goods;
	}

	public void setGoods(List<ShoppingCartGoodsInfo> goods) {
		this.goods = goods;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getFlagAddOrder() {
		return flagAddOrder;
	}

	public void setFlagAddOrder(String flagAddOrder) {
		this.flagAddOrder = flagAddOrder;
	} 
	
}

