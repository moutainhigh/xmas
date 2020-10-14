package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车商品活动
*    xiegj
*/
public class ShoppingCartEvent  {
	
	/** 件数凑单 */
	public static final int FULL_MONEY_TYPE_MONEY = 1;
	/** 金额凑单 */
	public static final int FULL_MONEY_TYPE_SKUNUM = 2;
	
	@ZapcomApi(value = "活动块描述", remark = "", demo = "满200减20")
	private String description = "";
	
	@ZapcomApi(value = "活动块标签", remark = "活动块标签", demo = "满减")
	private String tagname = "";
	
	@ZapcomApi(value="活动跳转类型",remark="100001:满减活动原生页；100002：满减活动专题页")
	private String forwardType = "";
	
	@ZapcomApi(value="活动跳转链接")
	private String forwardVal = "" ;
	
	@ZapcomApi(value="满减名称标签的背景色",remark="默认值为黄色(#fa6565)")
	private String eventNameColor = "#fa6565";
	
	@ZapcomApi(value="满减凑单还需购买的金额/件数")
	private String addFullMoney = "";
	
	@ZapcomApi(value="满减凑单类型",remark="1 金额、2 件数")
	private int addFullMoneyType = 0;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getForwardType() {
		return forwardType;
	}

	public void setForwardType(String forwardType) {
		this.forwardType = forwardType;
	}

	public String getForwardVal() {
		return forwardVal;
	}

	public void setForwardVal(String forwardVal) {
		this.forwardVal = forwardVal;
	}

	public String getEventNameColor() {
		return eventNameColor;
	}

	public void setEventNameColor(String eventNameColor) {
		this.eventNameColor = eventNameColor;
	}

	public String getAddFullMoney() {
		return addFullMoney;
	}

	public void setAddFullMoney(String addFullMoney) {
		this.addFullMoney = addFullMoney;
	}

	public int getAddFullMoneyType() {
		return addFullMoneyType;
	}

	public void setAddFullMoneyType(int addFullMoneyType) {
		this.addFullMoneyType = addFullMoneyType;
	}
	
}

