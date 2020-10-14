package com.srnpr.xmassystem.helper;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.load.LoadEventExcludeProduct;
import com.srnpr.xmassystem.load.LoadEventFree;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadEventInfoPlusList;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadEventOnlinePay;
import com.srnpr.xmassystem.load.LoadEventPurchase;
import com.srnpr.xmassystem.load.LoadEventSale;
import com.srnpr.xmassystem.load.LoadEventVipDiscount;
import com.srnpr.xmassystem.load.LoadFlashSaleList;
import com.srnpr.xmassystem.load.LoadFlashSaleProduct;
import com.srnpr.xmassystem.load.LoadGoodsProduct;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadHuDongEvent;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadSkuItem;
import com.srnpr.xmassystem.load.LoadSkuPriceChange;
import com.srnpr.xmassystem.load.LoadSubItem;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventLock;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelFreeQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelnotice.PlusModelNoticeOrder;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportMember;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;

/**
 * 通知相关调用类
 * 
 * @author srnpr
 * 
 */
public class PlusHelperNotice {

	/**
	 * 当活动信息发生变化时 每次活动信息发生任何变化 包括但不限于活动信息创建/修改/删除/发布/取消 或者活动明细添加/修改/删除等等
	 * 
	 * 4497472700020001:未发布
	 * 4497472700020002:已发布
	 * 4497472700020003:终止
	 * 4497472700020004:暂停
	 * 
	 * @param sEventCode
	 * @return
	 */
	/*public static boolean onChangeEvent(String sEventCode) {

		// 删除活动的缓存    [形如:xs-Event-CX2015072800003]
		new LoadEventInfo().deleteInfoByCode(sEventCode);
		
		MDataMap mEventMap = DbUp.upTable("sc_event_info").one("event_code",
				sEventCode);

		String sEventTypeCode = mEventMap.get("event_type_code");
		
		//检查活动的状态是否为已发布或已暂停
		boolean bFlagEnable = PlusHelperEvent.checkEventStatus(mEventMap
				.get("event_status"));

		// 删除活动关联的缓存信息
		for (MDataMap map : DbUp.upTable("sc_event_item_product").queryByWhere(
				"event_code", sEventCode)) {

			String sItemCode = map.get("item_code");

			String sStockCode = map.get("sku_code");

			// 删除活动关联商品的基本信息  [形如: xs-Item-IC150821100004]
			new LoadEventItemProduct().deleteInfoByCode(sItemCode);
			
			// 删除活动SKU的信息  
			//[形如: xs-IcSku-IC150821100004 xs-IcSku-IC_SMG_145623 xs-IcSku-IC_APPSMG_8019910513]
			new LoadSkuItem().deleteInfoByCode(sItemCode);
			
			// 删除SKU的缓存信息 [形如: xs-Sku-8019833566]
			new LoadSkuInfo().deleteInfoByCode(sStockCode);
			
			// 删除IC的库存信息 [形如: xs-Stock-8019903379]
			// XmasKv.upFactory(EKvSchema.Stock).del(sItemCode);
			
			// 删除IC的关联信息[形如: xs-Iconst-IC_SMG_142269]
			XmasKv.upFactory(EKvSchema.Iconst).del(sItemCode);

			// 如果活动生效 设置锁定库存 否则 如果活动其他状态并且锁定库存的编号等于当前活动明细编号 则将锁定值置为0
			if (bFlagEnable && map.get("flag_enable").equals("1")) {

				
				 * XmasKv.upFactory(EKvSchema.LockStock).set(sStockCode,
				 * mEventMap.get("end_time") + "-" + map.get("sales_num"));
				 
				
				 * // 如果活动价格不统一 才锁定库存 //if
				 * (mEventMap.get("uniform_price").equals("449746880002")) { //
				 * 设置锁定库存过期时间
				 * XmasKv.upFactory(EKvSchema.LockStock).hset(sStockCode,
				 * XmasSystemConst.LOCK_STOCK_TIME, mEventMap.get("end_time"));
				 * 
				 * XmasKv.upFactory(EKvSchema.LockStock).hset(sStockCode,
				 * XmasSystemConst.LOCK_START_TIME,
				 * mEventMap.get("begin_time"));
				 * 
				 * // 设置锁定库存数量
				 * XmasKv.upFactory(EKvSchema.LockStock).hset(sStockCode,
				 * XmasSystemConst.LOCK_STOCK_NUM, map.get("sales_num")); //
				 * 设置锁定库存的来源编号
				 * XmasKv.upFactory(EKvSchema.LockStock).hset(sStockCode,
				 * XmasSystemConst.LOCK_STOCK_SOURCE, sItemCode); //}
				 

				PlusModelEventLock plusEventLock = new PlusModelEventLock();
				plusEventLock.setBeginTime(mEventMap.get("begin_time"));
				plusEventLock.setEndTime(mEventMap.get("end_time"));
				plusEventLock.setLockNumber(Integer.valueOf(map
						.get("sales_num")));
				plusEventLock.setLockSource(sItemCode);
				
				//[形如 xs-EventLock-8019676124]
				XmasKv.upFactory(EKvSchema.EventLock).hset(sStockCode,
						sItemCode, GsonHelper.toJson(plusEventLock));

			} else {
				// XmasKv.upFactory(EKvSchema.LockStock).del(sStockCode);
				
				 * String sLockItemCode = XmasKv.upFactory(EKvSchema.LockStock)
				 * .hget(sStockCode, XmasSystemConst.LOCK_STOCK_SOURCE);
				 * 
				 * // 如果是当前明细锁定的库存 则将锁定的库存数置为0 if
				 * (StringUtils.isNotBlank(sLockItemCode) &&
				 * sLockItemCode.equals(sItemCode)) {
				 * XmasKv.upFactory(EKvSchema.LockStock).hset(sStockCode,
				 * XmasSystemConst.LOCK_STOCK_NUM, "0"); }
				 
				XmasKv.upFactory(EKvSchema.EventLock).hdel(sStockCode,
						sItemCode);

			}
		}

		
		// 如果是特定种类的活动 则清除特定种类活动的相关信息
		if (PlusHelperEvent.checkSmgOrDm(sEventTypeCode) && mEventMap.get("flag_enable").equals("1")) {
				
				for (MDataMap map : DbUp.upTable("sc_event_item_product")
						.queryByWhere("event_code", sEventCode)) {
					String sStockCode = map.get("sku_code");
					if (bFlagEnable) {
						
						//[形如:xs-SubEventCode-BJTV  xs-SubEventCode-DM  xs-SubEventCode-SMG xs-SubEventCode-APPSMG]
						XmasKv.upFactory(EKvSchema.SubEventCode).set(sStockCode,
								sEventCode);
						
						String sIcKey = StringUtils
								.defaultIfBlank(
										XmasKv.upFactory(EKvSchema.SubIcCode).get(
												sStockCode), "");
						if (StringUtils.isNotBlank(sIcKey)) {
							
							sIcKey = sIcKey + ",";
						}
						
						XmasKv.upFactory(EKvSchema.SubIcCode).set(sStockCode,
								sIcKey + map.get("item_code"));
						
					} else {
						
						
						MDataMap mSubMap = XmasKv
								.upFactory(EKvSchema.EventChildren).hgetAll(
										sStockCode);
						for (String sKey : mSubMap.keySet()) {
							new LoadSubItem().deleteInfoByCode(sKey);
							XmasKv.upFactory(EKvSchema.Stock).del(sKey);
							XmasKv.upFactory(EKvSchema.Stock).del(sStockCode);
						}
					}
					
				}
		}

		*//**满减活动**//*
		if (sEventTypeCode.equals("4497472600010008")) {
			//[形如: xs-EventSale-SI2003]
			new LoadEventSale().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		
		*//**运费减免**//*
		if(sEventTypeCode.equals("4497472600010013")){
			new LoadEventFree().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		*//**拼好货**//*
		if(sEventTypeCode.equals("4497472600010016")){
			new LoadGoodsProduct().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		*//**内购**//*
		if(sEventTypeCode.equals("4497472600010006")){
			new LoadEventPurchase().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		*//**会员日折扣**//*
		if(sEventTypeCode.equals("4497472600010018")){
			new LoadEventVipDiscount().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		return true;
	}
	
	/**
	 * 当活动信息发生变化时 每次活动信息发生任何变化 包括但不限于活动信息创建/修改/删除/发布/取消 或者活动明细添加/修改/删除等等
	 * 
	 * 4497472700020001:未发布
	 * 4497472700020002:已发布
	 * 4497472700020003:终止
	 * 4497472700020004:暂停
	 * 
	 * @param sEventCode
	 * @return
	 */
	public static boolean onChangeEvent(String sEventCode) {

		//重置活动缓存    [形如:xs-Event-CX2015072800003]
		LoadEventInfo loadEventInfo = new LoadEventInfo();
		loadEventInfo.deleteInfoByCode(sEventCode);
		PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
		plusModelEventQuery.setCode(sEventCode);
		loadEventInfo.upInfoByCodeMain(plusModelEventQuery);
		
		// 刷新缓存
		new LoadEventExcludeProduct().refresh(new PlusModelQuery(sEventCode));
		
		MDataMap mEventMap = DbUp.upTable("sc_event_info").onePriLib("event_code", sEventCode);
		String sEventTypeCode = mEventMap.get("event_type_code");
		//根据活动类型判断，是否需要刷新模板缓存。需要刷新的活动有：拼团、秒杀、闪购、小程序闪购、特价、打折促销活动，需要异步刷新模板缓存
		if("4497472600010001".equals(sEventTypeCode)||"4497472600010002".equals(sEventTypeCode)||"4497472600010005".equals(sEventTypeCode)||"4497472600010019".equals(sEventTypeCode)||"4497472600010024".equals(sEventTypeCode)||"4497472600010030".equals(sEventTypeCode)) {
			reflushTemplateCash(sEventCode);
		}
		/**
		 * 判断是否是秒杀活动，如果是秒杀活动，需要清除秒杀列表缓存，以及该秒杀商品缓存 ng++ 2019-05-29
		 */
		if("4497472600010001".equals(sEventTypeCode)) {
			LoadFlashSaleList loadFlashSaleList = new LoadFlashSaleList();
			loadFlashSaleList.deleteInfoByCode("");
			LoadFlashSaleProduct LoadFlashSaleProduct = new LoadFlashSaleProduct();
			LoadFlashSaleProduct.deleteInfoByCode(sEventCode);
		}
		//检查活动的状态是否为已发布或已暂停
		boolean bFlagEnable = PlusHelperEvent.checkEventStatus(mEventMap .get("event_status"));

		PlusModelEventItemQuery plusModelEventItemQuery = new PlusModelEventItemQuery();
		PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
		for (MDataMap map : DbUp.upTable("sc_event_item_product").queryPriLibList("event_code", sEventCode)) {
			String sItemCode = map.get("item_code");
			String sStockCode = map.get("sku_code");
			String sProductCode = map.get("product_code");

			//重置活动关联商品的基本信息  [形如: xs-Item-IC150821100004]
			LoadEventItemProduct loadEventItemProduct = new LoadEventItemProduct();
			loadEventItemProduct.deleteInfoByCode(sItemCode);
			plusModelEventItemQuery.setCode(sItemCode);
			loadEventItemProduct.upInfoByCodeMain(plusModelEventItemQuery);
			
			//重置活动SKU的信息   [形如: xs-IcSku-IC150821100004 xs-IcSku-IC_SMG_145623 xs-IcSku-IC_APPSMG_8019910513]
			if(StringUtils.isNotEmpty(sStockCode)) {
				LoadSkuItem loadSkuItem = new LoadSkuItem();
				loadSkuItem.deleteInfoByCode(sItemCode);
				plusModelSkuQuery.setCode(sItemCode);
				loadSkuItem.upInfoByCodeMain(plusModelSkuQuery);
			}
			
			//重置SKU的缓存信息 [形如:xs-Sku-8019833566]
			if(StringUtils.isEmpty(sStockCode)) {
				//sku为空的时候 根据商品编号查出所有sku
				PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sProductCode));
				List<PlusModelProductSkuInfo> skuList = productInfo.getSkuList();
				if(null != skuList && !skuList.isEmpty()) {
					for (PlusModelProductSkuInfo skuInfo : skuList) {
						sStockCode = skuInfo.getSkuCode();
						LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
						loadSkuInfo.deleteInfoByCode(sStockCode);
						plusModelSkuQuery.setCode(sStockCode);
						loadSkuInfo.upInfoByCodeMain(plusModelSkuQuery);
					}
				}
			}else {
				LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
				loadSkuInfo.deleteInfoByCode(sStockCode);
				plusModelSkuQuery.setCode(sStockCode);
				loadSkuInfo.upInfoByCodeMain(plusModelSkuQuery);
			}
			
