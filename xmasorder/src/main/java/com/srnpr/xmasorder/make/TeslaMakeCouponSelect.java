package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.AverageMoneyModel;
import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.service.TeslaCouponService;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.ArrayUtils;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取用户可使用的优惠，以及计算每个活动下最优使用的优惠券
 */
public class TeslaMakeCouponSelect extends TeslaTopOrderMake {

	TeslaCouponService couponService = new TeslaCouponService();
	LoadCouponActivity loadCouponActivity = new LoadCouponActivity();
	LoadCouponType loadCouponType = new LoadCouponType();

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();

		// 排除确定不使用优惠券的情况
		if(teslaOrder.isPointShop() || teslaOrder.getUorderInfo().getDueMoney().compareTo(BigDecimal.ZERO) == 0) {
			return result;
		}
		
		// 互动活动如果设置不支持优惠券则不计算优惠券逻辑
		if(teslaOrder.getHuDongEvent() != null && !teslaOrder.getHuDongEvent().getOrderLimit().contains("449748520002")) {
			return result;
		}
		
		// 用户所有未使用(已使用 可找零 余额大于零)的有效优惠券
		List<TeslaModelCouponInfo> couponInfoList = couponService.getAllCouponInfoList(teslaOrder);
		List<String> couponCodeList = new ArrayList<String>();
		for (TeslaModelCouponInfo ci : couponInfoList) {
			//5.5.8增加 查询优惠券使用记录缓存 防止数据库主从延迟 导致优惠券使用状态幻读
			String useFlag = XmasKv.upFactory(EKvSchema.CouponUse).get(ci.getCouponCode());
			if(StringUtils.isEmpty(useFlag) || ("Y".equals(ci.getIs_change()) && ci.getSurplusMoney().compareTo(BigDecimal.ZERO) > 0)) {
				couponCodeList.add(ci.getCouponCode());
			}
		}
		
		// 判断是否优惠券有对应可用的商品
		Map<String, List<TeslaModelOrderDetail>> resultMap = couponService.getCouponUseProductList(teslaOrder, couponCodeList);
		// 可选的优惠券列表
		List<TeslaModelCouponInfo> availableList = new ArrayList<TeslaModelCouponInfo>();
		for (TeslaModelCouponInfo ci : couponInfoList) {
			// 如果有可用的商品则设定优惠券可以选中
			if (resultMap.containsKey(ci.getCouponCode())) {
				if("Y".equals(ci.getIs_multi_use()) 
						&& (!"00".equals(loadCouponActivity.upInfoByCode(new PlusModelQuery(ci.getActivityCode())).getMinlimit_tp()) || "Y".equals(ci.getIs_change()))) {
					//可叠加优惠券且存在最低金额限制、可找零优惠券 默认都不可选
					ci.setSelectLimit("0");
				}else {
					ci.setSelectLimit("1");
				}
				availableList.add(ci);
			} else {
				ci.setSelectLimit("0");
			}
		}
		
		// 分销优惠券自动勾选记录
		Map<String,String> autoSelectCouponMap = new HashMap<String, String>();
		for(Entry<String, List<TeslaModelOrderDetail>> entry : resultMap.entrySet()) {
			for(TeslaModelOrderDetail detail : entry.getValue()) {
				if(detail.getFxFlag() == 1) {
					// 每个sku可以使用一张
					autoSelectCouponMap.put(detail.getSkuCode(), entry.getKey());
				}
			}
		}
		
		//如果非分销券，则优先勾选系统返利券 暂不处理
		/*if(autoSelectCouponMap.keySet().size()==0&&teslaOrder.getOrderDetails().size()==1) {
			for(TeslaModelCouponInfo ci : couponInfoList) {
				if("4497471600060005".equals(ci.getProvide_type())) {
					autoSelectCouponMap.put(teslaOrder.getOrderDetails().get(0).getSkuCode(), ci.getCouponCode());
				}
			}
		}*/

		teslaOrder.setCouponInfoList(couponInfoList);
		
		// 如果没有可用优惠券 或已选择完优惠券 则不再计算
		if(availableList.isEmpty() /*|| !CollectionUtils.isEmpty(teslaOrder.getUse().getCoupon_codes())*/) {
			teslaOrder.setAutoSelectCoupon("0");
			return result;
		}
		
		//每个活动下 最优的优惠券列表  key:活动  value:最优的优惠券列表
		Map<String, List<TeslaModelCouponInfo>> allActivityBestCouponInfo = new HashMap<String, List<TeslaModelCouponInfo>>();
		Map<String, List<TeslaModelCouponInfo>> couponInfoGroup = groupByCoupon(availableList);
		
