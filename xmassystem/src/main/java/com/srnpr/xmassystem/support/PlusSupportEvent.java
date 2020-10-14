package com.srnpr.xmassystem.support;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadEventFree;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadEventPurchase;
import com.srnpr.xmassystem.load.LoadEventSale;
import com.srnpr.xmassystem.load.LoadGoodsProduct;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFree;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelEventGoodsProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventNgProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventPurchase;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFreeQuery;
import com.srnpr.xmassystem.modelevent.PlusModelFreeShipping;
import com.srnpr.xmassystem.modelevent.PlusModelGoodsProduct;
import com.srnpr.xmassystem.modelevent.PlusModelNgProduct;
import com.srnpr.xmassystem.modelevent.PlusModelPurchase;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelevent.PlusModelVipDiscount;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServicePurchase;
import com.srnpr.xmassystem.service.PlusServiceVipDiscount;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import org.apache.commons.lang3.math.NumberUtils;
import com.srnpr.zapcom.topdo.TopConfig;
public class PlusSupportEvent {

	/**
	 * 根据活动明细编号获取活动明细信息
	 * 
	 * @param sIcCode
	 * @return
	 */
	public PlusModelEventItemProduct upItemProductByIcCode(String sIcCode) {

		PlusModelEventItemProduct pResult = new PlusModelEventItemProduct();

		String sSubCode = PlusHelperEvent.upSubEvent(sIcCode);

		if (StringUtils.isBlank(sSubCode)) {
			PlusModelEventItemQuery pQuery = new PlusModelEventItemQuery();

			pQuery.setCode(sIcCode);

			pResult = new LoadEventItemProduct().upInfoByCode(pQuery);
		} else {

			// 初始化一个信息 以供返回的调用
			PlusModelSkuInfo pSkuInfo = new PlusSupportProduct()
					.upSkuInfoBySkuCode(sIcCode);

			pResult.setEventCode(pSkuInfo.getEventCode());
			pResult.setItemCode(pSkuInfo.getItemCode());
			pResult.setProductCode(pSkuInfo.getProductCode());
			pResult.setSalesStock(pSkuInfo.getMaxBuy());
			pResult.setSkuCode(pSkuInfo.getSkuCode());

		}

		return pResult;

	}
	
	/**
	 * 根据促销活动编号设置活动信息
	 * @param plusModelEventInfo
	 * @param eventCode
	 */
	public void upEventInfoFromDB(PlusModelEventInfo plusModelEventInfo, String eventCode) {
		MDataMap mDataMap = DbUp.upTable("sc_event_info").one("event_code", eventCode);

		plusModelEventInfo.setEventCode(mDataMap.get("event_code"));
		plusModelEventInfo.setBeginTime(mDataMap.get("begin_time"));
		plusModelEventInfo.setEndTime(mDataMap.get("end_time"));
		plusModelEventInfo.setEventName(mDataMap.get("event_name"));
		plusModelEventInfo.setDescriptionUrl((mDataMap.get("description_url")==null||mDataMap.get("description_url").equals(""))?null:mDataMap.get("description_url"));
		plusModelEventInfo.setEventType(mDataMap.get("event_type_code"));
		plusModelEventInfo.setEventStatus(mDataMap.get("event_status"));
		plusModelEventInfo.setCollagePersonCount(mDataMap.get("collage_person_count"));
		plusModelEventInfo.setIsSuprapositionFlag(NumberUtils.toInt(mDataMap.get("is_supraposition_flag")));
		plusModelEventInfo.setSuprapositionType(mDataMap.get("supraposition_type"));
		plusModelEventInfo.setChannels(mDataMap.get("channels"));
		plusModelEventInfo.setOutActiveCode(mDataMap.get("out_active_code"));
		plusModelEventInfo.setSortOrder(mDataMap.get("sort_order"));
		plusModelEventInfo.setEventDiscount(new BigDecimal(mDataMap.get("event_discount")));
		plusModelEventInfo.setMaxDiscountMoney(new BigDecimal(mDataMap.get("max_discount_money")));
		plusModelEventInfo.setEventTipName(mDataMap.get("event_tip_name"));
		plusModelEventInfo.setEventDescription(mDataMap.get("event_description"));
		String collageTimeliness = mDataMap.get("collage_timeliness");
		if(StringUtils.isEmpty(collageTimeliness)) {
			collageTimeliness = "-1";
		}
		plusModelEventInfo.setCollageTimeLiness(Integer.parseInt(collageTimeliness));
		Long lOrderAging = Long.parseLong(mDataMap.get("order_aging"));
		// 开始设置订单时效
		if (lOrderAging > 0) {
			String sTimeType = mDataMap.get("time_category");
			if (sTimeType.equals("449747280001")) {
				plusModelEventInfo.setCancelTime(lOrderAging * 3600);
			} else if (sTimeType.equals("449747280002")) {
				plusModelEventInfo.setCancelTime(lOrderAging * 60);
			} else if (sTimeType.equals("449747280003")) {
				plusModelEventInfo.setCancelTime(lOrderAging);
			}
		}

		MDataMap mTypeMap = DbUp.upTable("sc_event_type").one("type_code",plusModelEventInfo.getEventType());
		if(mTypeMap != null) {
			//打折促销用自定义标签名
			if("4497472600010030".equals(mTypeMap.get("type_code"))) {
				plusModelEventInfo.setEventNote(mDataMap.get("event_tip_name"));
			}else if("4497472600010024".equals(mTypeMap.get("type_code"))){
				String collageType = mDataMap.get("collage_type");
				if("4497473400050001".equals(collageType)) {//普通团
					plusModelEventInfo.setEventNote("拼团");
				}else {
					plusModelEventInfo.setEventNote("邀新团");
				}
			}else {
				plusModelEventInfo.setEventNote(mTypeMap.get("type_name"));
			}
			plusModelEventInfo.setEventExecClass(mTypeMap.get("class_name"));
		}
		if (mDataMap.get("uniform_price").equals("449746880001")) {
			plusModelEventInfo.setScopePrice(1);
		}
	}

