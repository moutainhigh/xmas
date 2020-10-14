package com.srnpr.xmasorder.model.jingdong;

/**
 * 1.1.11	查询商品区域购买限制接口 返回VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class CheckAreaLimitVO {

	private String skuId;
	
	/**
	 * True 区域限制 false 不受区域限制
	 */
	private boolean isAreaRestrict;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public boolean isAreaRestrict() {
		return isAreaRestrict;
	}

	public void setAreaRestrict(boolean isAreaRestrict) {
		this.isAreaRestrict = isAreaRestrict;
	}
	
	
}
