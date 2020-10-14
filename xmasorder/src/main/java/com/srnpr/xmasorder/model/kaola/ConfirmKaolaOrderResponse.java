package com.srnpr.xmasorder.model.kaola;

public class ConfirmKaolaOrderResponse {
	private int recCode;
	
	private String recMeg;
	
	private OrderForm orderForm;

	public int getRecCode() {
		return recCode;
	}

	public void setRecCode(int recCode) {
		this.recCode = recCode;
	}

	public String getRecMeg() {
		return recMeg;
	}

	public void setRecMeg(String recMeg) {
		this.recMeg = recMeg;
	}

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;
	}
}
