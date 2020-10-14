package com.srnpr.xmaspay.common;

/**
 * 卖家编号
 * @author pang_jhui
 *
 */
public enum SellerCodeEnum {
	
	/**跨境通*/
	KJT("SF03KJT"),
	/**惠家有*/
	FAMILYHAS("SI2003"),
	/**沙皮狗*/
	SHAPIGOU("SI3003"),
	/**商户*/
	SHANGHU("SF031"),
	/**家有汇*/
	HPOOL("SI2009");
	
	/*卖家编号*/
	private String sellerCode = "";

	/**
	 * 获取卖家编号
	 * @return 买家编号
	 */
	public String getSellerCode() {
		return sellerCode;
	}
	
	
	SellerCodeEnum(String sellerCode){
		
		this.sellerCode = sellerCode;
		
	}
	

}
