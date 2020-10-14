package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;

/**
 * 记录分销订单明细
 * 包含：普通分销、北呼分销
 */
public class TeslaMakeAgentOrder  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		
		// 优先记录外呼分销订单
		Map<String, String> beihuSkuMap = waihuFxOrder(teslaOrder);
		
		// 然后再统计普通分销订单
		if(teslaOrder.getActivityAgent() != null && StringUtils.isNotBlank(teslaOrder.getActivityAgent().getActivityCode())) {
			List<MDataMap> settingList = DbUp.upTable("fh_agent_profit_setting").queryAll("", "", "", new MDataMap());
			if(!settingList.isEmpty()) {
				Set<String> fxOrderList = new HashSet<String>();
				MDataMap profitSetting = settingList.get(0);
				for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
					// 忽略非分销商品
					if(detail.getFxFlag() != 1 
							|| !detail.getGiftFlag().equals("1")
							|| StringUtils.isBlank(detail.getFxrcode())) {
						continue;
					}
					
					// 外呼分销的订单不计入普通分销
					if(beihuSkuMap.containsKey(detail.getSkuCode())) {
						continue;
					}
					
					MDataMap agentInfo = DbUp.upTable("fh_agent_member_info").one("member_code",detail.getFxrcode());
					
					// 分销人不存在则忽略
					if(agentInfo == null) {
						continue;
					}
					
					// 以SKU档案价为准
				    MDataMap fxSkuInfo = DbUp.upTable("pc_skuinfo").oneWhere("sell_price,cost_price", "", "", "sku_code", detail.getSkuCode());
				    BigDecimal fxSellPrice = new BigDecimal(fxSkuInfo.get("sell_price"));
				    BigDecimal fxCostPrice = new BigDecimal(fxSkuInfo.get("cost_price"));
				    
				    // 0成本的情况不应该存在
				    if(fxCostPrice.compareTo(BigDecimal.ZERO) <= 0) {
				    	continue;
				    }
					
					// 利润 = 原始售价 - 成本
					BigDecimal profit = fxSellPrice.subtract(fxCostPrice);
					
					if("SI2003".equals(detail.getSmallSellerCode())) {
						// LD商品增加10%成本
						profit = fxSellPrice.subtract(fxCostPrice.multiply(new BigDecimal("1.1")));
					}
					
					// 忽略负毛利的商品
					if(profit.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}
					
					// 如果分销人的上级同时也是外呼分销人则不给上级分销利润
					if(StringUtils.isNotBlank(agentInfo.get("parent_code"))) {
						if(DbUp.upTable("fh_waihu_member_info").count("member_code", agentInfo.get("parent_code")) > 0) {
							// 默认为分销人没有上级
							agentInfo.put("parent_code","");
						}
					}
					
					// 标记外呼分销人商品
					beihuSkuMap.put(detail.getSkuCode(), detail.getOrderCode());
					
					MDataMap dataMap = new MDataMap();
					dataMap.put("order_code", detail.getOrderCode());
					dataMap.put("buyer_code", teslaOrder.getUorderInfo().getBuyerCode());
					dataMap.put("agent_code", detail.getFxrcode());
					dataMap.put("agent_parent_code", agentInfo.get("parent_code"));
					dataMap.put("product_code", detail.getProductCode());
					dataMap.put("sku_code", detail.getSkuCode());
					dataMap.put("sku_num", detail.getSkuNum()+"");
					dataMap.put("sku_price", detail.getSkuPrice().toString());
					dataMap.put("cost_price", detail.getCostPrice().toString());
					dataMap.put("profit_money", profit.toString());
					dataMap.put("agent_profit_rate", profitSetting.get("agent_rate"));
					dataMap.put("agent_parent_rate", profitSetting.get("fans_rate"));
					dataMap.put("create_time", teslaOrder.getUorderInfo().getCreateTime());
					DbUp.upTable("fh_agent_order_detail").dataInsert(dataMap);
					fxOrderList.add(detail.getOrderCode());
				}
				
				// 分销订单下单时插入定时任务
				for(String orderCode : fxOrderList) {
					JobExecHelper.createExecInfo("449746990024", orderCode, null);
				}
			}
		}
		teslaOrder.setFxFlagMap(beihuSkuMap);
		return xResult;
	}
	
	// 记录外呼分销的订单
	private Map<String,String> waihuFxOrder(TeslaXOrder teslaOrder) {
		Map<String,String> map = new HashMap<String, String>();
		
		// 是否外呼人员自己下单
		boolean isWaihuBuyer = DbUp.upTable("fh_waihu_member_info").count("member_code", teslaOrder.getUorderInfo().getBuyerCode()) > 0;
		for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
			// 外呼的分销订单
			boolean isWaihuFxr = false;
			
			if(StringUtils.isNotBlank(detail.getFxrcode())) {
				isWaihuFxr = DbUp.upTable("fh_waihu_member_info").count("member_code", detail.getFxrcode()) > 0;
			}
			
			// 如果不是外呼人员下单，也不是外呼的分享下单则忽略
			if(!isWaihuBuyer && !isWaihuFxr) {
				continue;
			}
			
			// 如果分销人不是外呼人员则设置分销人为自己
			if(!isWaihuFxr) {
				detail.setFxrcode(teslaOrder.getUorderInfo().getBuyerCode());
			}
			
			// 标记外呼分销人商品
			map.put(detail.getSkuCode(), detail.getOrderCode());
			
			// 记录外呼分销人订单
			MDataMap dataMap = new MDataMap();
			dataMap.put("member_code", detail.getFxrcode());
			dataMap.put("order_code", detail.getOrderCode());
			dataMap.put("sku_code", detail.getSkuCode());
			dataMap.put("create_time", teslaOrder.getUorderInfo().getCreateTime());
			DbUp.upTable("fh_waihu_order_detail").dataInsert(dataMap);
			
			// 记录外呼人员的绑定关系
			waihuBind(detail.getFxrcode(), teslaOrder.getUorderInfo().getBuyerCode());
		}
		return map;
	}
	
	/**
	 * 外呼人员推广绑定
	 * @param waihuCode
	 * @param buyerCode
	 */
	private void waihuBind(String waihuCode, String buyerCode) {
		if(waihuCode.equals(buyerCode)) {
			return;
		}
		
		Date now = new Date();
		Date expiredDate = DateUtils.addDays(now, 90);
		String sNow = FormatHelper.upDateTime(now, "yyyy-MM-dd HH:mm:ss");
		String sExpiredDate = FormatHelper.upDateTime(expiredDate, "yyyy-MM-dd HH:mm:ss");
		
		MDataMap map = DbUp.upTable("fh_waihu_bind").one("member_code", buyerCode);
		// 如果不存在绑定关系则进行绑定记录
		if(map == null) {
			map = new MDataMap();
			map.put("member_code", buyerCode);
			map.put("waihu_code", waihuCode);
			map.put("expired_time", sExpiredDate);
			map.put("create_time", sNow);
			DbUp.upTable("fh_waihu_bind").dataInsert(map);
			return;
		}
		
		// 如果存在且未过期则再延迟3个月
		if(sNow.compareTo(map.get("expired_time")) < 0) {
			map.put("expired_time", sExpiredDate);
			DbUp.upTable("fh_waihu_bind").dataUpdate(map, "expired_time", "zid");
		} else {
			// 如果已经过期则删除重新创建绑定关系
			DbUp.upTable("fh_waihu_bind").delete("zid", map.get("zid"));
			
			map = new MDataMap();
			map.put("member_code", buyerCode);
			map.put("waihu_code", waihuCode);
			map.put("expired_time", sExpiredDate);
			map.put("create_time", sNow);
			DbUp.upTable("fh_waihu_bind").dataInsert(map);
		}
		
	}

}
