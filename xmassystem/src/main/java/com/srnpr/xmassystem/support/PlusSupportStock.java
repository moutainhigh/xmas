package com.srnpr.xmassystem.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.helper.PlusHelperScheduler;
import com.srnpr.xmassystem.modelevent.PlusModelEventLock;
import com.srnpr.xmassystem.modelproduct.PlusModelStockChange;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;

public class PlusSupportStock extends BaseClass {

	/**
	 * 获取SKU的库存数量
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public int upAllStock(String sSkuCode) {
		return Integer.valueOf(String.valueOf(upSkuStockBySkuCode(sSkuCode)));
	}

	/**
	 * 获取SKU的可售库存 该方法返回的是总库存
	 * @see PlusSupportStock#upAllStock
	 * @param sSkuCode
	 * @return
	 */
	public int upSalesStock(String sSkuCode) {
//		int stock = upAllStock(sSkuCode) - upLockStock(sSkuCode);
//
//		// 拿到关联商品 并且扣减调关联商品的库存数据
//		String sConcatSku = XmasKv.upFactory(EKvSchema.ConcatSku).get(sSkuCode);
//		if (StringUtils.isNotBlank(sConcatSku)) {
//			stock = stock - upLockStock(sConcatSku);
//		}
		int stock = upAllStock(sSkuCode);

		return stock < 0 ? 0 : stock;
	}

	/**
	 * 获取锁定库存 该方法返回SKU的被活动锁定的库存
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public int upLockStock(String sSkuCode) {
		int iLockNum = 0;

		/*
		 * if (XmasKv.upFactory(EKvSchema.LockStock).exists(sSkuCode)) { String
		 * sLockString = XmasKv.upFactory(EKvSchema.LockStock).hget( sSkuCode,
		 * XmasSystemConst.LOCK_STOCK_NUM); if
		 * (StringUtils.isNotBlank(sLockString)) {
		 * 
		 * if (DateHelper.parseDate(
		 * XmasKv.upFactory(EKvSchema.LockStock).hget(sSkuCode,
		 * XmasSystemConst.LOCK_STOCK_TIME)).after( new Date())) { iLockNum =
		 * Integer.valueOf(sLockString); }
		 * 
		 * } }
		 */

		MDataMap map = XmasKv.upFactory(EKvSchema.EventLock).hgetAll(sSkuCode);

		if (map != null && map.size() > 0) {
			Date dNow = new Date();
			//循环该SKU所有的库存锁定标记
			for (String sKey : map.keySet()) {

				PlusModelEventLock plusEventLock = new GsonHelper().fromJson(
						map.get(sKey), new PlusModelEventLock());
				//判断锁的有效时间
				if (DateHelper.parseDate(plusEventLock.getBeginTime()).before(
						dNow)
						&& DateHelper.parseDate(plusEventLock.getEndTime())
								.after(dNow)) {
					
					//第一种锁 按照活动锁定最大可售数量(正常代码)
					//iLockNum+=plusEventLock.getLockNumber();

					//应mazc的猥琐要求  将库存锁调整为第二种锁定操作   由此造成的超卖以及库存不准他认了  --2015-10-31 18:36:00
					iLockNum+=new PlusSupportEvent().upEventItemSkuStock(plusEventLock.getLockSource());
				}
				
				
				//如果锁的截止时间 大于当前时间  则表示该锁定已失效 清除缓存的锁定标记
				if(DateHelper.parseDate(plusEventLock.getEndTime())
						.before(dNow))
				{
					XmasKv.upFactory(EKvSchema.EventLock).hdel(sSkuCode,sKey);
				}

			}
		}

