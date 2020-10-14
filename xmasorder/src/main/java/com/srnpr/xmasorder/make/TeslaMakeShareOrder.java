package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;

/**
 * 
 * @author Angel Joy
 * @date 2020年6月11日 下午5:56:24
 * @version 
 * @desc 7月推广，分享下单。
 */
public class TeslaMakeShareOrder  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		
		List<MDataMap> settingList = DbUp.upTable("fh_share_profit_setting").queryAll("", "", "", new MDataMap());
		Map<String,String> beihuSkuMap = teslaOrder.getFxFlagMap();
		LoadProductInfo load = new LoadProductInfo();
		if(!settingList.isEmpty()) {
			Set<String> fxOrderList = new HashSet<String>();
			for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
				if(!detail.getGiftFlag().equals("1")) {//商品为赠品或者分销人编码有值的情况下，不计入推广订单明细
					continue;
				}
				//判断是否是分销商品，如果是分销商品，跳过逻辑，如果非分销商品。继续执行
				if(beihuSkuMap.containsKey(detail.getSkuCode())) {
					continue;
				}
				if(StringUtils.isBlank(detail.getShareCode())) {//推广人为空的情况下，不计入推广订单明细
					continue;
				}
			    BigDecimal shareSellPrice = detail.getShowPrice();
			    BigDecimal shareCostPrice = detail.getCostPrice();
			    
			    // 0成本的情况不应该存在
			    if(shareCostPrice.compareTo(BigDecimal.ZERO) <= 0) {
			    	continue;
			    }
			    
			    if(teslaOrder.getUorderInfo().getBuyerCode().equals(detail.getShareCode())) {//分享人与购买人是同一个人时，过滤
			    	continue;
			    }
				if("SI2003".equals(detail.getSmallSellerCode())) {
					// LD商品增加10%成本
					shareCostPrice = shareCostPrice.multiply(new BigDecimal("1.1"));
					PlusModelProductInfo productInfo = load.upInfoByCode(new PlusModelProductQuery(detail.getProductCode()));
					String VlOrs = "";
					if(productInfo != null) {
						VlOrs = productInfo.getVlOrs();
					}
					if("N".equals(VlOrs)) {//非一件代发产品，成本需要加50
						shareCostPrice = shareCostPrice.add(new BigDecimal("50"));
					}
				}
				
				// 利润 = 原始售价 - 成本
				BigDecimal profit = shareSellPrice.subtract(shareCostPrice);
				
				// 忽略毛利低于三十的商品
				if(profit.compareTo(new BigDecimal(bConfig("xmasorder.share_profit"))) <= 0) {
					continue;
				}
				
				MDataMap dataMap = new MDataMap();
				dataMap.put("order_code", detail.getOrderCode());
				dataMap.put("buyer_code", teslaOrder.getUorderInfo().getBuyerCode());
				dataMap.put("share_member_code", detail.getShareCode());
				dataMap.put("product_code", detail.getProductCode());
				dataMap.put("sku_code", detail.getSkuCode());
				dataMap.put("sku_num", detail.getSkuNum()+"");
				dataMap.put("sku_price", shareSellPrice.toString());
				dataMap.put("cost_price", shareCostPrice.toString());
				dataMap.put("profit_money", profit.toString());
				dataMap.put("create_time", teslaOrder.getUorderInfo().getCreateTime());
				DbUp.upTable("fh_share_order_detail").dataInsert(dataMap);
				
				fxOrderList.add(detail.getOrderCode());
			}
			
		}
	
		return xResult;
	}
	
}