			//重置IC的关联信息[形如: xs-Iconst-IC_SMG_142269]
			XmasKv.upFactory(EKvSchema.Iconst).del(sItemCode);
			PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
			plusSupportEvent.upConstItemCodeMain(sItemCode);
			
			// 如果活动生效 设置锁定库存 否则 如果活动其他状态并且锁定库存的编号等于当前活动明细编号 则将锁定值置为0
			if (bFlagEnable && map.get("flag_enable").equals("1")) {
				PlusModelEventLock plusEventLock = new PlusModelEventLock();
				plusEventLock.setBeginTime(mEventMap.get("begin_time"));
				plusEventLock.setEndTime(mEventMap.get("end_time"));
				plusEventLock.setLockNumber(Integer.valueOf(map.get("sales_num")));
				plusEventLock.setLockSource(sItemCode);
				
				//[形如 xs-EventLock-8019676124]
				XmasKv.upFactory(EKvSchema.EventLock).hset(sStockCode, sItemCode, GsonHelper.toJson(plusEventLock));
			} else {
				XmasKv.upFactory(EKvSchema.EventLock).hdel(sStockCode, sItemCode);

			}
		}

		
		// 如果是特定种类的活动 则清除特定种类活动的相关信息
		if (PlusHelperEvent.checkSmgOrDm(sEventTypeCode) && mEventMap.get("flag_enable").equals("1")) {
			for (MDataMap map : DbUp.upTable("sc_event_item_product").queryByWhere("event_code", sEventCode)) {
				String sStockCode = map.get("sku_code");
				if(StringUtils.isNotEmpty(sStockCode)) {
					if (bFlagEnable) {
						//[形如:xs-SubEventCode-BJTV  xs-SubEventCode-DM  xs-SubEventCode-SMG xs-SubEventCode-APPSMG]
						XmasKv.upFactory(EKvSchema.SubEventCode).set(sStockCode, sEventCode);
						
						String sIcKey = StringUtils.defaultIfBlank(XmasKv.upFactory(EKvSchema.SubIcCode).get(sStockCode), "");
						if (StringUtils.isNotBlank(sIcKey)) {
							sIcKey = sIcKey + ",";
						}
						
						XmasKv.upFactory(EKvSchema.SubIcCode).set(sStockCode, sIcKey + map.get("item_code"));
					} else {
						MDataMap mSubMap = XmasKv.upFactory(EKvSchema.EventChildren).hgetAll(sStockCode);
						for (String sKey : mSubMap.keySet()) {
							new LoadSubItem().deleteInfoByCode(sKey);
							XmasKv.upFactory(EKvSchema.Stock).del(sKey);
							XmasKv.upFactory(EKvSchema.Stock).del(sStockCode);
						}
					}
				}
				
			}
		}

		/**满减活动**/
		if (sEventTypeCode.equals("4497472600010008")) {
			//[形如: xs-EventSale-SI2003]
			LoadEventSale loadEventSale = new LoadEventSale();
			loadEventSale.deleteInfoByCode(mEventMap.get("seller_code"));
			PlusModelSaleQuery plusModelSaleQuery = new PlusModelSaleQuery();
			plusModelSaleQuery.setCode(mEventMap.get("seller_code"));
			loadEventSale.upInfoByCodeMain(plusModelSaleQuery);
		}
		
		/**运费减免**/
		if(sEventTypeCode.equals("4497472600010013")){
			//[形如: xs-FreeShipping-SI2003]
			LoadEventFree loadEventFree = new LoadEventFree();
			loadEventFree.deleteInfoByCode(mEventMap.get("seller_code"));
			PlusModelFreeQuery plusModelFreeQuery = new PlusModelFreeQuery();
			plusModelFreeQuery.setCode(mEventMap.get("seller_code"));
			loadEventFree.upInfoByCodeMain(plusModelFreeQuery);
		}
		
		/**拼好货**/
		if(sEventTypeCode.equals("4497472600010016")){
			//[形如: xs-GoodsProduct-SI2003]
			LoadGoodsProduct loadGoodsProduct = new LoadGoodsProduct();
			loadGoodsProduct.deleteInfoByCode(mEventMap.get("seller_code"));
			PlusModelSaleQuery plusModelSaleQuery = new PlusModelSaleQuery();
			plusModelSaleQuery.setCode(mEventMap.get("seller_code"));
			loadGoodsProduct.upInfoByCodeMain(plusModelSaleQuery);
		}
		
		/**内购**/
		if(sEventTypeCode.equals("4497472600010006")){
			//[形如: xs-EvenetPurchase-SI2003]
			LoadEventPurchase loadEventPurchase = new LoadEventPurchase();
			loadEventPurchase.deleteInfoByCode(mEventMap.get("seller_code"));
			PlusModelSaleQuery plusModelSaleQuery = new PlusModelSaleQuery();
			plusModelSaleQuery.setCode(mEventMap.get("seller_code"));
			loadEventPurchase.upInfoByCodeMain(plusModelSaleQuery);
			
		}
		
		/**会员日折扣**/
		if(sEventTypeCode.equals("4497472600010018")){
			//[形如: xs-VipDiscount-SI2003]
			LoadEventVipDiscount loadEventVipDiscount = new LoadEventVipDiscount();
			loadEventVipDiscount.deleteInfoByCode(mEventMap.get("seller_code"));
			PlusModelSaleQuery discountQuery = new PlusModelSaleQuery();
			discountQuery.setCode(mEventMap.get("seller_code"));
			loadEventVipDiscount.upInfoByCodeMain(discountQuery);
		}
		
		/** 橙意会员卡**/
		if(sEventTypeCode.equals("4497472600010026")){
			//[形如: xs-EventPlusList-SI2003]
			new LoadEventInfoPlusList().refresh(new PlusModelQuery(mEventMap.get("seller_code")));;
		}
		
		/** 在线支付立减活动**/
		if(sEventTypeCode.equals("4497472600010021")){
			//[形如: xs-EventOnlinePay-SI2003]
			new LoadEventOnlinePay().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		return true;
	}
	
	// 互动活动内容变更时调用
	public static void onChangeHuDongEvent(String eventCode) {
		// 已发布的情况下修改需要刷新缓存
		if(DbUp.upTable("sc_hudong_event_info").count("event_code",eventCode,"event_status","4497472700020002") > 0) {
			new LoadHuDongEvent().refresh(new PlusModelQuery(eventCode));
		}
	}
	
	/**
	 * 刷新缓存
	 * @param sEventCode
	 * 2020年5月6日
	 * Angel Joy
	 * void
	 */
	private static void reflushTemplateCash(String sEventCode) {
		if(DbUp.upTable("za_exectimer").count("exec_type","449746990031","exec_info",sEventCode,"flag_success","0") <= 0) {
			JobExecHelper.createExecInfo("449746990031", sEventCode, DateUtil.getSysDateTimeString());
		}
		
	}
	
	
	public static boolean clearCache(String sEventCode) {
		// 删除活动的缓存    [形如:xs-Event-CX2015072800003]
		new LoadEventInfo().deleteInfoByCode(sEventCode);
		reflushTemplateCash(sEventCode);
		MDataMap mEventMap = DbUp.upTable("sc_event_info").one("event_code",
				sEventCode);

		String sEventTypeCode = mEventMap.get("event_type_code");
		
		boolean bFlagEnable = PlusHelperEvent.checkEventStatus(mEventMap.get("event_status"));

		for (MDataMap map : DbUp.upTable("sc_event_item_product").queryByWhere("event_code", sEventCode)) {

			String sItemCode = map.get("item_code");

			String sStockCode = map.get("sku_code");

			new LoadEventItemProduct().deleteInfoByCode(sItemCode);
			
			new LoadSkuItem().deleteInfoByCode(sItemCode);
			
			new LoadSkuInfo().deleteInfoByCode(sStockCode);
			
			XmasKv.upFactory(EKvSchema.Iconst).del(sItemCode);

			if (bFlagEnable && map.get("flag_enable").equals("1")) {
				PlusModelEventLock plusEventLock = new PlusModelEventLock();
				plusEventLock.setBeginTime(mEventMap.get("begin_time"));
				plusEventLock.setEndTime(mEventMap.get("end_time"));
				plusEventLock.setLockNumber(Integer.valueOf(map.get("sales_num")));
				plusEventLock.setLockSource(sItemCode);
				
				XmasKv.upFactory(EKvSchema.EventLock).hset(sStockCode, sItemCode, GsonHelper.toJson(plusEventLock));

			} else {
				XmasKv.upFactory(EKvSchema.EventLock).hdel(sStockCode, sItemCode);

			}
		}

		if (PlusHelperEvent.checkSmgOrDm(sEventTypeCode) && mEventMap.get("flag_enable").equals("1")) {
			for (MDataMap map : DbUp.upTable("sc_event_item_product").queryByWhere("event_code", sEventCode)) {
				String sStockCode = map.get("sku_code");
				if (bFlagEnable) {
					XmasKv.upFactory(EKvSchema.SubEventCode).set(sStockCode, sEventCode);
					
					String sIcKey = StringUtils.defaultIfBlank(XmasKv.upFactory(EKvSchema.SubIcCode).get(sStockCode), "");
					if (StringUtils.isNotBlank(sIcKey)) {
						sIcKey = sIcKey + ",";
					}
					
					XmasKv.upFactory(EKvSchema.SubIcCode).set(sStockCode, sIcKey + map.get("item_code"));
				} else {
					MDataMap mSubMap = XmasKv.upFactory(EKvSchema.EventChildren).hgetAll(sStockCode);
					for (String sKey : mSubMap.keySet()) {
						new LoadSubItem().deleteInfoByCode(sKey);
						XmasKv.upFactory(EKvSchema.Stock).del(sKey);
						XmasKv.upFactory(EKvSchema.Stock).del(sStockCode);
					}
				}
				
			}
		}

		if (sEventTypeCode.equals("4497472600010008")) {
			new LoadEventSale().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		if(sEventTypeCode.equals("4497472600010013")){
			new LoadEventFree().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		if(sEventTypeCode.equals("4497472600010016")){
			new LoadGoodsProduct().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		if(sEventTypeCode.equals("4497472600010006")){
			new LoadEventPurchase().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		if(sEventTypeCode.equals("4497472600010018")){
			new LoadEventVipDiscount().deleteInfoByCode(mEventMap.get("seller_code"));
		}
		
		return true;
	}
	
	/**
	 * 当订单创建时调用
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public static boolean onOrderCreate(
			PlusModelNoticeOrder plusModelNoticeOrder) {
		PlusHelperScheduler.sendSchedler(EPlusScheduler.CreateOrder,
				plusModelNoticeOrder.getOrderCode(), plusModelNoticeOrder);

		new PlusSupportMember().onChangeOrder(
				plusModelNoticeOrder.getMemberCode(), 1);

		// JmsSupport.getInstance().sendMessage(sTypeName, sMsg, mPropMap,
		// eMessageType);

		return true;
	}

	/**
	 * 当活动订单创建成功时调用该方法 如果有多个Iccode需要调多次
	 * 
	 * @param sOrderCode
	 * @param sMemberCode
	 * @param sItemCode
	 * @param lNumber
	 * @return
	 */
	public static boolean onIcOrder(String sOrderCode, String sMemberCode,
			String sItemCode, long lNumber) {

		XmasKv.upFactory(EKvSchema.LogItem).hincrBy(sItemCode, sMemberCode,
				lNumber);
		XmasKv.upFactory(EKvSchema.LogItem).expire(sItemCode, 3600*24*180);

		// 记录促销订单调用的库存
		XmasKv.upFactory(EKvSchema.LogOrderStock).hset(sOrderCode, sItemCode,
				String.valueOf(lNumber));
		XmasKv.upFactory(EKvSchema.LogOrderStock).expire(sOrderCode, 3600*24*30);

		// PlusModelEventItemProduct plusModelEventItemProduct = new
		// PlusSupportEvent()
		// .upItemProductByIcCode(sItemCode);
		//
		// // 设置预计订单取消时间
		// if (plusModelEventItemProduct != null) {
		// PlusModelEventInfo plusModelEventInfo = new PlusSupportEvent()
		// .upEventInfoByCode(plusModelEventItemProduct.getEventCode());
		//
		// if (plusModelEventInfo.getCancelTime() > 0) {
		// XmasKv.upFactory(EKvSchema.TimeCancelOrder).set(
		// sOrderCode,
		// DateHelper.upDateTimeAdd(String
		// .valueOf(plusModelEventInfo.getCancelTime())
		// + "s"));
		// }
		//
		// }

		return true;
	}

	/**
	 * 订单创建时调用 用于取消订单
	 * 
	 * @param sOrderCodes
	 *            小单号
	 * @param eventCodes
	 *            非必填字段
	 * @return
	 */
	public static boolean onOrderCreateForCancelOrder(Integer isKaolaOrder, List<String> sOrderCodes,
			List<String> eventCodes) {

		long lMin = XmasSystemConst.CANCEL_ORDER_TIME;
		if(isKaolaOrder == 1) {
			lMin = XmasSystemConst.CANCEL_ORDER_KAOLA_TIME;
		}

		for (String sEventCode : eventCodes) {
			if (StringUtils.isNotBlank(sEventCode)) {
				PlusModelEventInfo plusModelEventInfo = new PlusSupportEvent()
						.upEventInfoByCode(sEventCode);

				if (plusModelEventInfo.getCancelTime() > 0) {

					lMin = Math.min(lMin, plusModelEventInfo.getCancelTime());
				}
			}
		}
		for (String sOrderCode : sOrderCodes) {
			if(StringUtils.isNotBlank(sOrderCode)) {
				// 设置订单取消时间
				XmasKv.upFactory(EKvSchema.TimeCancelOrder).setex(sOrderCode,
						(int) lMin * 10,
						DateHelper.upDateTimeAdd(String.valueOf(lMin) + "s"));
			}
		}

		return true;
	}

	/**
	 * 取消订单时调用 返回促销的库存
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public static boolean onCancelIcOrder(String sOrderCode, String memberCode) {

		MDataMap map = XmasKv.upFactory(EKvSchema.LogOrderStock).hgetAll(
				sOrderCode);
		if (map != null && map.size() > 0) {

			PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
			for (String sKey : map.upKeys()) {

				XmasKv.upFactory(EKvSchema.LogItem).hincrBy(sKey, memberCode,
						0 - Long.parseLong(map.get(sKey)));

				plusSupportEvent.subtractSkuStock(sKey,
						0 - Long.parseLong(map.get(sKey)));
			}

		}

		new PlusSupportMember().onChangeOrder(memberCode, -1);

		return true;
	}
	
	/**
	 * 修改订单SKU时调用 返回促销的库存
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public static boolean onCancelIcOrder(String sOrderCode, String memberCode, String skuCode) {

		MDataMap map = XmasKv.upFactory(EKvSchema.LogOrderStock).hgetAll(
				sOrderCode);
		if (map != null && map.size() > 0) {

			PlusSupportEvent plusSupportEvent = new PlusSupportEvent();
			for (String sKey : map.upKeys()) {
				// 只回滚当前SKU的库存
				if(DbUp.upTable("oc_order_activity").count("order_code", sOrderCode, "sku_code", skuCode, "out_active_code", sKey) > 0) {
					XmasKv.upFactory(EKvSchema.LogItem).hincrBy(sKey, memberCode,
							0 - Long.parseLong(map.get(sKey)));

					plusSupportEvent.subtractSkuStock(sKey,
							0 - Long.parseLong(map.get(sKey)));
				}
			}

		}

		return true;
	}
	
	/**
	 * 取消订单时调用 返还促销的库存以及商品库存
	 * 每次调用都会加库存，不能重复调用
	 * @param sOrderCode
	 * @return
	 */
	public static void onCancelOrderForStock(String sOrderCode) {
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("buyer_code,small_seller_code", "", "", "order_code", sOrderCode);
		
		// 只对商户订单返商品库存
		if(StringUtils.isNotBlank(orderInfo.get("small_seller_code")) && !orderInfo.get("small_seller_code").startsWith("SI")){
			onCancelIcOrder(sOrderCode, orderInfo.get("buyer_code"));
			
			List<MDataMap> detalList = DbUp.upTable("oc_orderdetail").queryAll("product_code,sku_code,sku_num", "", "", new MDataMap("order_code", sOrderCode,"gift_flag","1"));
			PlusSupportStock pss = new PlusSupportStock();
			for(MDataMap mData : detalList){
				// 多货主商品取消时不返还库存，库存同步以定时为准
				if(DbUp.upTable("pc_productinfo_ext").count("product_code",mData.get("product_code"),"delivery_store_type","4497471600430002") > 0) {
					continue;
				}
				pss.skuStockForCancelOrder(sOrderCode, mData.get("sku_code"), 0 - NumberUtils.toInt(mData.get("sku_num")));
			}
		}
	}

	/**
	 * 当修改商品或者SKU的信息时调用此方法逻辑
	 * 
	 * @param sProductCode
	 * @return
	 */
	public static boolean onChangeProductInfo(String sProductCode) {

		// 循环删除所有商品下关联的子活动
		for (String sKey : XmasKv.upFactory(EKvSchema.ProductIcChildren)
				.hgetAll(sProductCode).keySet()) {
			XmasKv.upFactory(EKvSchema.IcSku).del(sKey);
			
			// 解决扫码购商品扫码价库存缓存不更新的问题
			XmasKv.upFactory(EKvSchema.Stock).del(sKey);
		}

		for (MDataMap mDataMap : DbUp.upTable("pc_skuinfo").queryAll(
				"sku_code", "", "", new MDataMap("product_code", sProductCode))) {
			String sSkuCode = mDataMap.get("sku_code");
			new LoadSkuInfo().deleteInfoByCode(sSkuCode);
			onChangeSkuStock(sSkuCode);
			new LoadSkuPriceChange().deleteInfoByCode(sSkuCode);
		}
		//删除促销里非扫码购活动缓存 (算是填了一个坑)
		for (MDataMap mDataMap : DbUp.upTable("sc_event_item_product").queryAll(
				"item_code", "", "", new MDataMap("product_code", sProductCode))) {
			String itemCode = mDataMap.get("item_code");
			XmasKv.upFactory(EKvSchema.IcSku).del(itemCode);
			
		}

		XmasKv.upFactory(EKvSchema.Product).del(sProductCode);
		XmasKv.upFactory(EKvSchema.ProductSku).del(sProductCode);
		XmasKv.upFactory(EKvSchema.ProductSales).del(sProductCode);		//刷新销量缓存
		XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del(sProductCode);  // 刷新商品关联的权威标识
		XmasKv.upFactory(EKvSchema.SkuInfoSpread).del(sProductCode);  // 刷新商品扩展信息

		return true;
	}

	/**
	 * 当修改SKU库存时调用
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public static boolean onChangeSkuStock(String sSkuCode) {
		XmasKv.upFactory(EKvSchema.Stock).del(sSkuCode);
		XmasKv.upFactory(EKvSchema.SkuStoreStock).del(sSkuCode);
		return true;
	}

	/**
	 * 更新运费模板是调用
	 * 
	 * @param tplUid
	 * @return
	 */
	public static boolean onChangeFreight(String tplUid) {
		XmasKv.upFactory(EKvSchema.Freight).del(tplUid);
		return true;
	}

}
