package com.srnpr.xmasorder.model.jingdong;

/**
 * 1.1.15	商品可售验证接口 扩展信息
 * @remark 
 * @author 任宏斌
 * @date 2019年5月28日
 */
public enum SkuStateQueryExts {

	/**无理由退货类型*/
	noReasonToReturn,
	/**无理由退货文案类型*/
	thwa,
	/**是否自营*/
	isSelf,
	/**是否京东配送*/
	isJDLogistics,
	/**商品税率*/
	taxInfo
}