	/**
	 * 获取SKU上的所有有效的活动信息
	 * 
	 * @param plusSku
	 * @param plusQuery
	 */
	public void upSkuEventFromDB(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery) {

		String sSkuCode = plusSku.getSkuCode();

		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> aItem = new ArrayList<String>();
		ArrayList<String> aType = new ArrayList<String>();

		String sql="SELECT pro.event_code,pro.item_code,inf.event_type_code FROM sc_event_item_product pro,sc_event_info inf"+
				   " WHERE pro.event_code = inf.event_code AND pro.sku_code =:skuCode"+
				   " AND (inf.event_status='4497472700020002' OR inf.event_status='4497472700020004')"+
				   " AND inf.end_time > now() AND pro.flag_enable = 1";
		
		for (Map<String, Object> map :DbUp.upTable("sc_event_item_product").dataSqlList(sql, new MDataMap("skuCode",sSkuCode))) {

			aList.add(map.get("event_code").toString());
			aItem.add(map.get("item_code").toString());
			aType.add(map.get("event_type_code").toString());

		}
		
		//补充商品参加促销活动 活动明细未记录sku编号的情况 暂时仅支持打折促销
		String sql2 = "SELECT pro.event_code,pro.item_code,inf.event_type_code FROM sc_event_item_product pro,sc_event_info inf " + 
				" WHERE pro.event_code = inf.event_code AND pro.product_code =:product_code " + 
				" AND inf.event_type_code='4497472600010030' " + 
				" AND (inf.event_status='4497472700020002' OR inf.event_status='4497472700020004') " + 
				" AND inf.end_time > now() AND pro.flag_enable = 1";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_event_item_product").dataSqlList(sql2, new MDataMap("product_code", plusSku.getProductCode()));
		if(null != dataSqlList && !dataSqlList.isEmpty()) {
			for (Map<String, Object> map : dataSqlList) {
				aList.add(map.get("event_code").toString());
				aItem.add(map.get("item_code").toString());
				aType.add(map.get("event_type_code").toString());
			}
		}
		
		/*for (MDataMap map : DbUp.upTable("sc_event_item_product").queryByWhere(
				"sku_code", sSkuCode, "flag_enable", "1")) {

			aList.add(map.get("event_code"));
			aItem.add(map.get("item_code"));

		}*/

		plusSku.setEventCode(StringUtils.join(aList, ","));
		plusSku.setItemCode(StringUtils.join(aItem, ","));
		plusSku.setEventType(StringUtils.join(aType, ","));
	}
	
	public PlusModelSkuInfo refreshSkuEvent (PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery) {
		LoadEventInfo loadEventInfo = new LoadEventInfo();
		
		PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(plusSku.getProductCode()));
		
		boolean isCheckAcitivity = true;//是否继续检查活动
		
		// 橙意会员卡商品不参与任何促销活动
		if(plusSku.getProductCode().equals(TopConfig.Instance.bConfig("xmassystem.plus_product_code"))) {
			return plusSku;
		}
		
