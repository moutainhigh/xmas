package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 记录推广赚订单明细
 */
public class TeslaMakeTgzOrder  extends TeslaTopOrderMake {

	HjycoinService hjycoinService = new HjycoinService();
	LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
	
		// 推广配置
		MDataMap fxConfigMap = hjycoinService.getTgzTypeConfigMap(HjycoinService.TGZ_TYPE_FX);
		// 买家秀配置
		MDataMap showConfigMap = hjycoinService.getTgzTypeConfigMap(HjycoinService.TGZ_TYPE_SHOW);
		
		if(fxConfigMap == null && showConfigMap == null) {
			return xResult;
		}
		
		Set<String> hjycoinOrderCodeList = new HashSet<String>();
		for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
			// 忽略赠品
			if(!detail.getGiftFlag().equals("1")) {
				continue;
			}
			
			// 忽略没有推广人的
			if(StringUtils.isBlank(detail.getTgzShowCode()) && StringUtils.isBlank(detail.getTgzUserCode())) {
				continue;
			}
			
			// 忽略分销订单
			if(teslaOrder.getFxFlagMap().containsKey(detail.getSkuCode())) {
				continue;
			}
			
			MDataMap configMap = null;
			String tgzType = "";
			String tgzMemberCode = "";
			if(StringUtils.isNotBlank(detail.getTgzUserCode())) {
				configMap = fxConfigMap;
				tgzType = HjycoinService.TGZ_TYPE_FX;
				tgzMemberCode = detail.getTgzUserCode();
			} else {
				configMap = showConfigMap;
				tgzType = HjycoinService.TGZ_TYPE_SHOW;
				tgzMemberCode = (String)DbUp.upTable("nc_buyer_show_info").dataGet("member_code", "", new MDataMap("uid", detail.getTgzShowCode()));
				
				if(StringUtils.isBlank(tgzMemberCode)) {
					continue;
				}
			}
			
			// 忽略自己
			if(tgzMemberCode.equalsIgnoreCase(teslaOrder.getUorderInfo().getBuyerCode())) {
				continue;
			}
			
			// 忽略收益配置关闭的情况
			if(configMap == null) {
				continue;
			}
			
			BigDecimal skuNum = new BigDecimal(detail.getSkuNum());
			
			PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(detail.getProductCode()));
			BigDecimal shipmentMoney = BigDecimal.ZERO;
			
			// 非厂配的LD商品需要计算配送费
			if(!"Y".equals(productInfo.getCspsFlag()) && "SI2003".equals(detail.getSmallSellerCode())) {
				shipmentMoney = hjycoinService.getShipmentMoney();
				shipmentMoney = shipmentMoney.multiply(new BigDecimal(detail.getSkuNum()));
			}
			
			// 毛利 = 销售额 - 积分 - 惠币 - 储值金 - 成本 - 预估配送费
			BigDecimal profitMoney = detail.getSkuPrice().multiply(skuNum)
					.subtract(detail.getIntegralMoney())
					.subtract(detail.getHjycoin())
					.subtract(detail.getCzjMoney())
					.subtract(detail.getCostPrice().multiply(skuNum))
					.subtract(shipmentMoney);
			
			// LD商品额外减去10%成本
			if("SI2003".equals(detail.getSmallSellerCode())) {
				profitMoney = profitMoney.subtract(detail.getCostPrice().multiply(skuNum).multiply(new BigDecimal("0.1")));
			}
			
			BigDecimal tgzMoney = hjycoinService.getProductTgzMoney(profitMoney, skuNum.intValue(), configMap.get("tgz_rate"));
			
			// 忽略负毛利商品
			if(profitMoney.compareTo(BigDecimal.ZERO) <= 0 || tgzMoney.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			
			MDataMap dataMap = new MDataMap();
			dataMap.put("tgz_type", tgzType);
			dataMap.put("buyer_show_code", detail.getTgzShowCode());
			dataMap.put("order_code", detail.getOrderCode());
			dataMap.put("small_seller_code", detail.getSmallSellerCode());
			dataMap.put("buyer_code", teslaOrder.getUorderInfo().getBuyerCode());
			dataMap.put("tgz_member_code", tgzMemberCode);
			dataMap.put("product_code", detail.getProductCode());
			dataMap.put("sku_code", detail.getSkuCode());
			dataMap.put("sku_num", detail.getSkuNum()+"");
			dataMap.put("sku_price", detail.getSkuPrice().toString());
			dataMap.put("show_price", detail.getShowPrice().toString());
			dataMap.put("shipment_money", shipmentMoney.toString());
			dataMap.put("profit_money", profitMoney.toString());
			dataMap.put("tgz_rate", configMap.get("tgz_rate"));
			dataMap.put("tgz_money", tgzMoney.toString());
			dataMap.put("create_time", teslaOrder.getUorderInfo().getCreateTime());
			dataMap.put("update_time", teslaOrder.getUorderInfo().getCreateTime());
			DbUp.upTable("fh_tgz_order_detail").dataInsert(dataMap);
			
			// 如果是商户品则需要插入预估收益的定时
			if(!"SI2003".equals(detail.getSmallSellerCode())) {
				hjycoinOrderCodeList.add(detail.getOrderCode());
			}
		}
		
		// 插入惠币预估收益定时
		for(String code : hjycoinOrderCodeList) {
			teslaOrder.getExecInfoMap().put("449746990041", code);
		}
		
		return xResult;
	}

}
