package com.srnpr.xmasorder.model.kaola;

public class BookKaolaOrderResponse {

	private int recCode;
	
	private String recMeg;
	
	private KaolaOrder gorder = new KaolaOrder();

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

	public KaolaOrder getGorder() {
		return gorder;
	}

	public void setGorder(KaolaOrder gorder) {
		this.gorder = gorder;
	}
	
	
}
