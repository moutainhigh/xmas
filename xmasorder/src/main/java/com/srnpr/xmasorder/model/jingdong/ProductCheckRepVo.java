package com.srnpr.xmasorder.model.jingdong;

import java.math.BigDecimal;

/**
 * 1.1.15	商品可售验证接口 返回VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class ProductCheckRepVo {

	/**
	 * 商品编号
	 */
	private String skuId = "";
	
	/**
	 * 商品名称
	 */
	private String name = "";
	
	/**
	 * 是否可售，1：是，0：否
	 */
	private int saleState = 0;
	
	/**
	 * 是否可开增票，1：支持，0：不支持
	 */
	private int isCanVAT = 0;
	
	/**
	 * 此字段已废弃！
	 * 是否支持7天退货，1：是，0：不支持 
	 */
	private int is7ToReturn = 0;
	
	/**
	 * 无理由退货类型：0,1,2,3,4,5,6,7,8
	 *	0、3：不支持7天无理由退货；
	 *	1、5、8或null：支持7天无理由退货；
	 *	2：支持90天无理由退货；
	 *	4、7：支持15天无理由退货；
	 *	6：支持30天无理由退货；
	 *	（取到其他枚举值，无效）
	 */
	private int noReasonToReturn = -1;

	/**
	 * 无理由退货文案类型：
	 * null：文案空；
	 * 0：文案空；
	 * 1：支持7天无理由退货；
	 * 2：支持7天无理由退货（拆封后不支持）；
	 * 3：支持7天无理由退货（激活后不支持）
	 * 4：支持7天无理由退货（使用后不支持）；
	 * 5：支持7天无理由退货（安装后不支持）；
	 * 12：支持15天无理由退货；
	 * 13：支持15天无理由退货（拆封后不支持）；
	 * 14：支持15天无理由退货（激活后不支持）；
	 * 15：支持15天无理由退货（使用后不支持）；
	 * 16：支持15天无理由退货（安装后不支持）；
	 * 22：支持30天无理由退货；
	 * 23：支持30天无理由退货（安装后不支持）；
	 * 24：支持30天无理由退货（拆封后不支持）；
	 * 25：支持30天无理由退货（使用后不支持）；
	 * 26：支持30天无理由退货（激活后不支持）；
	 * （提示客户取到其他枚举值，无效）
	 */
	private int thwa = -1;
	
	/**
	 * 是否京东自营，1：是，0：否
	 */
	private int isSelf = -1;
	
	/**
	 * 是否京东配送，1：是，0：否
	 */
	private int isJDLogistics = -1;
	
	/**
	 * 商品税率，例如：本参数值返回13，意味着税率为13
	 */
	private BigDecimal taxInfo = BigDecimal.ZERO; 
	
	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSaleState() {
		return saleState;
	}

	public void setSaleState(int saleState) {
		this.saleState = saleState;
	}

	public int getIsCanVAT() {
		return isCanVAT;
	}

	public void setIsCanVAT(int isCanVAT) {
		this.isCanVAT = isCanVAT;
	}

	public int getIs7ToReturn() {
		return is7ToReturn;
	}

	public void setIs7ToReturn(int is7ToReturn) {
		this.is7ToReturn = is7ToReturn;
	}

	public int getNoReasonToReturn() {
		return noReasonToReturn;
	}

	public void setNoReasonToReturn(int noReasonToReturn) {
		this.noReasonToReturn = noReasonToReturn;
	}

	public int getThwa() {
		return thwa;
	}

	public void setThwa(int thwa) {
		this.thwa = thwa;
	}

	public int getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(int isSelf) {
		this.isSelf = isSelf;
	}

	public int getIsJDLogistics() {
		return isJDLogistics;
	}

	public void setIsJDLogistics(int isJDLogistics) {
		this.isJDLogistics = isJDLogistics;
	}

	public BigDecimal getTaxInfo() {
		return taxInfo;
	}

	public void setTaxInfo(BigDecimal taxInfo) {
		this.taxInfo = taxInfo;
	}
	
}
