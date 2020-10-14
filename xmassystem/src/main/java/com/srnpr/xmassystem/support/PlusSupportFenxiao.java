package com.srnpr.xmassystem.support;

import java.math.BigDecimal;

import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class PlusSupportFenxiao {

	static LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
	
	/**
	 * 计算分销金额
	 * @param skuCode
	 * @return
	 */
	public BigDecimal getSkuFenxiaoMoney(String skuCode) {
		BigDecimal fxMoney = BigDecimal.ZERO;
		// 以SKU档案价为准
	    PlusModelSkuQuery query = new PlusModelSkuQuery();
	    query.setCode(skuCode);
	    PlusModelSkuInfo skuInfo = loadSkuInfo.upInfoByCode(query);
	    
	    fxMoney = getFenxiaoMoney(skuInfo.getSkuPrice(), skuInfo.getCostPrice(), skuInfo.getSmallSellerCode());
	    return fxMoney;
	}
	
	/**
	 * 计算商品最小分销金额
	 * @param skuCode
	 * @return
	 */
	public BigDecimal getProductMinFenxiaoMoney(String productCode) {
		BigDecimal fxMoney = BigDecimal.ZERO;
		MDataMap m = DbUp.upTable("pc_skuinfo").oneWhere("sku_code", "sell_price asc", "", "product_code", productCode, "sale_yn", "Y");
		if(m != null) {
			fxMoney = getSkuFenxiaoMoney(m.get("sku_code"));
		}
	    return fxMoney;
	}
	
	/**
	 * 分销收益计算
	 * @param sellPrice
	 * @param costPrice
	 * @param smallSellerCode
	 * @return
	 */
	public BigDecimal getFenxiaoMoney(BigDecimal sellPrice, BigDecimal costPrice, String smallSellerCode) {
		BigDecimal fxMoney = BigDecimal.ZERO;
		
		// 利润 = 原始售价 - 成本
		BigDecimal profit = sellPrice.subtract(costPrice);
		
		if("SI2003".equalsIgnoreCase(smallSellerCode)) {
			// LD商品增加10%成本
			profit = sellPrice.subtract(costPrice.multiply(new BigDecimal("1.1")));
		}
		
	    // 售价高于成本价时再计算分销收益
	    if(profit.compareTo(BigDecimal.ZERO) > 0) {
	    	MDataMap settingMap = DbUp.upTable("fh_agent_profit_setting").oneWhere("agent_rate", "", "");
	    	if(settingMap != null) {
	    		fxMoney = profit.multiply(new BigDecimal(settingMap.get("agent_rate"))).setScale(1, BigDecimal.ROUND_HALF_UP);
	    	}
	    }
	    
	    return fxMoney;
	}
	
	/**
	 * 计算商品分销优惠券金额信息
	 * @param sellPrice
	 * @param costPrice
	 * @param smallSellerCode
	 * @return
	 */
	public MDataMap getFenxiaoCouponInfo(String productCode) {
		MDataMap priceMap = DbUp.upTable("pc_skuinfo").oneWhere("min(sell_price) sell_price,min(cost_price) cost_price", "", "", "product_code", productCode);
		String smallSellerCode = (String)DbUp.upTable("pc_productinfo").dataGet("small_seller_code", "", new MDataMap("product_code", productCode));
		
		BigDecimal sellPrice = new BigDecimal(priceMap.get("sell_price"));
		BigDecimal costPrice = new BigDecimal(priceMap.get("cost_price"));
		// 利润 = 原始售价 - 成本
		BigDecimal profit = sellPrice.subtract(costPrice);
		
		BigDecimal couponMoney = BigDecimal.ZERO;
		
		if("SI2003".equals(smallSellerCode)) {
			// LD商品增加10%成本
			profit = sellPrice.subtract(costPrice.multiply(new BigDecimal("1.1")));
		}
		
	    // 售价高于成本价时再计算分销收益
	    if(profit.compareTo(BigDecimal.ZERO) > 0) {
	    	MDataMap settingMap = DbUp.upTable("fh_agent_profit_setting").oneWhere("coupon_rate", "", "");
	    	if(settingMap != null) {
	    		couponMoney = profit.multiply(new BigDecimal(settingMap.get("coupon_rate"))).setScale(1, BigDecimal.ROUND_HALF_UP);
	    	}
	    }
	    
	    priceMap.put("coupon_money", couponMoney.toString());
	    return priceMap;
	}
	
}