		//必须为LD的商品
		if(productInfo.getSellerCode().equals("SI2003")&&productInfo.getSmallSellerCode().equals("SI2003")&&"0".equals(plusQuery.getIfJJGFlag()) ){
			
			//LD品，首先过滤内购活动
			{
				if(StringUtils.isNotBlank(plusQuery.getMemberCode())) {
					PlusModelEventNgProduct pong = new PlusModelEventNgProduct();
					pong.setMemberCode(plusQuery.getMemberCode());
					pong.setProCostPrice(productInfo.getCost_price());
					pong.setListSku(productInfo.getSkuList());
					List<PlusModelNgProduct> list = new ArrayList<PlusModelNgProduct>();
					
					PlusModelNgProduct ngList = new PlusModelNgProduct();
					ngList.setProductCode(plusSku.getProductCode());
					ngList.setSkuCode(plusSku.getSkuCode());
					ngList.setSkuPrice(plusSku.getSellPrice());
					list.add(ngList);
					
					pong.setNgPro(list);
					
					/**过滤内购活动**/
					PlusModelEventNgProduct pongNew = new PlusServicePurchase().getCartPurchase(pong,"SI2003",plusQuery.getChannelId());
					/**重新添加内购价格和内购活动**/
					if(pongNew.getMap()!=null&&!pongNew.getMap().isEmpty()){
						
						if(pongNew.getMap().get(plusSku.getSkuCode()).isTrue()){
							plusSku.setSellPrice(pongNew.getMap().get(plusSku.getSkuCode()).getSkuPrice());
							plusSku.setEventCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());
							plusSku.setEventType("4497472600010006");
						    plusSku.setItemCode(pongNew.getMap().get(plusSku.getSkuCode()).getItemCode());
						    
						    //添加限购数量显示
						    Integer numHistory = numHistory(plusQuery.getMemberCode(), plusSku.getProductCode());
						    plusSku.setMaxBuy(2-numHistory);
						    plusSku.setLimitBuy(2);
						    
						    isCheckAcitivity = false;
						    
							PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
							plusModelEventQuery.setCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());

							PlusModelEventInfo plusModelEventInfo = loadEventInfo
									.upInfoByCode(plusModelEventQuery);

							// 如果活动有效 且活动的价格为全局影响价格
							if (checkEventEnable(plusModelEventInfo)
									&& (plusModelEventInfo.getScopePrice() == 1)) {
								plusSku.setEventCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());
								plusSku.setItemCode(pongNew.getMap().get(plusSku.getSkuCode()).getItemCode());
								upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
										plusQuery, plusModelEventInfo);


							}
							
						}
					}
				}
				
			}
			
			/*
			 * 过滤是否是LD会员日（此会员日非促销活动 的会员日）
			 */
			{
				if(isCheckAcitivity) {
					
					/**
					 * 过滤会员日之前先判断是否是会员日排除商品，如果是则直接跳下一个循环。
					 */
					
					PlusServiceVipDiscount plusServiceVipDiscount = new PlusServiceVipDiscount();
					Map<String, PlusModelVipDiscount> vipDiscountActivity = plusServiceVipDiscount.getVipDiscountActivity("SI2003", plusQuery.getMemberCode(), plusSku.getProductCode(),plusQuery.getChannelId());
					
					
					PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
					if(vipDiscountActivity.containsKey(plusSku.getProductCode()) ) {
						
						PlusModelVipDiscount curntProductDiscontInfo = vipDiscountActivity.get(plusSku.getProductCode());//当前商品的折扣活动
						
						BigDecimal discont = BigDecimal.valueOf(curntProductDiscontInfo.getDisount()).divide(BigDecimal.valueOf(100));
						//如果当前商品支持会员日活动，在判断该商品是不是经过人工导入，需要排除的商品。NG-Start
						Integer discountOutProduct = DbUp.upTable("sc_event_vipdiscount_exclude_product").count("product_code",plusSku.getProductCode(),"flag_enable","1","event_code",curntProductDiscontInfo.getEventCode());
						//如果存在则不参与会员日折扣活动
						if(discountOutProduct == 0){
							//NG-end
							//设置价格
							BigDecimal curntSkuPrice = plusSku.getSellPrice();//当前sku售价
							BigDecimal computePrice = MoneyHelper.round(0, BigDecimal.ROUND_HALF_UP,discont.multiply(curntSkuPrice));
							BigDecimal maxSaleMoney = curntProductDiscontInfo.getMaxDiscountMoney();
							if(maxSaleMoney.compareTo(BigDecimal.ZERO) > 0 && plusSku.getSkuPrice().subtract(computePrice).compareTo(maxSaleMoney) > 0) {
								computePrice = plusSku.getSkuPrice().subtract(maxSaleMoney);
							};
								
							plusSku.setSellPrice(computePrice);//打折后的价格（四舍五入不保留小数）
							plusSku.setEventCode(curntProductDiscontInfo.getEventCode());
							plusSku.setEventType(curntProductDiscontInfo.getEventType());
							plusSku.setItemCode(curntProductDiscontInfo.getOutActiveCode());
							isCheckAcitivity = false;
							
							plusModelEventQuery.setCode(vipDiscountActivity.get(plusSku.getProductCode()).getEventCode());
							PlusModelEventInfo plusModelEventInfo = loadEventInfo
									.upInfoByCode(plusModelEventQuery);
							upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
									plusQuery, plusModelEventInfo);
						}
						
					}
				}
				
			}
			
			
		}
		
		/*
		 * 过滤其他促销活动
		 */
		{
			if(isCheckAcitivity) {
				
				String[] sEvents = StringUtils.split(plusSku.getEventCode(), ",");
				String[] sItems = StringUtils.split(plusSku.getItemCode(), ",");
				
				
				plusSku.setEventCode("");
				plusSku.setItemCode("");
				plusSku.setEventType("");
				
				for (int i = 0, j = sEvents.length; i < j; i++) {

					String sEvent = sEvents[i];
					String sItem = sItems[i];
					PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
					plusModelEventQuery.setCode(sEvent);

					PlusModelEventInfo plusModelEventInfo = loadEventInfo
							.upInfoByCode(plusModelEventQuery);
					
					if(("1".equals(plusQuery.getIfJJGFlag()) && !"4497472600010025".equals(plusModelEventInfo.getEventType()))||("0".equals(plusQuery.getIfJJGFlag()) && "4497472600010025".equals(plusModelEventInfo.getEventType()))) {//加价购活动
						continue;
					}
					
					if("0".equals(plusQuery.getIsSupportCollage()) && "4497472600010024".equals(plusModelEventInfo.getEventType())) {//非拼团类型不参与拼团活动
						continue;
					}
					
					/**投票换购特殊处理 begin*/
					if(StringUtils.isEmpty(plusQuery.getEventCode()) && "4497472600010029".equals(plusModelEventInfo.getEventType())) {
						continue;
					}
					if(StringUtils.isEmpty(plusQuery.getEventCode()) && "4497472600010032".equals(plusModelEventInfo.getEventType())) {
						continue;
					}
					if(StringUtils.isNotEmpty(plusQuery.getEventCode()) && !sEvent.equals(plusQuery.getEventCode())) {
						continue;
					}
					/**投票换购特殊处理 end*/
					
				
					// 如果活动有效 且活动的价格为全局影响价格
					if (checkEventEnable(plusModelEventInfo)
							&& ((StringUtils.isBlank(plusModelEventInfo.getChannels()) || plusModelEventInfo.getChannels().contains(plusQuery.getChannelId())))) {
						plusSku.setEventCode(sEvent);
						plusSku.setItemCode(sItem);
						plusSku.setEventType(plusModelEventInfo.getEventType());
						
						upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
								plusQuery, plusModelEventInfo);

						
						/*
						// 特定判断 如果是闪购活动 则只有可购买时才返回活动
						if (plusModelEventInfo.getEventType()
								.equals("4497472600010005")
								&& plusSku.getBuyStatus() != 1) {
							eventCodeString = "";
							itemCodeString = "";
						}
						*/

					}
			
	
				}
				
			}
			
		}
		
		
		return plusSku;
	}

	/**
	 * 经过该方法处理所有对商品价格有影响的活动
	 * 暂时不用，bak为备份
	 * @param plusSku
	 * @param plusQuery
	 * @return
	 */
	public PlusModelSkuInfo refreshSkuEvent_bak(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery) {

		LoadEventInfo loadEventInfo = new LoadEventInfo();
		
		/***过滤内购 购物车        20160725  有内购活动优先取内购    内购优先级最高***/
		if(plusQuery.getIsPurchase()==1){
			
			PlusModelProductInfo productInfo = new LoadProductInfo().topInitInfo(new PlusModelProductQuery(plusSku.getProductCode()));
			//必须为LD的商品
			if(productInfo.getSellerCode().equals("SI2003")&&productInfo.getSmallSellerCode().equals("SI2003") ){

				PlusModelEventNgProduct pong = new PlusModelEventNgProduct();
				pong.setMemberCode(plusQuery.getMemberCode());
				pong.setProCostPrice(productInfo.getCost_price());
				pong.setListSku(productInfo.getSkuList());
				List<PlusModelNgProduct> list = new ArrayList<PlusModelNgProduct>();
				
				PlusModelNgProduct ngList = new PlusModelNgProduct();
				ngList.setProductCode(plusSku.getProductCode());
				ngList.setSkuCode(plusSku.getSkuCode());
				ngList.setSkuPrice(plusSku.getSellPrice());
				list.add(ngList);
				
				pong.setNgPro(list);
				
				/**过滤内购活动**/
				PlusModelEventNgProduct pongNew = new PlusServicePurchase().getCartPurchase(pong,"SI2003",plusQuery.getChannelId());
				/**重新添加内购价格和内购活动**/
				if(pongNew.getMap()!=null&&!pongNew.getMap().isEmpty()){
					
					if(pongNew.getMap().get(plusSku.getSkuCode()).isTrue()){
						plusSku.setSellPrice(pongNew.getMap().get(plusSku.getSkuCode()).getSkuPrice());
						plusSku.setEventCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());
						plusSku.setEventType("4497472600010006");
					    plusSku.setItemCode(pongNew.getMap().get(plusSku.getSkuCode()).getItemCode());
					    
						PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
						plusModelEventQuery.setCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());

						PlusModelEventInfo plusModelEventInfo = loadEventInfo
								.upInfoByCode(plusModelEventQuery);

						// 如果活动有效 且活动的价格为全局影响价格
						if (checkEventEnable(plusModelEventInfo)
								&& (plusModelEventInfo.getScopePrice() == 1)) {
							plusSku.setEventCode(pongNew.getMap().get(plusSku.getSkuCode()).getEventCode());
							plusSku.setItemCode(pongNew.getMap().get(plusSku.getSkuCode()).getItemCode());
							upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
									plusQuery, plusModelEventInfo);


						}
					    
					    
					    
					}else{


						String[] sEvents = StringUtils.split(plusSku.getEventCode(), ",");
						String[] sItems = StringUtils.split(plusSku.getItemCode(), ",");

						//String eventCodeString = "";
						//String itemCodeString = "";
						
						
						plusSku.setEventCode("");
						plusSku.setItemCode("");
						
						for (int i = 0, j = sEvents.length; i < j; i++) {

							String sEvent = sEvents[i];
							String sItem = sItems[i];
							PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
							plusModelEventQuery.setCode(sEvent);

							PlusModelEventInfo plusModelEventInfo = loadEventInfo
									.upInfoByCode(plusModelEventQuery);

							// 如果活动有效 且活动的价格为全局影响价格
							if (checkEventEnable(plusModelEventInfo)
									&& (plusModelEventInfo.getScopePrice() == 1)) {
								//eventCodeString = sEvent;
								//itemCodeString = sItem;
								plusSku.setEventCode(sEvent);
								plusSku.setItemCode(sItem);
								upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
										plusQuery, plusModelEventInfo);

								
								/*
								// 特定判断 如果是闪购活动 则只有可购买时才返回活动
								if (plusModelEventInfo.getEventType()
										.equals("4497472600010005")
										&& plusSku.getBuyStatus() != 1) {
									eventCodeString = "";
									itemCodeString = "";
								}
								*/

							}
						}
					
					
					}
				
				}else{

					String[] sEvents = StringUtils.split(plusSku.getEventCode(), ",");
					String[] sItems = StringUtils.split(plusSku.getItemCode(), ",");

					//String eventCodeString = "";
					//String itemCodeString = "";
					
					
					plusSku.setEventCode("");
					plusSku.setItemCode("");
					
					for (int i = 0, j = sEvents.length; i < j; i++) {

						String sEvent = sEvents[i];
						String sItem = sItems[i];
						PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
						plusModelEventQuery.setCode(sEvent);

						PlusModelEventInfo plusModelEventInfo = loadEventInfo
								.upInfoByCode(plusModelEventQuery);

						// 如果活动有效 且活动的价格为全局影响价格
						if (checkEventEnable(plusModelEventInfo)
								&& (plusModelEventInfo.getScopePrice() == 1)) {
							//eventCodeString = sEvent;
							//itemCodeString = sItem;
							plusSku.setEventCode(sEvent);
							plusSku.setItemCode(sItem);
							upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
									plusQuery, plusModelEventInfo);

							
							/*
							// 特定判断 如果是闪购活动 则只有可购买时才返回活动
							if (plusModelEventInfo.getEventType()
									.equals("4497472600010005")
									&& plusSku.getBuyStatus() != 1) {
								eventCodeString = "";
								itemCodeString = "";
							}
							*/

						}
					}
				
				}
					
			
			}else{
				String[] sEvents = StringUtils.split(plusSku.getEventCode(), ",");
				String[] sItems = StringUtils.split(plusSku.getItemCode(), ",");

				//String eventCodeString = "";
				//String itemCodeString = "";
				
				
				plusSku.setEventCode("");
				plusSku.setItemCode("");
				
				for (int i = 0, j = sEvents.length; i < j; i++) {

					String sEvent = sEvents[i];
					String sItem = sItems[i];
					PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
					plusModelEventQuery.setCode(sEvent);

					PlusModelEventInfo plusModelEventInfo = loadEventInfo
							.upInfoByCode(plusModelEventQuery);

					// 如果活动有效 且活动的价格为全局影响价格
					if (checkEventEnable(plusModelEventInfo)
							&& (plusModelEventInfo.getScopePrice() == 1)) {
						//eventCodeString = sEvent;
						//itemCodeString = sItem;
						plusSku.setEventCode(sEvent);
						plusSku.setItemCode(sItem);
						upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
								plusQuery, plusModelEventInfo);

						
						/*
						// 特定判断 如果是闪购活动 则只有可购买时才返回活动
						if (plusModelEventInfo.getEventType()
								.equals("4497472600010005")
								&& plusSku.getBuyStatus() != 1) {
							eventCodeString = "";
							itemCodeString = "";
						}
						*/

					}
				}
			}
			
		}else{
			String[] sEvents = StringUtils.split(plusSku.getEventCode(), ",");
			String[] sItems = StringUtils.split(plusSku.getItemCode(), ",");

			//String eventCodeString = "";
			//String itemCodeString = "";
			
			
			plusSku.setEventCode("");
			plusSku.setItemCode("");
			
			for (int i = 0, j = sEvents.length; i < j; i++) {

				String sEvent = sEvents[i];
				String sItem = sItems[i];
				PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
				plusModelEventQuery.setCode(sEvent);

				PlusModelEventInfo plusModelEventInfo = loadEventInfo
						.upInfoByCode(plusModelEventQuery);

				// 如果活动有效 且活动的价格为全局影响价格
				if (checkEventEnable(plusModelEventInfo)
						&& (plusModelEventInfo.getScopePrice() == 1)) {
					//eventCodeString = sEvent;
					//itemCodeString = sItem;
					plusSku.setEventCode(sEvent);
					plusSku.setItemCode(sItem);
					upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku,
							plusQuery, plusModelEventInfo);

					
					/*
					// 特定判断 如果是闪购活动 则只有可购买时才返回活动
					if (plusModelEventInfo.getEventType()
							.equals("4497472600010005")
							&& plusSku.getBuyStatus() != 1) {
						eventCodeString = "";
						itemCodeString = "";
					}
					*/

				}
			}
		}
		
		
		return plusSku;
	}

	/**
	 * 刷新单个活动详情页调用时的情况
	 * 
	 * @param plusSku
	 * @param plusQuery
	 * @return
	 */
	public PlusModelSkuInfo refreshSkuItem(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery) {

		PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
		plusModelEventQuery.setCode(plusSku.getEventCode());
		LoadEventInfo loadEventInfo = new LoadEventInfo();
		PlusModelEventInfo plusModelEventInfo = loadEventInfo
				.upInfoByCode(plusModelEventQuery);

		upServiceProduct(plusModelEventInfo).refreshSkuInfo(plusSku, plusQuery,
				plusModelEventInfo);

		return plusSku;

	}

	/**
	 * 获取针对活动商品的处理的类
	 * 
	 * @param plusModelEventInfo
	 * @return
	 */
	public IPlusServiceProduct upServiceProduct(
			PlusModelEventInfo plusModelEventInfo) {
		IPlusServiceProduct iProduct = null;
		try {
			iProduct = (IPlusServiceProduct) ClassUtils.getClass(
					plusModelEventInfo.getEventExecClass()).newInstance();

		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		return iProduct;
	}

	/**
	 * 检查判断活动是否可用
	 * 
	 * @param plusModelEventInfo
	 * @return
	 */
	public boolean checkEventEnable(PlusModelEventInfo plusModelEventInfo) {
		boolean bFlagEnable = false;

		if (PlusHelperEvent.checkEventStatus(plusModelEventInfo
				.getEventStatus())
				&& plusModelEventInfo.getBeginTime().compareTo(
						FormatHelper.upDateTime()) <= 0
				&& plusModelEventInfo.getEndTime().compareTo(
						FormatHelper.upDateTime()) >= 0) {
			bFlagEnable = true;
		}

		return bFlagEnable;
	}

	/**
	 * 获取活动明细的剩余库存
	 * 
	 * @return
	 */
	public long upEventItemSkuStock(String sIcCode) {

		checkAndInitStock(sIcCode);

		Long lReturn= Long.parseLong(XmasKv.upFactory(EKvSchema.Stock).get(sIcCode));
		
		return lReturn>=0?lReturn:0;
	}

	/**
	 * 检查并且初始化活动的库存数量
	 * 
	 * @param sIcCode
	 */
	public void checkAndInitStock(String sIcCode) {
		if (!XmasKv.upFactory(EKvSchema.Stock).exists(sIcCode)) {

			String sSubCode = PlusHelperEvent.upSubEvent(sIcCode);

			
			// 如果是特殊活动 则设定库存数量为SKU的可售数量
			if (StringUtils.isNotBlank(sSubCode)) {

				String sSkuCode = PlusHelperEvent.upSubSkuCode(sIcCode);
				XmasKv.upFactory(EKvSchema.Stock).setnx(
						sIcCode,
						String.valueOf(new PlusSupportProduct()
								.upSkuAllStock(sSkuCode)));
				// 设定这个库存一定时间过期
				XmasKv.upFactory(EKvSchema.Stock).expire(sIcCode,
						XmasSystemConst.STOCK_TTL_TIME);

			} else {
				// 如果不存在 则初始化设置为库存的默认值
				XmasKv.upFactory(EKvSchema.Stock).setnx(
						sIcCode,
						String.valueOf(upItemProductByIcCode(sIcCode)
								.getSalesStock()));
			}

		}
	}

	/**
	 * 扣除或者增加活动明细的库存并返回变化后的值，如果该值小于0，则标记为失败
	 * 
	 * @param sIcCode
	 * @return
	 */
	public long subtractSkuStock(String sIcCode, Long lSubstractStock) {
		checkAndInitStock(sIcCode);
		
		Long before = Long.parseLong(XmasKv.upFactory(EKvSchema.Stock).get(sIcCode));
		long lNow = XmasKv.upFactory(EKvSchema.Stock).incrBy(sIcCode,
				0 - lSubstractStock);

		if(lNow >= 0){
			try {
				// 如果是旧数据则能正常读取，需要清除一下旧数据
				XmasKv.upFactory(EKvSchema.EmptyStock).get(sIcCode);
				XmasKv.upFactory(EKvSchema.EmptyStock).del(sIcCode);
			} catch (Exception e) {}
			
			// 如果库存小于或者等于0时，记录一下当时的时间
			if (lNow <= 0) {
				XmasKv.upFactory(EKvSchema.EmptyStock).hset(sIcCode, FormatHelper.upDateTime(), "empty");
			}
			
			XmasKv.upFactory(EKvSchema.EmptyStock).hset(sIcCode, FormatHelper.upDateTime("yyyyMMddHHmmss"), "{before: "+before+", after: "+lNow+"}");
			XmasKv.upFactory(EKvSchema.EmptyStock).expire(sIcCode, 3600*24*30);
		}else{
			// 库存扣成负数的时候需要恢复原来的值，避免并发执行时造成库存扣为负数导致取消订单后还原库存仍不可购买问题
			XmasKv.upFactory(EKvSchema.Stock).incrBy(sIcCode, lSubstractStock);
		}
		
		return lNow;
	}

	/**
	 * 获取某一条活动明细库存变为0时的时间
	 * 
	 * @param sIcCode
	 * @return
	 */
	public String upIcEmptyStockTime(String sIcCode) {
		try {
			return XmasKv.upFactory(EKvSchema.EmptyStock).hget(sIcCode, "empty");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取某个活动明细编号对应的活动下的同商品编号的其他明细编号 不包含自己，该方法用于找到某个活动下同商品编号的对应的SKU的活动
	 * 
	 * @param sIcCode
	 * @return
	 */
	public String upConstItemCode(String sIcCode) {

		// 不存在则开始初始化
		if (!XmasKv.upFactory(EKvSchema.Iconst).exists(sIcCode)) {

			List<String> aList = new ArrayList<String>();

			String sSubCode = PlusHelperEvent.upSubEvent(sIcCode);

			// 如果是特殊活动
			if (StringUtils.isNotBlank(sSubCode)) {

				PlusModelSkuInfo pSkuInfo = new PlusSupportProduct()
						.upSkuInfoBySkuCode(sIcCode);
				for (MDataMap map : DbUp.upTable("pc_skuinfo").queryByWhere(
						"product_code", pSkuInfo.getProductCode())) {

					String sConstCode = StringUtils.substringBeforeLast(
							sIcCode, XmasSystemConst.EVENT_SPLIT_LINE)
							+ XmasSystemConst.EVENT_SPLIT_LINE
							+ map.get("sku_code");

					if (!sConstCode.equals(sIcCode)) {
						aList.add(sConstCode);
					}
				}

			} else {
				PlusModelEventItemProduct pItemProduct = upItemProductByIcCode(sIcCode);
				for (MDataMap map : DbUp.upTable("sc_event_item_product")
						.queryByWhere("event_code",
								pItemProduct.getEventCode(), "product_code",
								pItemProduct.getProductCode(),"flag_enable","1")) {

					if (!map.get("item_code").equals(sIcCode)) {
						aList.add(map.get("item_code"));
					}
				}
			}

			String sConsts = StringUtils.join(aList, ",");

			XmasKv.upFactory(EKvSchema.Iconst).setex(sIcCode,7200, sConsts);
		}
		return XmasKv.upFactory(EKvSchema.Iconst).get(sIcCode);
	}
	
	/**
	 * 获取某个活动明细编号对应的活动下的同商品编号的其他明细编号 不包含自己，该方法用于找到某个活动下同商品编号的对应的SKU的活动
	 * 查询主库
	 * @param sIcCode
	 * @return
	 */
	public String upConstItemCodeMain(String sIcCode) {
		// 不存在则开始初始化
		if (!XmasKv.upFactory(EKvSchema.Iconst).exists(sIcCode)) {
			List<String> aList = new ArrayList<String>();
			String sSubCode = PlusHelperEvent.upSubEvent(sIcCode);
			// 如果是特殊活动
			if (StringUtils.isNotBlank(sSubCode)) {
				MDataMap pSkuInfo = DbUp.upTable("sc_event_item_product").onePriLib("item_code", sIcCode);
				for (MDataMap map : DbUp.upTable("pc_skuinfo").queryPriLibList("product_code", pSkuInfo.get("product_code"))) {
					String sConstCode = StringUtils.substringBeforeLast(sIcCode, XmasSystemConst.EVENT_SPLIT_LINE) + XmasSystemConst.EVENT_SPLIT_LINE + map.get("sku_code");
					if (!sConstCode.equals(sIcCode)) {
						aList.add(sConstCode);
					}
				}
			} else {
				MDataMap dataMap = DbUp.upTable("sc_event_item_product").onePriLib("item_code", sIcCode, "flag_enable", "1");
				if(dataMap != null) {
					for (MDataMap map : DbUp.upTable("sc_event_item_product").queryPriLibList("event_code", dataMap.get("event_code"), "product_code", dataMap.get("product_code"), "flag_enable", "1")) {
						if (!map.get("item_code").equals(sIcCode)) {
							aList.add(map.get("item_code"));
						}
					}
				}
			}

			String sConsts = StringUtils.join(aList, ",");
			XmasKv.upFactory(EKvSchema.Iconst).setex(sIcCode,7200, sConsts);
		}
		return XmasKv.upFactory(EKvSchema.Iconst).get(sIcCode);
	}
	
	/**
	 * 根据活动编号获取活动信息
	 * 
	 * @param sCode
	 * @return
	 */
	public PlusModelEventInfo upEventInfoByCode(String sCode) {
		PlusModelEventQuery tQuery = new PlusModelEventQuery();
		tQuery.setCode(sCode);

		return new LoadEventInfo().upInfoByCode(tQuery);
	}

	
	/***
	 * 根据系统获取已发布的拼好货活动数据
	 * @param sManageCode 系统编号
	 */
	public PlusModelEventGoodsProduct getGoodsProduct(String sManageCode){
		PlusModelSaleQuery tQuery = new PlusModelSaleQuery();
		tQuery.setCode(sManageCode);
		PlusModelEventGoodsProduct pmegp = new LoadGoodsProduct().upInfoByCode(tQuery);
		if(pmegp.getGoodsProduct()!=null && pmegp.getGoodsProduct().size()>0){
			

			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String data = sysDateTime.format(new Date()).toString();
			
			for(int i=pmegp.getGoodsProduct().size()-1;i>=0;--i){
				if(compareDate(data,pmegp.getGoodsProduct().get(i).getEndTime())>=0 || compareDate(pmegp.getGoodsProduct().get(i).getBeginTime(),data)>=0){
					pmegp.getGoodsProduct().remove(i);
				}
			}
			//价格倒排序
			Collections.sort(pmegp.getGoodsProduct(), new Comparator<Object>() {
			      public int compare(Object beginTimeOne, Object beginTimeTwo) {
			    	  String one =((PlusModelGoodsProduct)beginTimeOne).getBeginTime();
			    	  String two =((PlusModelGoodsProduct)beginTimeTwo).getBeginTime();
			        return two.compareTo(one);
			      }
		    });
			
		}
		
		
		return pmegp;
	}
	
	
	/***
	 * 根据系统获取已发布的内购活动数据
	 * @param sManageCode 系统编号
	 */
	public PlusModelEventPurchase getPurchase(String sManageCode){
		PlusModelSaleQuery tQuery = new PlusModelSaleQuery();
		tQuery.setCode(sManageCode);
		PlusModelEventPurchase pmep = new LoadEventPurchase().upInfoByCode(tQuery);
		if(pmep.getListPurchase()!=null && pmep.getListPurchase().size()>0){
			

			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String data = sysDateTime.format(new Date()).toString();
			
			for(int i=pmep.getListPurchase().size()-1;i>=0;--i){
				if(compareDate(data,pmep.getListPurchase().get(i).getEndTime())>=0 || compareDate(pmep.getListPurchase().get(i).getBeginTime(),data)>=0){
					pmep.getListPurchase().remove(i);
				}
			}
			//价格倒排序
			Collections.sort(pmep.getListPurchase(), new Comparator<Object>() {
			      public int compare(Object beginTimeOne, Object beginTimeTwo) {
			    	  String one =((PlusModelPurchase)beginTimeOne).getBeginTime();
			    	  String two =((PlusModelPurchase)beginTimeTwo).getBeginTime();
			        return two.compareTo(one);
			      }
		    });
			
		}
		
		
		return pmep;
	}
	
	/**
	 * 根据系统获取已发布的满减活动数据
	 * @param sManageCode
	 * @return
	 */
	public PlusModelEventSale upEventSalueByMangeCode(String sManageCode,String channelId)
	{
		PlusModelSaleQuery tQuery = new PlusModelSaleQuery();
		tQuery.setCode(sManageCode);
		PlusModelEventSale eventSale = new LoadEventSale().upInfoByCode(tQuery);
		if(eventSale.getEventFulls()!=null && eventSale.getEventFulls().size()>0){
			
			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String data = sysDateTime.format(new Date()).toString();
			
			for(int i=eventSale.getEventFulls().size()-1;i>=0;--i){
				if(compareDate(data,eventSale.getEventFulls().get(i).getEndTime())>=0 
						|| compareDate(eventSale.getEventFulls().get(i).getBeginTime(),data)>=0
						|| (StringUtils.isNotBlank(eventSale.getEventFulls().get(i).getChannels())) && !eventSale.getEventFulls().get(i).getChannels().contains(channelId)){
					eventSale.getEventFulls().remove(i);
				}
			}
			//价格倒排序
			Collections.sort(eventSale.getEventFulls(), new Comparator<Object>() {
			      public int compare(Object beginTimeOne, Object beginTimeTwo) {
			    	  String one =((PlusModelEventFull)beginTimeOne).getBeginTime();
			    	  String two =((PlusModelEventFull)beginTimeTwo).getBeginTime();
			        return two.compareTo(one);
			      }
		    });
			
		}
		
		return eventSale;
	}
	
	/**
	 * 根据SellerCode和ProductCode过滤特殊满减活动(每满X减Y-LD多重促销活动,参加此活动的商品准许参加特价和扫码购活动)
	 * @param sManageCode sellerCode
	 * @param productCode 商品编号
	 * @author zht
	 * @return
	 */
	public PlusModelEventFull upEventSalueByMangeCodeAndProductCode(String sManageCode, String productCode,String eventType)
	{
		PlusModelEventFull result = null;
		PlusModelSaleQuery tQuery = new PlusModelSaleQuery();
		tQuery.setCode(sManageCode);
		
		PlusModelEventSale eventSale = new LoadEventSale().upInfoByCode(tQuery);
		if(eventSale.getEventFulls() != null && eventSale.getEventFulls().size() > 0) {
			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String now = sysDateTime.format(new Date()).toString();
			
			List<PlusModelEventFull> delList = new LinkedList<PlusModelEventFull>();
			for(int i = eventSale.getEventFulls().size() - 1; i >= 0; --i) {
				PlusModelEventFull currentEvent = eventSale.getEventFulls().get(i);
				//活动已结束或活动还未开始
				if(compareDate(now, currentEvent.getEndTime()) >= 0 || compareDate(currentEvent.getBeginTime(), now) >= 0 ) {
					delList.add(currentEvent);
//					eventSale.getEventFulls().remove(i);
				}
				//5.4.2前规则：非每满X减Y-LD多重促销活动
/*				if(!currentEvent.getFullType().equals("449747630008")) {
					delList.add(currentEvent);
//					eventSale.getEventFulls().remove(i);
				}*/
				//5.4.2新规则：判断满减活动是否进行了可叠加使用
			/*	if(StringUtils.isBlank(currentEvent.getSuprapositionType())) {
					Map<String,Object> map =DbUp.upTable("sc_event_info").dataSqlOne("select supraposition_type from sc_event_info where event_code=:event_code", new MDataMap("event_code",currentEvent.getEventCode()));
					currentEvent.setSuprapositionType(map.get("supraposition_type").toString());
				}*/
				if(StringUtils.isBlank(currentEvent.getSuprapositionType())||!currentEvent.getSuprapositionType().contains(eventType)) {
					delList.add(currentEvent);
				}
				
			}
			eventSale.getEventFulls().removeAll(delList);
			
			if(!eventSale.getEventFulls().isEmpty()) {
				for(PlusModelEventFull eventFull : eventSale.getEventFulls()) {
					String limitType = eventFull.getRuleSku().getLimitType();
					List<String> limitcodes = eventFull.getRuleSku().getLimitCode();
					if(limitType.equals("4497476400020001")) {
						//不限
						result = eventFull;
					} else if(limitType.equals("4497476400020002")) {
						//仅包含,双重break,优先取
						boolean found = false;
						if(limitcodes != null) {
							for(String code : limitcodes) {
								if(productCode.equals(code)) {
									result = eventFull;
									found = true;
									break;
								}
							}
						}
						if(found) break;
					} else if(limitType.equals("4497476400020003")) {
						//以下除外
						boolean found = false;
						if(limitcodes != null) {
							for(String code : limitcodes) {
								if(productCode.equals(code)) {
									found = true;
									break;
								}
							}
						}
						if(!found) {
							result = eventFull;
						}
					}
				}
			}
			
		}
		return result;
	}
	
	
	/**
	 * 根据系统获取已发布的减免运费活动数据
	 * @param sManageCode
	 * @return
	 */
	public PlusModelEventFree upEventFreeByMangeCode(String sManageCode)
	{
		PlusModelFreeQuery tQuery = new PlusModelFreeQuery();
		tQuery.setCode(sManageCode);
		PlusModelEventFree eventFree = new LoadEventFree().upInfoByCode(tQuery);
		
		
		if(eventFree.getEventFree()!=null && eventFree.getEventFree().size()>0){
			
			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String data = sysDateTime.format(new Date()).toString();
			
			for(int i=eventFree.getEventFree().size()-1;i>=0;--i){
				if(compareDate(data,eventFree.getEventFree().get(i).getEndTime())>=0 || compareDate(eventFree.getEventFree().get(i).getBeginTime(),data)>=0){
					eventFree.getEventFree().remove(i);
				}
			}
			
			//价格倒排序
			Collections.sort(eventFree.getEventFree(), new Comparator<Object>() {
			      public int compare(Object fullPriceOne, Object fullPriceTwo) {
			    	  Integer one =((PlusModelFreeShipping)fullPriceOne).getFullPrice();
			    	  Integer two =((PlusModelFreeShipping)fullPriceTwo).getFullPrice();
			        return two.compareTo(one);
			      }
		    });
			
		}
		
		return eventFree;
	}
	
	
	/**
	 * 比较两个日期时间
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	 public static int compareDate(String beginTime, String endTime) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try {
	            Date dt1 = df.parse(beginTime.trim());
	            Date dt2 = df.parse(endTime.trim());
	            if (dt1.getTime() > dt2.getTime()) {
	                return 1;     //beginTime时间大于endTime时间
	            } else if (dt1.getTime() < dt2.getTime()) {
	                return -1;     //beginTime时间小于endTime时间
	            } else {
	                return 0;      //beginTime时间等于endTime时间
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	 
	 private Integer numHistory(String isMemberCode,String productCode) {
			String sql = "";
			if(productCode=="") {
				sql = "SELECT SUM(b.sku_num) num	"
						+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
						+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
						+ "AND a.order_type = '449715200007' "
						+ "AND b.gift_flag = '1' "					
						+ "AND a.order_status != '4497153900010006'	"
						+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
			}else {
				sql = "SELECT SUM(b.sku_num) num	"
						+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
						+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
						+ "AND a.order_type = '449715200007' "
						+ "AND b.gift_flag = '1' "
						+ "AND a.order_status != '4497153900010006' "
						+ "AND b.product_code = '"+productCode+"'	"
						+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
			}
			List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, null);
			Integer object = 0;
			if(null!=dataSqlList&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				Object object2 = map.get("num");
				if(object2!=null) {
					object = ((BigDecimal)object2).intValue();
				}
			}
			return object;
		}
	
}

