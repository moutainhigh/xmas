package com.srnpr.xmassystem.modelbean;

import com.srnpr.zapcom.topapi.RootInput;

/**
 * 操作惠豆  调用用户中心操作惠豆输入参数
 * @author fq
 *
 */
public class HjyBeanCtrInput extends RootInput{

	/*
	 * 积分金额
	 */
	private Integer amount = 0;
	
	/*
	 * 操作类型
	 * 
	 * 1、	购物送惠豆
	 * 2、	用户取消订单
	 * 3、	部分退款
	 * 4、	超时取消订单
	 * 5、	购物使用惠豆
	 */
	private Integer tradetype = 0;
	
	/*
	 * 订单日期
	 */
	private String orderdate = "";
	
	/*
	 * 资金方向
	 * 
	 * 1、	入
	 * 2、	出
	 */
	private Integer inout = 0;
	
	/*
	 * 备注
	 */
	private String memo = "";

	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getTradetype() {
		return tradetype;
	}

	public void setTradetype(Integer tradetype) {
		this.tradetype = tradetype;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public Integer getInout() {
		return inout;
	}

	public void setInout(Integer inout) {
		this.inout = inout;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}
