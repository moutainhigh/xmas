package com.srnpr.xmasorder.model.jingdong;

/**
 * 下单时的sku信息
 * @remark 
 * @author 任宏斌
 * @date 2019年5月15日
 */
public class SkuModel {

	private String skuId = "";
	
	private int num = 0;
	
	/**
	 * 表示是否需要附件，默认每个订单都给附件，默认值为：true，
	 * 如果客户实在不需要附件bNeedAnnex可以给false，
	 * 该参数配置为false时请谨慎，真的不会给客户发附件的
	 */
	private boolean bNeedAnnex = true;
	
	/**
	 * 表示是否需要赠品，如赠品无货，可以给false，不影响主商品下单
	 */
	private boolean bNeedGift = true;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isbNeedAnnex() {
		return bNeedAnnex;
	}

	public void setbNeedAnnex(boolean bNeedAnnex) {
		this.bNeedAnnex = bNeedAnnex;
	}

	public boolean isbNeedGift() {
		return bNeedGift;
	}

	public void setbNeedGift(boolean bNeedGift) {
		this.bNeedGift = bNeedGift;
	}
	
}
