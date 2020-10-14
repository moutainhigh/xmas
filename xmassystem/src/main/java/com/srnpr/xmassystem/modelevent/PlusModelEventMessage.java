package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品详情页面满减或组合购促销广告语实体类 
 *  用于满减和组合购
 * @author zhouguohui
 *
 */
public class PlusModelEventMessage {

	List<PlusModelFullCutMessage> saleMessage = new ArrayList<PlusModelFullCutMessage>();

	/**
	 * @return the saleMessage
	 */
	public List<PlusModelFullCutMessage> getSaleMessage() {
		return saleMessage;
	}

	/**
	 * @param saleMessage the saleMessage to set
	 */
	public void setSaleMessage(List<PlusModelFullCutMessage> saleMessage) {
		this.saleMessage = saleMessage;
	}
	
	
 
}
