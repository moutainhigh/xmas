package com.srnpr.xmasorder.model.jingdong;

/**
 * 库存查询接口 请求VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class SkuNum {

	private String skuId;
	
	private int num;

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
	
	
}