		return iLockNum;
	}

	/**
	 * 根据sku编号获取SKU的库存数据
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public long upSkuStockBySkuCode(String sSkuCode) {
		String sStock = upSkuStockOrFromCode(sSkuCode);
		// 如果是复制商品标记
		if (sStock.startsWith(XmasSystemConst.CONCAT_STOCK_START)) {
			sStock = upSkuStockOrFromCode(StringUtils.substringAfter(sStock,
					XmasSystemConst.CONCAT_STOCK_START));
		}

		long lReturn=Long.parseLong(sStock);
		return lReturn>0?lReturn:0;

	}

	/**
	 * 获取SKU的库存或者SKU的来源编号
	 * 
	 * @param sSkuCode
	 * @return
	 */
	private String upSkuStockOrFromCode(String sSkuCode) {

		String sStock = XmasKv.upFactory(EKvSchema.Stock).get(sSkuCode);
		MDataMap SkuStoreStock = XmasKv.upFactory(EKvSchema.SkuStoreStock).hgetAll(sSkuCode);
		if (StringUtils.isBlank(sStock) || null == SkuStoreStock ||  SkuStoreStock.size() <= 0) {

			sStock = "0";

			/*
			 * sStock = DbUp .upTable("sc_store_skunum")
			 * .dataGet("ifnull(sum(stock_num),0)", "", new MDataMap("sku_code",
			 * sSkuCode)).toString();
			 */

			List<MDataMap> listMaps = DbUp.upTable("sc_store_skunum").queryAll(
					"ifnull(stock_num,0) as stock_num,store_code", "", "",
					new MDataMap("sku_code", sSkuCode));

			// 如果有库存信息 则初始化库存
			if (listMaps.size() > 0) {

				// 首先清除掉缓存数据
				XmasKv.upFactory(EKvSchema.SkuStoreStock).del(sSkuCode);

				int iNumber = 0;
				for (MDataMap map : listMaps) {
					iNumber += Integer.valueOf(map.get("stock_num"));
					XmasKv.upFactory(EKvSchema.SkuStoreStock).hset(sSkuCode,
							map.get("store_code"), map.get("stock_num"));
				}
				sStock = String.valueOf(iNumber);

			} else {

				// 如果没有库存信息 则取一下来源编号 看下来源信息是否为空
				String sFromCode = String.valueOf(DbUp.upTable("pc_skuinfo")
						.dataGet("sku_code_old", "",
								new MDataMap("sku_code", sSkuCode)));

				// 如果来源SKU不为空 则设置上值
				if (StringUtils.isNotBlank(sFromCode)
						&& !sFromCode.equals(sSkuCode)) {

					XmasKv.upFactory(EKvSchema.ConcatSku).set(sFromCode,
							sSkuCode);
					XmasKv.upFactory(EKvSchema.ConcatSku).set(sSkuCode,
							sFromCode);

					sStock = XmasSystemConst.CONCAT_STOCK_START + sFromCode;
				}

			}

			XmasKv.upFactory(EKvSchema.Stock).setnx(sSkuCode, sStock);

			// 设置过期时间 防止时间太长
			// XmasKv.upFactory(EKvSchema.Stock).expire(sSkuCode,
			// XmasSystemConst.STOCK_TTL_TIME);

		}

		return sStock;
	}

	/**
	 * 扣减库存 返回的如果是空则表示扣减失败 如果非空表示扣减的流水
	 * 
	 * @param sSkuCode
	 * @param iSubNumber
	 * @return
	 */
	public String subtractSkuStock(String sOrderCode, String sSkuCode,
			int iSubNumber) {

		String sReturn = "";

		String sStock = upSkuStockOrFromCode(sSkuCode);

		// 如果是复制商品标记 则将SKU编号置为复制来源的商品编号 并重新获取库存数据
		if (sStock.startsWith(XmasSystemConst.CONCAT_STOCK_START)) {

			sSkuCode = StringUtils.substringAfter(sStock,
					XmasSystemConst.CONCAT_STOCK_START);

			sStock = upSkuStockOrFromCode(sSkuCode);
		}

		int iAllStock = Integer.valueOf(sStock);

		// 首先判断必须小于全部库存量
		if (iAllStock >= iSubNumber) {

			boolean bFlag = true;

			List<String> lStrings = new ArrayList<String>();

			// 获取所有分库库存数据
			MDataMap map = XmasKv.upFactory(EKvSchema.SkuStoreStock).hgetAll(
					sSkuCode);

			if (map != null && map.size() > 0) {

				// 定义重新计算库存总数的和 防止map中分库库存与总库存数量不一致造成的超额扣减
				int iRecheckStock = 0;

				for (String sStore : map.keySet()) {
					iRecheckStock += Integer.valueOf(map.get(sStore));
				}

				// 如果map中的库存量也够扣减 开始执行扣减逻辑
				if (iRecheckStock >= iSubNumber) {

					// 首先扣减总库存数量

					if (XmasKv.upFactory(EKvSchema.Stock).incrBy(sSkuCode,
							0 - iSubNumber) < 0) {
						bFlag = false;
					}

					if (bFlag) {

						for (String sStore : map.keySet()) {

							if (bFlag && iSubNumber > 0) {

								int iStoreStock = Integer.valueOf(map
										.get(sStore));
								// 如果当前库存地库存大于0
								if (iStoreStock > 0 && iSubNumber > 0) {

									int iStoreSub = Math.min(iSubNumber,
											iStoreStock);

									PlusModelStockChange plusModelStockChange = new PlusModelStockChange();

									plusModelStockChange
											.setChangeCode(sOrderCode);
									plusModelStockChange
											.setChangeNumber(0 - iStoreSub);
									plusModelStockChange
											.setCreateTime(DateHelper.upNow());
									plusModelStockChange.setSkuCode(sSkuCode);
									plusModelStockChange.setStoreCode(sStore);

									PlusHelperScheduler
											.sendSchedler(
													EPlusScheduler.StockChangeLog,
													KvHelper.upCode(EPlusScheduler.StockChangeLog
															.toString()),
													plusModelStockChange);

									// 判断如果扣减失败 通常发生再各种并发逻辑上
									if (XmasKv.upFactory(
											EKvSchema.SkuStoreStock).hincrBy(
											sSkuCode, sStore, 0 - iStoreSub) < 0) {
										bFlag = false;

									} else {
										iSubNumber = iSubNumber - iStoreSub;
										lStrings.add(sStore + "_" + iStoreSub);
									}

								}

							}
						}
					}

					// 如果扣减库存都操作成功 则返回数据
					if (bFlag && iSubNumber == 0 && lStrings.size() > 0) {

						sReturn = StringUtils.join(lStrings, ",");
					}

				}

			}

		}

		return sReturn;
	}

	/**
	 * 取消订单 回滚sku实际库存
	 * 
	 * @param sSkuCode
	 * @param iSubNumber
	 * @return
	 */
	public String skuStockForCancelOrder(String sOrderCode, String sSkuCode,
			int iSubNumber) {

		String sReturn = "";

		String sStock = upSkuStockOrFromCode(sSkuCode);

		// 如果是复制商品标记 则将SKU编号置为复制来源的商品编号 并重新获取库存数据
		if (sStock.startsWith(XmasSystemConst.CONCAT_STOCK_START)) {

			sSkuCode = StringUtils.substringAfter(sStock,
					XmasSystemConst.CONCAT_STOCK_START);

			sStock = upSkuStockOrFromCode(sSkuCode);
		}

		int iAllStock = Integer.valueOf(sStock);

		// 首先判断必须小于全部库存量
		if (iAllStock >= iSubNumber) {

			boolean bFlag = true;

			List<String> lStrings = new ArrayList<String>();

			// 获取所有分库库存数据
			MDataMap map = XmasKv.upFactory(EKvSchema.SkuStoreStock).hgetAll(
					sSkuCode);

			if (map != null && map.size() > 0) {

				// 定义重新计算库存总数的和 防止map中分库库存与总库存数量不一致造成的超额扣减
				int iRecheckStock = 0;

				for (String sStore : map.keySet()) {
					iRecheckStock += Integer.valueOf(map.get(sStore));
				}

				// 如果map中的库存量也够扣减 开始执行扣减逻辑
				if (iRecheckStock >= iSubNumber) {

					// 首先扣减总库存数量

					if (XmasKv.upFactory(EKvSchema.Stock).incrBy(sSkuCode,
							0 - iSubNumber) < 0) {
						bFlag = false;
					}

					if (bFlag) {

						for (String sStore : map.keySet()) {

							if (bFlag) {

								int iStoreStock = Integer.valueOf(map
										.get(sStore));
								// 如果当前库存地库存大于0
								//以前判断是iStoreStock > 0,当有人在分地库存为0时退货,会造成分地库存无法退还的情况
								//现改成iStoreStock >= 0. by zht 2017-02-22
								if (iStoreStock >= 0 && iSubNumber < 0) {

									int iStoreSub = Math.min(iSubNumber,
											iStoreStock);

									PlusModelStockChange plusModelStockChange = new PlusModelStockChange();

									plusModelStockChange
											.setChangeCode(sOrderCode);
									plusModelStockChange
											.setChangeNumber(0 - iStoreSub);
									plusModelStockChange
											.setCreateTime(DateHelper.upNow());
									plusModelStockChange.setSkuCode(sSkuCode);
									plusModelStockChange.setStoreCode(sStore);

									PlusHelperScheduler
											.sendSchedler(
													EPlusScheduler.StockChangeLog,
													KvHelper.upCode(EPlusScheduler.StockChangeLog
															.toString()),
													plusModelStockChange);

									// 判断如果扣减失败 通常发生再各种并发逻辑上
									if (XmasKv.upFactory(
											EKvSchema.SkuStoreStock).hincrBy(
											sSkuCode, sStore, 0 - iStoreSub) < 0) {
										bFlag = false;

									} else {
										iSubNumber = iSubNumber - iStoreSub;
										lStrings.add(sStore + "_" + iStoreSub);
									}

								}

							}
						}
					}

					// 如果扣减库存都操作成功 则返回数据
					if (bFlag && iSubNumber == 0 && lStrings.size() > 0) {

						sReturn = StringUtils.join(lStrings, ",");
					}

				}

			}

		}

		return sReturn;
	}
	/**
	 * 获取product的库存数量
	 * 
	 * @param productCode
	 * @return
	 */
	public int upAllStockForProduct(String productCode) {
		
		String skuCodes = new PlusSupportProduct().upProductSku(productCode);
		int stock = 0;
		if (StringUtils.isNotBlank(skuCodes)) {
			for (String sSkuCode : skuCodes.split(",")) {
				stock += this.upAllStock(sSkuCode);
			}
		}
		return stock;
	}
}