		List<TeslaModelOrderDetail> detailList;
		List<AverageMoneyModel> averageModel;
		for (Entry<String, List<TeslaModelCouponInfo>> entry : couponInfoGroup.entrySet()) {
			// 最优的优惠券优惠金额
			BigDecimal bestCouponMoney = BigDecimal.ZERO;
			// 最优的优惠券列表
			List<TeslaModelCouponInfo> bestCouponInfoList = null;
			
			detailList = resultMap.get(entry.getValue().get(0).getCouponCode());
			averageModel = new ArrayList<AverageMoneyModel>();
			
			// 商品总金额
			BigDecimal productMoney = BigDecimal.ZERO;
			AverageMoneyModel model;
			for(TeslaModelOrderDetail detail : detailList) {
				model = new AverageMoneyModel();
				model.setSkuCode(detail.getSkuCode());
				model.setSkuNum(detail.getSkuNum());
				model.setSkuPrice(detail.getSkuPrice().doubleValue());
				model.setSmallSellerCode(detail.getSmallSellerCode());
				averageModel.add(model);
				productMoney = productMoney.add(detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())));	
			}
			
			// 每个优惠券对应的金额
			Map<String, BigDecimal> couponMoneyMap = new HashMap<String, BigDecimal>();
			// 折扣券计算具体的金额
			for(TeslaModelCouponInfo ci : entry.getValue()) {
				BigDecimal couponMoney = couponService.getCouponMoney(ci.getCouponCode(), teslaOrder.getUorderInfo().getBuyerCode(), averageModel);
				couponMoneyMap.put(ci.getCouponCode(), couponMoney);
			}
			
			// 总优惠券金额
			BigDecimal totalCouponMoney = BigDecimal.ZERO;
			// 多个优惠券叠加的情况计算一下所有的组合情况
			List<List<TeslaModelCouponInfo>> allSubLit = ArrayUtils.combinationList(entry.getValue());
			for (List<TeslaModelCouponInfo> subList : allSubLit) {
				PlusModelCouponActivity activity = loadCouponActivity.upInfoByCode(new PlusModelQuery(entry.getKey()));
				//不可叠加券 或可叠加且没有最低金额限制 不用计算最优情况
				if("N".equals(subList.get(0).getIs_multi_use()) 
						|| ("Y".equals(subList.get(0).getIs_multi_use()) && "00".equals(activity.getMinlimit_tp()) && "N".equals(activity.getIs_change()))) {
					continue;
				}
				
				totalCouponMoney = BigDecimal.ZERO;
				for (TeslaModelCouponInfo ci : subList) {
					totalCouponMoney = totalCouponMoney.add(couponMoneyMap.get(ci.getCouponCode()));
				}

				//5.5.4版本 应用LD最新叠加规则
				if(("Y".equals(subList.get(0).getIs_multi_use()) && !"00".equals(activity.getMinlimit_tp())) 
						&& (subList.size()>1|| (subList.size()==1 && "N".equals(activity.getIs_onelimit())))) {
					//不符合最低优惠金额 则进行叠加校验
					if(totalCouponMoney.compareTo(activity.getMindis_amt()) > 0) {
						// 总优惠下限叠加金额
						BigDecimal totalLimitMoney = BigDecimal.ZERO;
						for (TeslaModelCouponInfo ci : subList) {
							totalLimitMoney = totalLimitMoney.add(ci.getLimitMoney());
						}
						
						//叠加上限为0时 代表面额不受限 但门槛依然受限制
						//叠加上限后 面额不能大于限定金额 且门槛不能大于订单金额
						if((activity.getDisup_amt().compareTo(BigDecimal.ZERO) == 0 && totalLimitMoney.compareTo(productMoney) > 0)
								|| (activity.getDisup_amt().compareTo(BigDecimal.ZERO) > 0 && (totalCouponMoney.compareTo(activity.getDisup_amt()) > 0 || totalLimitMoney.compareTo(productMoney) > 0))) {
							//做一个老版本兼容 如果不符合限制条件的券组合 和已使用的券完全一致 则清空已使用的券列表
							List<String> alreadyUseCouponCodeList = teslaOrder.getUse().getCoupon_codes();
							List<String> nowCouponCodeList = new ArrayList<String>();
							for (TeslaModelCouponInfo couponInfo : subList) {
								nowCouponCodeList.add(couponInfo.getCouponCode());
							}
							
							if(CollectionUtils.isEqualCollection(nowCouponCodeList, alreadyUseCouponCodeList)) {
								teslaOrder.getUse().setCoupon_codes(new ArrayList<String>());
							}
							
							continue;
						}
					}
				}
				
				// 最优情况统计
				// 优先取最接近商品金额的 并且要大于等于商品金额
				// 优惠金额和面额都一样时优先取快过期的
				// 优惠金额和面额、过期时间都一样时优先取大额的
				if ((totalCouponMoney.compareTo(bestCouponMoney) > 0 && bestCouponMoney.compareTo(productMoney) < 0) 
				    || (totalCouponMoney.compareTo(bestCouponMoney) == 0 && compareEndTime(subList, bestCouponInfoList) < 0)
				    || (totalCouponMoney.compareTo(bestCouponMoney) == 0 && compareEndTime(subList, bestCouponInfoList) == 0 && subList.size() < bestCouponInfoList.size())
				    || (totalCouponMoney.compareTo(bestCouponMoney) < 0 && totalCouponMoney.compareTo(productMoney) >= 0)) {

				    bestCouponMoney = totalCouponMoney;
				    bestCouponInfoList = subList;
				    allActivityBestCouponInfo.put(entry.getKey(), bestCouponInfoList);
				}
				
			}
		}
		
		//将每个活动里允许使用的优惠券标记为可用
		for(Entry<String, List<TeslaModelCouponInfo>> entry : allActivityBestCouponInfo.entrySet()) {
			for (TeslaModelCouponInfo couponInfo : entry.getValue()) {
				couponInfo.setSelectLimit("1");
			}
		}
		
		if(teslaOrder.getActivityAgent() != null 
				&& !autoSelectCouponMap.isEmpty()
				&& teslaOrder.getUse().getCoupon_codes() != null 
				&& teslaOrder.getUse().getCoupon_codes().isEmpty()) {
			// 第一次进入确认页面时设置为自动勾选
			if("1".equals(teslaOrder.getAutoSelectCoupon())) {
				// 排重后设置自动勾选的优惠券
				teslaOrder.getUse().getCoupon_codes().addAll(new HashSet<String>(autoSelectCouponMap.values()));
			}
		}
		
		return result;
	}

	// 可叠加的优惠券优惠券分组，不可叠加的单独一个组
	private Map<String, List<TeslaModelCouponInfo>> groupByCoupon(List<TeslaModelCouponInfo> couponInfoList) {
		Map<String, List<TeslaModelCouponInfo>> couponGroup = new HashMap<String, List<TeslaModelCouponInfo>>();

		PlusModelCouponType couponType;
		for (TeslaModelCouponInfo couponInfo : couponInfoList) {
			couponType = loadCouponType.upInfoByCode(new PlusModelQuery(couponInfo.getCouponTypeCode()));
			if (!"Y".equals(couponInfo.getIs_multi_use()) || "449748120002".equals(couponType.getMoneyType())) {
				// 不可叠加的优惠券每张单独分组,包含折扣券
				couponGroup.put(couponInfo.getCouponCode(), Arrays.asList(couponInfo));
				continue;
			} else {
				// 可叠加的优惠券按活动编号分组
				if (!couponGroup.containsKey(couponInfo.getActivityCode())) {
					couponGroup.put(couponInfo.getActivityCode(), new ArrayList<TeslaModelCouponInfo>());
				}

				couponGroup.get(couponInfo.getActivityCode()).add(couponInfo);
			}
		}

		return couponGroup;
	}

	/**
	 * 
	 * 对比两个列表中的优惠券过期时间
	 * 
	 * @param list1
	 * @param list2
	 * @return 0 过期时间相同， -1 list1的过期时间早于list2， 1 list1的过期时间晚于list2
	 */
	private int compareEndTime(List<TeslaModelCouponInfo> list1, List<TeslaModelCouponInfo> list2) {
		String minEndTime1 = "";
		for(TeslaModelCouponInfo ci : list1) {
			if(ci.getEndTime().compareTo(minEndTime1) < 0 || minEndTime1.isEmpty()) {
				minEndTime1 = ci.getEndTime();
			}
		}
		
		String minEndTime2 = "";
		for(TeslaModelCouponInfo ci : list2) {
			if(ci.getEndTime().compareTo(minEndTime2) < 0 || minEndTime2.isEmpty()) {
				minEndTime2 = ci.getEndTime();
			}
		}
		
		return minEndTime1.compareTo(minEndTime2);
	}

}
