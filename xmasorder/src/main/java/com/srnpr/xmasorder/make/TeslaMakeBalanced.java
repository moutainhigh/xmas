package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.AverageMoneyModel;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.service.AverageMoneyService;
import com.srnpr.xmasorder.service.TeslaCouponService;
import com.srnpr.xmasorder.service.TeslaCrdtService;
import com.srnpr.xmasorder.service.TeslaPpcService;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelbean.HjybeanConsumeSetModel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
/**
 * 
 * 均衡分配各种金额(优惠券、微公社余额、储值金、暂存款、积分、惠豆)
 * @author xiegj
 *
 */
public class TeslaMakeBalanced extends TeslaTopOrderMake {

	PlusServiceAccm accm = new PlusServiceAccm();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) { 
		TeslaXResult result = new TeslaXResult();
//		useCouponsForProduct(teslaOrder);
		useCouponsForProductNew(teslaOrder);
//		useWgsForOrder(teslaOrder);
//		useWgsForOrderNew(teslaOrder);
		
		//摊分惠豆
		//result = useHjyBeanForOrder(teslaOrder);
		//if(result.getResultCode() != 1) {
		//	return result;
		//}
		
		if(teslaOrder.getStatus().getExecStep()==ETeslaExec.PCCreate){
			if(BigDecimal.valueOf(teslaOrder.getUse().getCzj_money()).add(BigDecimal.valueOf(teslaOrder.getUse().getZck_money())).compareTo(teslaOrder.getUorderInfo().getDueMoney())>0){
				result.setResultCode(963902220);
				result.setResultMessage(bInfo(963902220));
			}else {
//				userCzjZckForOrder(teslaOrder);
				userCzjZckForOrder3(teslaOrder);
			}
		}
		String custId = getCustId(teslaOrder.getUorderInfo().getBuyerCode());
		GetCustAmtResult custAmt = null;
		if(StringUtils.isNotBlank(custId)){
			//设置custId
			teslaOrder.setCustId(custId);
			//查积分、储值金、暂存款
			custAmt = getPlusModelCustAmt(custId);
		}
		
		// 如果使用了储值金但是未查询到储值金数据
		if(custAmt == null){
			if(teslaOrder.getUse().getIntegral() > 0)  result.inErrorMessage(963906053);
			if(teslaOrder.getUse().getCzj_money() > 0) result.inErrorMessage(963907053);
			if(teslaOrder.getUse().getZck_money() > 0) result.inErrorMessage(963908053);
			if(teslaOrder.getUse().getHjycoin() > 0) result.inErrorMessage(963908653);
			return result;
		}
		// 计算积分相关逻辑
		if(accm.isEnabledAccm()){
			result = userIntegralForOrder(teslaOrder, custAmt);
		}else if(teslaOrder.getUse().getIntegral() > 0){
			result.setResultCode(963906055);
			result.setResultMessage(bInfo(963906055));
		}
		
		if(result.upFlagTrue() && (teslaOrder.getStatus().getExecStep()==ETeslaExec.Confirm ||teslaOrder.getStatus().getExecStep()==ETeslaExec.Create)) {
			if(!teslaOrder.isPointShop()) {//5.2.2积分商城不支持储值金、暂存款
				
				// 计算惠币的使用以及可用
				result = useHjycoinForOrder(teslaOrder, custAmt);
				if(result.getResultCode() != 1) {
					return result;
				}
				
				// 计算储值金相关逻辑 目前PC端允许用户手输使用金额  其他渠道全为最大可用 20180528 -rhb
				result = new TeslaPpcService().userCzjForOrder(teslaOrder, custAmt);
				if(result.getResultCode() != 1) {
					return result;
				}
				// 计算暂存款相关逻辑 目前PC端允许用户手输使用金额  其他渠道全为最大可用 20180528 -rhb
				result = new TeslaCrdtService().userZckForOrder(teslaOrder, custAmt);
			}
		}
		
		return result;
	}
	
	/**
	
	 */
	/**
	 * 平摊优惠券的金额
	 * @author fq 重写 此类中useCouponsForProduct 方法
	 * @param order
	 * @return
	 */
	public TeslaXOrder useCouponsForProductNew(TeslaXOrder order) {
		
		Map<String, String> skuSmallSellerCode = new HashMap<>();//<商品编号,商户编号>
		for (int j = 0; j < order.getOrderDetails().size(); j++) {//各参数赋值
			TeslaModelOrderDetail od = order.getOrderDetails().get(j);
			if("1".equals(od.getGiftFlag())){
				skuSmallSellerCode.put(od.getSkuCode(), od.getSmallSellerCode());
			}
		}
		
		Map<String,List<TeslaModelOrderDetail>> resultMap = new TeslaCouponService().getCouponUseProductList(order, order.getUse().getCoupon_codes());
		
		// 可使用优惠券的商品
		Map<String,String> skuNumMap = new HashMap<String, String>();
		if(!resultMap.isEmpty()){
			// 使用的优惠券以最终计算出来可用的为准，避免客户端提交多张优惠券都使用了的情况
			order.getUse().setCoupon_codes(new ArrayList<String>(resultMap.keySet()));
			
			// 默认取第一个可用的商品列表，多张优惠券时默认商品都是一致，目前支持叠加使用的只有LD同步来的礼金券
//			Iterator<TeslaModelOrderDetail> skuNumIterator = resultMap.values().iterator().next().iterator();

//			List<AverageMoneyModel> averageModel = new ArrayList<AverageMoneyModel>();//需要平摊优惠金额的sku
//			while (skuNumIterator.hasNext()) {
//				TeslaModelOrderDetail detail = skuNumIterator.next();
//				AverageMoneyModel averageMoneyModel = new AverageMoneyModel();
//				averageMoneyModel.setSkuCode(detail.getSkuCode());
//				averageMoneyModel.setSkuNum(detail.getSkuNum());
//				averageMoneyModel.setSkuPrice(detail.getSkuPrice().doubleValue());
//				averageMoneyModel.setSmallSellerCode(detail.getSmallSellerCode());
//				averageModel.add(averageMoneyModel);
//				skuNumMap.put(detail.getSkuCode(), "");
//			}
			
			// 兼容分销叠加优惠券可使用商品限制不相同的情况
			List<AverageMoneyModel> averageModel;
			Map<String,List<AverageMoneyModel>> couponSkusMap = new HashMap<String, List<AverageMoneyModel>>();
			for(Entry<String, List<TeslaModelOrderDetail>> entry : resultMap.entrySet()) {
				averageModel = new ArrayList<AverageMoneyModel>();//需要平摊优惠金额的sku
				List<String> skuList = new ArrayList<String>();
				for(TeslaModelOrderDetail detail : entry.getValue()) {
					// 赠品和加价购不支持优惠券
					if(!"1".equals(detail.getGiftFlag()) || "1".equals(detail.getIfJJGFlag())){
						continue;
					}
					AverageMoneyModel averageMoneyModel = new AverageMoneyModel();
					averageMoneyModel.setSkuCode(detail.getSkuCode());
					averageMoneyModel.setSkuNum(detail.getSkuNum());
					averageMoneyModel.setSkuPrice(detail.getSkuPrice().doubleValue());
					averageMoneyModel.setSmallSellerCode(detail.getSmallSellerCode());
					averageModel.add(averageMoneyModel);
					
					skuList.add(detail.getSkuCode());
					
					// 记录一下可用优惠券的商品，以及分销商品sku可用的券编号
					skuNumMap.put(detail.getSkuCode(), entry.getKey());
				}
				
				if(!averageModel.isEmpty()) {
					couponSkusMap.put(entry.getKey(), averageModel);
				}
			}
			
			List<String> couponCodes = new ArrayList<String>(order.getUse().getCoupon_codes());
			List<BigDecimal> couponMoneys = new ArrayList<BigDecimal>();
			List<String> couponTypes = new ArrayList<String>();
			List<String> isMultiUse = new ArrayList<String>();
			for(String couponCode : couponCodes) {
				if(!couponSkusMap.containsKey(couponCode)) {
					order.getUse().getCoupon_codes().remove(couponCode);
					continue;
				}
				
				//ld的优惠券 没有外部优惠券编号、版本低于5.2.8、活动未发布、活动类型未发布 任一情况 则不能使用此优惠券 -rhb 20181113
				Map<String, Object> maps = new TeslaCouponService().checkIsLdAndStatus(couponCode);
				String version = order.getUorderInfo().getAppVersion();
				if (null == maps || ("ld".equals(maps.get("creater"))
						&& ((StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.2.8") < 0)
								|| !"1".equals(maps.get("flag")+"") || !"4497469400030002".equals(maps.get("status"))
								|| StringUtils.isBlank(maps.get("out_coupon_code")+"")))) {
					order.getUse().setCoupon_codes(new ArrayList<String>());
					return order;
				}
				
				
				BigDecimal couponMoney = new TeslaCouponService().getCouponMoney(couponCode,order.getUorderInfo().getBuyerCode(),couponSkusMap.get(couponCode));//优惠券金额
				couponMoneys.add(couponMoney);
				String couponType = new TeslaCouponService().getCouponMoneyType(couponCode, order.getUorderInfo().getBuyerCode());
				couponTypes.add(couponType);
				
//				// 不是礼金券则不能支持多张同时使用
//				if(!"449748120003".equals(couponType) && couponCodes.size() > 1){
//					order.getUse().setCoupon_codes(new ArrayList<String>());
//					return order;
//				}
				String isMulti = new TeslaCouponService().getCouponIsMulti(couponCode, order.getUorderInfo().getBuyerCode());
				if(!"Y".equals(isMulti) && couponCodes.size() > 1) {
					order.getUse().setCoupon_codes(new ArrayList<String>());
					return order;
				}
				isMultiUse.add(isMulti);
			}			
			order.getUse().setCoupon_Moneys(couponMoneys);
			order.getUse().setCoupon_types(couponTypes);
			order.getUse().setIs_multi_uses(isMultiUse);
			//计算优惠券总金额
			BigDecimal couponMoney = BigDecimal.ZERO;
			for(BigDecimal money : couponMoneys) {
				couponMoney = couponMoney.add(money);
			}
			
			//平摊优惠金额，返回值map（key：sku编号，value：优惠的金额）
			//Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, couponMoney);
			//活动信息和支付信息进行赋值以及订单应付款进行重新计算
			Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
			
			/**
			 * 叠加使用礼金券
			 */
			// 根据礼金券的金额倒排序
			List<Map<String,Object>> couponList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<couponCodes.size();i++ ) {
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("code", couponCodes.get(i));
				item.put("money", couponMoneys.get(i));
				couponList.add(item);
			}
			//将礼金券金额从大到小排序
			Collections.sort(couponList, new Comparator<Map<String,Object>>() {
				public int compare(Map<String,Object> moenyOne, Map<String,Object> moneyTwo) {
					BigDecimal one = (BigDecimal) moenyOne.get("money");
					BigDecimal two = (BigDecimal) moneyTwo.get("money");
					return two.compareTo(one);
				}
			});
			// LD订单优先
			Collections.sort(order.getOrderDetails(), new Comparator<TeslaModelOrderDetail>() {
				@Override
				public int compare(TeslaModelOrderDetail o1, TeslaModelOrderDetail o2) {
					if(o1.getSmallSellerCode().equals("SI2003") && !o2.getSmallSellerCode().equals("SI2003")){
						return -1;
					}
					if(!o1.getSmallSellerCode().equals("SI2003") && o2.getSmallSellerCode().equals("SI2003")){
						return 1;
					}
					
					// 大金额优先
					return o2.getSkuPrice().compareTo(o1.getSkuPrice());
				}
			});
			
			// 计算优惠券可用商品总金额
			BigDecimal totalProductMoney = BigDecimal.ZERO;					
			for (int j = 0; j < order.getOrderDetails().size(); j++) {
				TeslaModelOrderDetail od = order.getOrderDetails().get(j);
				if(!"1".equals(od.getGiftFlag()) || !skuNumMap.containsKey(od.getSkuCode())){
					continue;
				}
				totalProductMoney = totalProductMoney.add(od.getSkuPrice().multiply(new BigDecimal(od.getSkuNum()))).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			if(totalProductMoney.compareTo(BigDecimal.ZERO) <= 0) return order;
			
			List<TeslaModelOrderActivity> actList = new ArrayList<TeslaModelOrderActivity>();
			BigDecimal productMoney = null;
			BigDecimal surplusMoney = couponMoney; //剩余优惠券金额
			BigDecimal rpjlMoney = BigDecimal.ZERO;
			
			Map<String,String> skuUseCouponLock = new HashMap<String, String>();
			int availableProduct = skuNumMap.size();
			int useproduct = 0;
			
			for (int j = 0; j < order.getOrderDetails().size(); j++) {
				TeslaModelOrderDetail od = order.getOrderDetails().get(j);
				if(!"1".equals(od.getGiftFlag()) || !skuNumMap.containsKey(od.getSkuCode())||"1".equals(od.getIfJJGFlag())){
					continue;
				}
				useproduct = useproduct + 1;
				
				productMoney = od.getSkuPrice().multiply(new BigDecimal(od.getSkuNum()));
				BigDecimal useCouponMoney = productMoney.divide(totalProductMoney,4,BigDecimal.ROUND_HALF_UP).multiply(couponMoney);
				
				//如果计算的优惠券金额大于剩余金额
				if(useCouponMoney.compareTo(surplusMoney) > 0) {
					useCouponMoney = surplusMoney;
				}
				//如果是最后一件商品，将剩余的优惠金额都摊给最后一件商品
				if(useproduct == availableProduct){
					if(useCouponMoney.compareTo(surplusMoney) < 0) {
						useCouponMoney = surplusMoney;
					}
				}
				BigDecimal eveCouMoney = BigDecimal.ZERO;
				if(useproduct == availableProduct) {
					eveCouMoney = useCouponMoney.divide(new BigDecimal(od.getSkuNum()),2, BigDecimal.ROUND_UP);
				} else {
					eveCouMoney = useCouponMoney.divide(new BigDecimal(od.getSkuNum()),2, BigDecimal.ROUND_HALF_UP);
				}				
				
				// LD商品保留整数
				if("SI2003".equalsIgnoreCase(od.getSmallSellerCode())){
					eveCouMoney = eveCouMoney.setScale(0, BigDecimal.ROUND_UP);
				}
				
				// 使用的金额不能超过商品金额
				if(eveCouMoney.compareTo(od.getSkuPrice()) > 0){
					eveCouMoney = od.getSkuPrice();
				}
				
				// 确认单件商品使用的优惠券金额
				useCouponMoney = eveCouMoney.multiply(new BigDecimal(od.getSkuNum()));
				
				// 分销商品使用优惠券特殊处理，不走普通优惠券叠加逻辑
				if(od.getFxFlag() == 1) {
					eveCouMoney = BigDecimal.ZERO;
					useCouponMoney = BigDecimal.ZERO;
				}
				
				// 循环礼金券拆分使用的商品
				Map<String,Object> couponItem = null;
				BigDecimal useC = BigDecimal.ZERO; // 已经拆分的金额
				for (int i = 0; i < couponList.size(); i++) {
					couponItem = couponList.get(i);
					
					// 一个优惠券不能在同一个SKU上面使用多次
					if(skuUseCouponLock.containsKey(od.getSkuCode()+couponItem.get("code").toString())){
						continue;
					}
					
					// 优惠券的剩余金额
					BigDecimal money = (BigDecimal)couponItem.get("money");
					BigDecimal useM = null;
					
					// 忽略已经拆分完的优惠券
					if(money.compareTo(BigDecimal.ZERO) <= 0){
						continue;
					}
					
					// 分销商品使用优惠券特殊处理
					if(od.getFxFlag() == 1) {
						// 分销商品只能使用指定分销券
						if(!couponItem.get("code").equals(skuNumMap.get(od.getSkuCode()))) {
							continue;
						}
						
						// 分销商品使用本张优惠券全部金额
						eveCouMoney = money.divide(new BigDecimal(od.getSkuNum()),2, BigDecimal.ROUND_UP);
						// LD商品保留整数
						if("SI2003".equalsIgnoreCase(od.getSmallSellerCode())){
							eveCouMoney = eveCouMoney.setScale(0, BigDecimal.ROUND_UP);
						}
						useCouponMoney = eveCouMoney.multiply(new BigDecimal(od.getSkuNum()));
					}
					
					// 不能超过优惠券的使用金额
					if(useC.add(money).compareTo(useCouponMoney) > 0){
						useM = useCouponMoney.subtract(useC);
					}else{						
						//最后一件商品在使用最后一张优惠券时，会出现人品奖励
						if(useproduct == availableProduct && i == (couponList.size() - 1)) {
							rpjlMoney = useCouponMoney.subtract(useC.add(money));
							useM = money.add(rpjlMoney);
						} else {
							useM = money;
						}
					}
					
					//刚好用完
					if(useM.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					
					//已经使用的优惠金额
					useC = useC.add(useM);
					
					// 更新优惠券的剩余金额
					couponItem.put("money", money.subtract(useM));
					
					// 记录下优惠券使用记录
					TeslaModelOrderActivity oa = new TeslaModelOrderActivity();
					oa.setProductCode(od.getProductCode());
					oa.setTicketCode(couponItem.get("code").toString());
					oa.setActivityName("优惠券");
					oa.setOrderCode(od.getOrderCode());
					oa.setSkuCode(od.getSkuCode());
					oa.setPreferentialMoney(useM);
					actList.add(oa);
					
					//记录优惠券的支付信息
					TeslaModelOrderPay op = new TeslaModelOrderPay();  //插入一条优惠券支付信息
					op.setMerchantId(order.getUorderInfo().getBuyerCode());
					op.setOrderCode(od.getOrderCode());
					op.setPaySequenceid(couponItem.get("code").toString());
					op.setPayedMoney(useM);
					op.setPayType("449746280002");
					order.getOcOrderPayList().add(op);
					
					skuUseCouponLock.put(od.getSkuCode()+couponItem.get("code").toString(), "");
				}
				
				surplusMoney = surplusMoney.subtract(eveCouMoney.multiply(new BigDecimal(od.getSkuNum()))); //计算剩余金额
				
				od.setCouponPrice(eveCouMoney);//订单商品上记优惠金额
				od.setSkuPrice(od.getSkuPrice().subtract(eveCouMoney));
			}
			
			// 根据优惠券记录一下优惠券的明细
			BigDecimal allCouponMoney = BigDecimal.ZERO;
			for(TeslaModelOrderActivity oa : actList){
				BigDecimal money = map.get(oa.getOrderCode());  // 优惠券使用的金额
				if(oa.getPreferentialMoney().compareTo(BigDecimal.ZERO) <= 0){
					continue;
				}
				
				if(money == null) {
					money = oa.getPreferentialMoney();
				}else{
					money = oa.getPreferentialMoney();
				}
				
				map.put(oa.getOrderCode(), money);
				allCouponMoney = allCouponMoney.add(money);
				
				for (int k = 0; k < order.getSorderInfo().size(); k++) {
					if(order.getSorderInfo().get(k).getOrderCode().equals(oa.getOrderCode())){
						order.getSorderInfo().get(k).setDueMoney(order.getSorderInfo().get(k).getDueMoney().subtract(money));//应付款金额-优惠金额
						order.getSorderInfo().get(k).setOrderMoney(order.getSorderInfo().get(k).getOrderMoney().subtract(money));//商品总金额-微公社金额
						break;
					}
				}
			}
			
			// 使用的优惠券超过优惠券总金额，出现人品奖励
			if(allCouponMoney.compareTo(couponMoney) > 0){
				TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将人品奖励添加到activity
				activity.setOrderCode("");
				activity.setActivityName("人品奖励(优惠券引起)");
				activity.setActivityType("4497472600010009");
				activity.setPreferentialMoney(allCouponMoney.subtract(couponMoney));
				actList.add(activity);
			}
			
			order.getActivityList().addAll(actList);				
		}else {
			order.getUse().setCoupon_codes(new ArrayList<String>());
			order.getUse().setCoupon_Moneys(new ArrayList<BigDecimal>());
			order.getUse().setCoupon_types(new ArrayList<String>());
		}
		return order;
	}
	
	/**
	 * 微公社金额平摊
	 * 重写 此类中useWgsForOrder 方法
	 * @param order
	 * @return
	 */
	public TeslaXOrder useWgsForOrderNew(TeslaXOrder order) {
		
		List<AverageMoneyModel> averageModel = new ArrayList<AverageMoneyModel>();//所有需要平摊微公社金额的商品信息
		for (int j = 0; j < order.getOrderDetails().size(); j++) {//各参数赋值
			TeslaModelOrderDetail od = order.getOrderDetails().get(j);
			if("1".equals(od.getGiftFlag())){
				AverageMoneyModel averageMoneyModel = new AverageMoneyModel();
				averageMoneyModel.setSkuCode(od.getSkuCode());
				averageMoneyModel.setSkuNum(od.getSkuNum());
				averageMoneyModel.setSkuPrice(od.getSkuPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				averageMoneyModel.setSmallSellerCode(od.getSmallSellerCode());
				averageModel.add(averageMoneyModel);
				
			}
		}
		
		BigDecimal reCouponMoney = order.getUse().getWgs_money();//微公社金额(需要平摊的金额)
		if(averageModel.size() > 0 && reCouponMoney.compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal couponMoney = reCouponMoney;
			
			//平摊优惠金额，返回值map（key：sku编号，value：优惠的金额）
			Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, reCouponMoney);
			
			Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
			for (int j = 0; j < order.getOrderDetails().size(); j++) {
				TeslaModelOrderDetail od = order.getOrderDetails().get(j);
				if("1".equals(od.getGiftFlag())&&averageMoney.containsKey(od.getSkuCode())){
					if(!averageMoney.containsKey(od.getSkuCode())) {
						continue;
					}
					Double eveCouMoney = averageMoney.get(od.getSkuCode()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					order.getOrderDetails().get(j).setGroupPrice(BigDecimal.valueOf(eveCouMoney));
					order.getOrderDetails().get(j).setSkuPrice(od.getSkuPrice().subtract(BigDecimal.valueOf(eveCouMoney)));
					for (int k = 0; k < order.getSorderInfo().size(); k++) {
						if(order.getSorderInfo().get(k).getOrderCode().equals(od.getOrderCode())){
							order.getSorderInfo().get(k).setDueMoney(order.getSorderInfo().get(k).getDueMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//应付款金额-微公社金额
							order.getSorderInfo().get(k).setOrderMoney(order.getSorderInfo().get(k).getOrderMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//商品总金额-微公社金额
						}
					}
					if(map.containsKey(od.getOrderCode())){
						map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
					}else {
						map.put(od.getOrderCode(), BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum())));
					}
				}
			}
			boolean hadRPJL = false;//是否已经计算过人品奖励
			
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String orderCode = iterator.next();
				TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条微公社支付信息
				op.setMerchantId(order.getUorderInfo().getBuyerCode());
				op.setOrderCode(orderCode);
				op.setPayedMoney(map.get(orderCode));//均摊的钱
				op.setPayType("449746280009");
				if(couponMoney.doubleValue()>0&&couponMoney.compareTo(map.get(orderCode))>=0){//实际扣减的钱
					couponMoney=couponMoney.subtract(map.get(orderCode));
					op.setPayRemark(String.valueOf(map.get(orderCode)));
				}else if(couponMoney.doubleValue()>0&&couponMoney.compareTo(map.get(orderCode))<0) {
					op.setPayRemark(String.valueOf(couponMoney.doubleValue()));
					couponMoney=new BigDecimal(0.00);
				}else {
					op.setPayRemark("0");
				}
				order.getOcOrderPayList().add(op);
				if(!hadRPJL) {
					hadRPJL = true;
					//计算人品奖励(之前版本的优惠券平摊金额是没有人品奖励的，新版加了人品奖励)
					BigDecimal allUseCouponMoney = BigDecimal.ZERO;
					for (AverageMoneyModel m : averageModel) {
						allUseCouponMoney = allUseCouponMoney.add(averageMoney.get(m.getSkuCode()).multiply(BigDecimal.valueOf(m.getSkuNum())));
					}
					
					if(allUseCouponMoney.compareTo(BigDecimal.ZERO) > 0 && allUseCouponMoney.compareTo(reCouponMoney) > 0) {
						BigDecimal rpjl = allUseCouponMoney.subtract(reCouponMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
						TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将人品奖励添加到activity
						activity.setOrderCode(orderCode);
						activity.setActivityName("人品奖励(微公社引起)");
						activity.setActivityType("4497472600010009");
						activity.setPreferentialMoney(rpjl);
						order.getActivityList().add(activity);
						
					}
				}
			}
		}
		
		return order;
	}
	
	/**
	 * 惠豆平摊
	 * @param order
	 * @return
	 */
	public TeslaXResult useHjyBeanForOrder(TeslaXOrder order) {
		
		TeslaXResult result = new TeslaXResult();
		HjybeanService hjybeanService = new HjybeanService();
		HjybeanConsumeSetModel homehasBeanConsumeConfig = hjybeanService.getHomehasBeanConsumeConfig();
		order.setHomehasBeanConsumeConfig(homehasBeanConsumeConfig);
		
		BigDecimal maxPercent = homehasBeanConsumeConfig.getMaxPercent();//惠豆使用最大占订单的百分比
		BigDecimal minUse = homehasBeanConsumeConfig.getMinUse();//惠豆最小使用数量
		List<String> sellerTypeList = homehasBeanConsumeConfig.getSellerType();//使用惠豆支持的商户类型
		BigDecimal ratio = homehasBeanConsumeConfig.getRatio();//惠豆与人品币的兑换率
		
		//定义最小使用的倍数为1000；
		BigDecimal minMultiple = minUse;//将最小使用数作为整数倍
		BigDecimal useHjyBean = order.getUse().getHjyBean();//要使用的总惠豆数
		//判断要使用的惠豆数是否是制定规则的整数倍
		if(useHjyBean.compareTo(BigDecimal.ZERO) > 0 && useHjyBean.divideAndRemainder(minMultiple)[1].compareTo(BigDecimal.ZERO) > 0) {//divideAndRemainder  取余数
			result.setResultCode(963905053);
			result.setResultMessage(bInfo(963905053,minMultiple));
			return result;
		}

		
		List<AverageMoneyModel> averageModel = new ArrayList<AverageMoneyModel>();//所有需要平摊惠豆金额的商品信息
		BigDecimal allProdcutMoney = BigDecimal.ZERO;
		for (int j = 0; j < order.getOrderDetails().size(); j++) {
			TeslaModelOrderDetail od = order.getOrderDetails().get(j);
			if("1".equals(od.getGiftFlag())){
				
				String sellerType = "";
				/*String sellerType = new PlusServiceSeller().getSellerType(od.getSmallSellerCode());
				if(StringUtils.isBlank(sellerType) && "SI2003".equals(od.getSmallSellerCode())) {//如果没有查询到信息并且SmallSellerCode为SI2003时，则为LD商品
					sellerType = "4497478100050000";
				}*/
				/*
				 * 从缓存获取数据事，由于表(uc_seller_info_extend)中没有SI2003对应的数据，
				 * 缓存第一次加载没有从表里查到数据，会将model(PlusModelSellerInfo) 直接放入缓存，
				 * 这时此model的默认商户类型为普通商户，导致SI2003查询的商户类型为普通商户，
				 * 因此查库解决.
				 */
				MDataMap sellerInfo = DbUp.upTable("uc_seller_info_extend").one("small_seller_code",od.getSmallSellerCode());
				if(sellerInfo != null){
					sellerType = StringUtils.isBlank(sellerInfo.get("uc_seller_type")) ? "" : sellerInfo.get("uc_seller_type");
				}else if("SI2003".equals(od.getSmallSellerCode())){
					sellerType = "4497478100050000";
				}
				
				if(sellerTypeList.contains(sellerType)) {//判断惠豆的商户类型是否满足配置
					
					AverageMoneyModel averageMoneyModel = new AverageMoneyModel();
					averageMoneyModel.setSkuCode(od.getSkuCode());
					averageMoneyModel.setSkuNum(od.getSkuNum());
					averageMoneyModel.setSkuPrice(od.getSkuPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					averageMoneyModel.setSmallSellerCode(od.getSmallSellerCode());
					averageModel.add(averageMoneyModel);
					
					allProdcutMoney = allProdcutMoney.add(  od.getSkuPrice().multiply( BigDecimal.valueOf( od.getSkuNum() ) )   );//记录总金额
				}
				
			}
			
		}
		
		Integer allHjyBean = hjybeanService.uphjyBeanByMemberCode(order.getUorderInfo().getBuyerCode());
		order.setAllHjyBean(BigDecimal.valueOf(allHjyBean));
		
		if(averageModel.size() > 0) {
			
			//判断使用的惠豆是否满足配置的惠豆所占订单总金额的比例 (最大使用金额 = 订单总金额 * 最大占的订单金额百分比  )
			BigDecimal maxUseHjyMoney = allProdcutMoney.multiply(maxPercent).divide(BigDecimal.valueOf(100).setScale(10,BigDecimal.ROUND_HALF_UP));//此单最大能使用的金额
			if(maxUseHjyMoney.compareTo(BigDecimal.ZERO) < 0) {
				maxUseHjyMoney = BigDecimal.ZERO;
			}
			if(maxUseHjyMoney.compareTo(allProdcutMoney) > 0) {//如果可使用的最大金额大于此单的总金额
				maxUseHjyMoney = allProdcutMoney;
			}
			
			BigDecimal maxUseHjyBean = maxUseHjyMoney.divide(ratio, 2, BigDecimal.ROUND_HALF_UP);//将最大使用的RMB转换为惠豆数
			
			//将最大可使用的惠豆数取整
			if(maxUseHjyBean.compareTo(BigDecimal.valueOf(allHjyBean)) > 0) {
				BigDecimal t_multiple = BigDecimal.valueOf(allHjyBean).divide(minMultiple, 0,BigDecimal.ROUND_FLOOR);//最大能使用的惠豆的倍数
				maxUseHjyBean = t_multiple.multiply(minMultiple);
			} else {
				BigDecimal t_multiple = maxUseHjyBean.divide(minMultiple, 0,BigDecimal.ROUND_FLOOR);//最大能使用的惠豆的倍数
				maxUseHjyBean = t_multiple.multiply(minMultiple);
			}
			order.setMaxUseHjyBean(maxUseHjyBean);
			
			maxUseHjyMoney = MoneyHelper.roundHalfUp(maxUseHjyBean.multiply(ratio));
			
			//是否使用了惠豆
			if(useHjyBean.compareTo(BigDecimal.ZERO) > 0) {
				
				//将惠豆转换为人民币（RMB）
				BigDecimal alluserRmb = useHjyBean.multiply(ratio);
				
				BigDecimal useRMB = alluserRmb;
				
				Integer hjy_bean = hjybeanService.uphjyBeanByMemberCode(order.getUorderInfo().getBuyerCode());//用户总拥有的惠豆数
				
				
				
				//判断     该用户是否有惠豆，使用的惠豆是否大于拥有的惠豆总数，使用的惠豆是否小于最小使用数,
				if(hjy_bean == 0 || useHjyBean.compareTo(BigDecimal.valueOf(hjy_bean)) > 0 || useHjyBean.compareTo(minUse) < 0 ) {
					result.setResultCode(963905050);
					result.setResultMessage(bInfo(963905050));
					
				}else if( useHjyBean.compareTo(minUse) < 0 ) {//是否满足配置的最小使用惠豆数 
					
					result.setResultCode(963905054);
					result.setResultMessage(bInfo(963905054));
					
				} else if(useHjyBean.compareTo(maxUseHjyBean) > 0) {
						// 使用的惠豆数大于  总订单应占比的数量
						result.setResultCode(963905052);
						result.setResultMessage(bInfo(963905052 ));
				} else {
						
					/*
					 * 符合条件的商品开始平摊惠豆金额   ,返回值map（key：sku编号，value：优惠的金额）
					 */
					Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, useRMB);
					
					Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
					for (int j = 0; j < order.getOrderDetails().size(); j++) {//各参数赋值
						TeslaModelOrderDetail od = order.getOrderDetails().get(j);
						if("1".equals(od.getGiftFlag())&&averageMoney.containsKey(od.getSkuCode())){
							if(!averageMoney.containsKey(od.getSkuCode())) {
								continue;
							}
							Double eveCouMoney = averageMoney.get(od.getSkuCode()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							order.getOrderDetails().get(j).setHjyBean(BigDecimal.valueOf(eveCouMoney));
							order.getOrderDetails().get(j).setSkuPrice(od.getSkuPrice().subtract(BigDecimal.valueOf(eveCouMoney)));
							for (int k = 0; k < order.getSorderInfo().size(); k++) {
								if(order.getSorderInfo().get(k).getOrderCode().equals(od.getOrderCode())){
									order.getSorderInfo().get(k).setDueMoney(order.getSorderInfo().get(k).getDueMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//应付款金额-惠豆金额
									order.getSorderInfo().get(k).setOrderMoney(order.getSorderInfo().get(k).getOrderMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//商品总金额-惠豆金额
								}
							}
							if(map.containsKey(od.getOrderCode())){
								map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
							}else {
								map.put(od.getOrderCode(), BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum())));
							}
						}
					}
					boolean hadRPJL = false;//是否已经计算过人品奖励
					
					for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
						String orderCode = iterator.next();
						TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条惠豆支付信息
						op.setMerchantId(order.getUorderInfo().getBuyerCode());
						op.setOrderCode(orderCode);
						op.setPayedMoney(map.get(orderCode));//均摊的钱
						op.setPayType("449746280015");
						if(useRMB.doubleValue()>0&&useRMB.compareTo(map.get(orderCode))>=0){//实际扣减的钱
							useRMB=useRMB.subtract(map.get(orderCode));
							op.setPayRemark(String.valueOf(map.get(orderCode)));
						}else if(useRMB.doubleValue()>0&&useRMB.compareTo(map.get(orderCode))<0) {
							op.setPayRemark(String.valueOf(useRMB.doubleValue()));
							useRMB=new BigDecimal(0.00);
						}else {
							op.setPayRemark("0");
						}
						order.getOcOrderPayList().add(op);
						if(!hadRPJL) {
							hadRPJL = true;
							//计算人品奖励(之前版本的优惠券平摊金额是没有人品奖励的，新版加了人品奖励)
							BigDecimal allUseCouponMoney = BigDecimal.ZERO;
							for (AverageMoneyModel m : averageModel) {
								allUseCouponMoney = allUseCouponMoney.add(averageMoney.get(m.getSkuCode()).multiply(BigDecimal.valueOf(m.getSkuNum())));
							}
							
							if(allUseCouponMoney.compareTo(BigDecimal.ZERO) > 0 && allUseCouponMoney.compareTo(alluserRmb) > 0) {
								BigDecimal rpjl = allUseCouponMoney.subtract(alluserRmb).setScale(2, BigDecimal.ROUND_HALF_UP);
								TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将人品奖励添加到activity
								activity.setOrderCode(orderCode);
								activity.setActivityName("人品奖励(惠豆引起)");
								activity.setActivityType("4497472600010009");
								activity.setPreferentialMoney(rpjl);
								order.getActivityList().add(activity);
								
							}
						}
					}
						
						
				}
				
			}
			
		} else if(useHjyBean.compareTo(BigDecimal.ZERO) > 0) {//如果使用了惠豆数，但是没有符合规则的商品。则不返回错误信息
			result.setResultCode(963905051);
			result.setResultMessage(bInfo(963905051));
		}
		
		return result;
		
	}
	
	/**
	 *惠家有使用优惠券处理方法 (优惠券金额均等绑定在拆分的订单商品上)
	 *均等拆分方案：按照单个商品数量金额占总额比例获取单个商品优惠金额向下取整，如优惠金额向下取整后等于0，此商品不参与优惠，下一个商品再进行均摊,
	 *到最后一个商品时优惠券总额减去之前优惠的金额再均摊到此商品单品向上取整（此处有可能存在误差）
	 *如果购买数量均一样，则单价最高的商品不参与计算(边界情况，此方法为减小误差)
	 */
//	public TeslaXOrder useCouponsForProduct(TeslaXOrder order) {
//		BigDecimal skusCount = new BigDecimal(0.00);//可用优惠券的商品总金额
//		Map<String, Double> skuMonMap = new HashMap<String, Double>();//<商品编号,商品金额>
//		Map<String, Double> skuNumMap = new HashMap<String, Double>();//<商品编号,商品数量>
//		Map<String, Double> skuCouMon = new HashMap<String, Double>();//<商品编号,单品优惠券额度>
//		int num = 0;//随机获取单品的数量
//		for (int j = 0; j < order.getOrderDetails().size(); j++) {//各参数赋值
//			TeslaModelOrderDetail od = order.getOrderDetails().get(j);
//			if("1".equals(od.getGiftFlag())){
//				skuMonMap.put(od.getSkuCode(), od.getSkuPrice().doubleValue());
//				skuNumMap.put(od.getSkuCode(),Double.valueOf(od.getSkuNum()));
//				num = od.getSkuNum();
//			}
//		}
//		skuNumMap = new TeslaCouponService().getAvailableCouponProduct(order);
//		if(skuNumMap!=null&&!skuNumMap.isEmpty()){
//			Iterator<String> skuNumIterator = skuNumMap.keySet().iterator();
//			while (skuNumIterator.hasNext()) {
//				String key = (String) skuNumIterator.next();
//				skusCount=skusCount.add(BigDecimal.valueOf(skuMonMap.get(key)).multiply(BigDecimal.valueOf(skuNumMap.get(key))));
//			}
//			BigDecimal couponMoney = new TeslaCouponService().getCouponMoney(order.getUse().getCoupon_code(),order.getUorderInfo().getBuyerCode());//优惠券金额
//			order.getUse().setCoupon_Money(couponMoney);
//			BigDecimal overcouponMoney = BigDecimal.valueOf(0.00);//已使用优惠券金额
//			String couponCode = order.getUse().getCoupon_code();//优惠券编号
//			//边界情况处理逻辑:商品种类大于两种时商品数量都相等的情况，默认为true
//			boolean bjqk = true;
//			if(skuNumMap.size()>1){
//				Iterator<String> it = skuNumMap.keySet().iterator();
//				while(it.hasNext()){
//					if(skuNumMap.get(it.next())!=num){
//						bjqk=false;
//						break;
//					}
//				}
//			}else {
//				bjqk=false;
//			}
//			if(couponMoney.doubleValue()>0&&order.getOrderDetails().size()>0){
//				List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String,Double>>();
//				if(bjqk){//边界情况
//					Map<String, Double> skuSPMap = new HashMap<String, Double>();//<商品编号,商品金额>
//					Iterator<String> ks = skuNumMap.keySet().iterator();
//					while (ks.hasNext()) {
//						String key = (String) ks.next();
//						skuSPMap.put(key, skuMonMap.get(key));
//					}
//					list_Data = mapValueSortDesc(skuSPMap);//数量相等时，按金额倒序
//				}else{
//					//一般情况处理逻辑
//					list_Data = mapValueSort(skuNumMap,skuMonMap);//一般情况按数量倒序
//				}
//				for (int i = 0; i < list_Data.size(); i++) {
//					Map.Entry<String, Double> entry = list_Data.get(i);
//					String skuCode = entry.getKey();
//					Double skuNum = skuNumMap.get(skuCode);
//					BigDecimal skuPrice = BigDecimal.valueOf(skuMonMap.get(skuCode));
//					if(i==list_Data.size()-1){//最后一个商品特殊处理
//						Double eveSkuCouMon = Math.ceil(((couponMoney.subtract(overcouponMoney)).divide(BigDecimal.valueOf(skuNum),2,BigDecimal.ROUND_CEILING)).doubleValue());//向上取整 单品优惠金额
//						if(eveSkuCouMon.compareTo(skuPrice.doubleValue())>=0){
//							eveSkuCouMon=skuPrice.doubleValue();
//						}
//						skuCouMon.put(skuCode, eveSkuCouMon);
//					}else {//一般商品处理
//						Double eveSkuCouMon = Math.floor(((skuPrice.multiply(couponMoney).divide(skusCount,2,BigDecimal.ROUND_DOWN))).doubleValue());//向下取整 单品优惠金额
//						if(eveSkuCouMon>0){
//							if(eveSkuCouMon.compareTo(skuPrice.doubleValue())>=0){
//								eveSkuCouMon=skuPrice.doubleValue();
//							}
//							skuCouMon.put(skuCode, eveSkuCouMon);
//						}
//						overcouponMoney = overcouponMoney.add(BigDecimal.valueOf(eveSkuCouMon).multiply(BigDecimal.valueOf(skuNum)));
//					}
//				}
//				//活动信息和支付信息进行赋值以及订单应付款进行重新计算
//				Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
//				for (int j = 0; j < order.getOrderDetails().size(); j++) {
//					TeslaModelOrderDetail od = order.getOrderDetails().get(j);
//					if("1".equals(od.getGiftFlag())&&skuCouMon.containsKey(od.getSkuCode())){
//						Double eveCouMoney = skuCouMon.get(od.getSkuCode());
//						BigDecimal sa = od.getCouponPrice();
//						if(sa==null){
//							sa = new BigDecimal(0.00);
//						}
//						order.getOrderDetails().get(j).setCouponPrice(sa.add(BigDecimal.valueOf(eveCouMoney)));//订单商品上记优惠金额
//						order.getOrderDetails().get(j).setSkuPrice(order.getOrderDetails().get(j).getSkuPrice().subtract(BigDecimal.valueOf(eveCouMoney)));
//						//小订单待付款去掉优惠券的金额
//						for (int k = 0; k < order.getSorderInfo().size(); k++) {
//							if(order.getSorderInfo().get(k).getOrderCode().equals(od.getOrderCode())){
//								order.getSorderInfo().get(k).setDueMoney(order.getSorderInfo().get(k).getDueMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//应付款金额-优惠金额
//								order.getSorderInfo().get(k).setOrderMoney(order.getSorderInfo().get(k).getOrderMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//商品总金额-微公社金额
//								break;
//							}
//						}
//						//大订单待付款去掉优惠券的金额
//						order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
//						if(map.containsKey(od.getOrderCode())){
//							map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
//						}else {
//							map.put(od.getOrderCode(), BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum())));
//						}
//						
//						TeslaModelOrderActivity oa = new TeslaModelOrderActivity();
//						oa.setProductCode(od.getProductCode());
//						oa.setTicketCode(couponCode);
//						oa.setActivityName("优惠券");
//						oa.setOrderCode(od.getOrderCode());
//						oa.setOutActiveCode(bConfig("xmasorder.coupon_code"));//优惠券使用虚拟活动号传至LD
//						oa.setSkuCode(od.getSkuCode());
//						oa.setPreferentialMoney((BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
//						order.getActivityList().add(oa);
//					}
//				}
//				
//				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
//					String orderCode = iterator.next();
//					
//					TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条优惠券支付信息
//					op.setMerchantId(order.getUorderInfo().getBuyerCode());
//					op.setOrderCode(orderCode);
//					op.setPaySequenceid(couponCode);
//					op.setPayedMoney(map.get(orderCode));
//					op.setPayType("449746280002");
//					order.getOcOrderPayList().add(op);
//				}
//			}
//		}else {
//			order.getUse().setCoupon_code("");
//			order.getUse().setCoupon_Money(BigDecimal.ZERO);
//		}
//		return order;
//	}
	
	/**
	 *惠家有使用优惠券处理方法 (优惠券金额均等绑定在拆分的订单商品上)
	 *均等拆分方案：按照单个商品数量金额占总额比例获取单个商品优惠金额向下取整，如优惠金额向下取整后等于0，此商品不参与优惠，下一个商品再进行均摊,
	 *到最后一个商品时优惠券总额减去之前优惠的金额再均摊到此商品单品向上取整（此处有可能存在误差）
	 *如果购买数量均一样，则单价最高的商品不参与计算(边界情况，此方法为减小误差)
	 */
	public TeslaXOrder useWgsForOrder(TeslaXOrder order) {
		BigDecimal skusCount = new BigDecimal(0.00);//可用微公社的商品总金额
		Map<String, Double> skuMonMap = new HashMap<String, Double>();//<商品编号,商品金额>
		Map<String, Double> skuNumMap = new HashMap<String, Double>();//<商品编号,商品数量>
		Map<String, Double> skuCouMon = new HashMap<String, Double>();//<商品编号,单品使用微公社额度>
		int num = 0;//随机获取单品的数量
		for (int j = 0; j < order.getOrderDetails().size(); j++) {//各参数赋值
			TeslaModelOrderDetail od = order.getOrderDetails().get(j);
			if("1".equals(od.getGiftFlag())){
				skuMonMap.put(od.getSkuCode(), od.getSkuPrice().doubleValue());
				skuNumMap.put(od.getSkuCode(),Double.valueOf(od.getSkuNum()));
				num = od.getSkuNum();
			}
		}
		if(skuNumMap!=null&&!skuNumMap.isEmpty()){
			Iterator<String> skuNumIterator = skuNumMap.keySet().iterator();
			while (skuNumIterator.hasNext()) {
				String key = (String) skuNumIterator.next();
				skusCount=skusCount.add(BigDecimal.valueOf(skuMonMap.get(key)).multiply(BigDecimal.valueOf(skuNumMap.get(key))));
			}
			BigDecimal reCouponMoney = order.getUse().getWgs_money();//优惠券金额
			BigDecimal couponMoney = reCouponMoney; 
			BigDecimal overcouponMoney = new BigDecimal(0.00);//已使用优惠券金额
			//边界情况处理逻辑:商品种类大于两种时商品数量都相等的情况，默认为true
			boolean bjqk = true;
			if(skuNumMap.size()>1){
				Iterator<String> it = skuNumMap.keySet().iterator();
				while(it.hasNext()){
					if(skuNumMap.get(it.next())!=num){
						bjqk=false;
						break;
					}
				}
			}else {
				bjqk=false;
			}
			if(reCouponMoney.doubleValue()>0&&order.getOrderDetails().size()>0){
				List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String,Double>>();
				if(bjqk){//边界情况
					Map<String, Double> skuSPMap = new HashMap<String, Double>();//<商品编号,商品金额>
					Iterator<String> ks = skuNumMap.keySet().iterator();
					while (ks.hasNext()) {
						String key = (String) ks.next();
						skuSPMap.put(key, skuMonMap.get(key));
					}
					list_Data = mapValueSortDesc(skuSPMap);//数量相等时，按金额倒序
				}else{
					//一般情况处理逻辑
					list_Data = mapValueSort(skuNumMap,skuMonMap);//一般情况按数量倒序
				}
				for (int i = 0; i < list_Data.size(); i++) {
					Map.Entry<String, Double> entry = list_Data.get(i);
					String skuCode = entry.getKey();
					Double skuNum = skuNumMap.get(skuCode);
					BigDecimal skuPrice = BigDecimal.valueOf(skuMonMap.get(skuCode));
					if(i==list_Data.size()-1){//最后一个商品特殊处理
						Double eveSkuCouMon = Math.ceil(((reCouponMoney.subtract(overcouponMoney)).divide(BigDecimal.valueOf(skuNum),2,BigDecimal.ROUND_CEILING)).doubleValue());//向上取整 单品优惠金额
						if(eveSkuCouMon.compareTo(skuPrice.doubleValue())>=0){
							eveSkuCouMon=skuPrice.doubleValue();
						}
						skuCouMon.put(skuCode, eveSkuCouMon);
					}else {//一般商品处理
						Double eveSkuCouMon = Math.floor(((skuPrice.multiply(reCouponMoney).divide(skusCount,2,BigDecimal.ROUND_DOWN))).doubleValue());//向下取整 单品优惠金额
						if(eveSkuCouMon>0){
							if(eveSkuCouMon.compareTo(skuPrice.doubleValue())>=0){
								eveSkuCouMon=skuPrice.doubleValue();
							}
							skuCouMon.put(skuCode, eveSkuCouMon);
						}
						overcouponMoney = overcouponMoney.add(BigDecimal.valueOf(eveSkuCouMon).multiply(BigDecimal.valueOf(skuNum)));
					}
				}
				Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
				for (int j = 0; j < order.getOrderDetails().size(); j++) {
					TeslaModelOrderDetail od = order.getOrderDetails().get(j);
					if("1".equals(od.getGiftFlag())&&skuCouMon.containsKey(od.getSkuCode())){
						Double eveCouMoney = skuCouMon.get(od.getSkuCode());
						order.getOrderDetails().get(j).setGroupPrice(BigDecimal.valueOf(eveCouMoney));
						order.getOrderDetails().get(j).setSkuPrice(od.getSkuPrice().subtract(BigDecimal.valueOf(eveCouMoney)));
						for (int k = 0; k < order.getSorderInfo().size(); k++) {
							if(order.getSorderInfo().get(k).getOrderCode().equals(od.getOrderCode())){
								order.getSorderInfo().get(k).setDueMoney(order.getSorderInfo().get(k).getDueMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//应付款金额-微公社金额
								order.getSorderInfo().get(k).setOrderMoney(order.getSorderInfo().get(k).getOrderMoney().subtract(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));//商品总金额-微公社金额
							}
						}
						if(map.containsKey(od.getOrderCode())){
							map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
						}else {
							map.put(od.getOrderCode(), BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum())));
						}
					}
				}
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
					String orderCode = iterator.next();
					TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条优惠券支付信息
					op.setMerchantId(order.getUorderInfo().getBuyerCode());
					op.setOrderCode(orderCode);
					op.setPayedMoney(map.get(orderCode));//均摊的钱
					op.setPayType("449746280009");
					if(couponMoney.doubleValue()>0&&couponMoney.compareTo(map.get(orderCode))>=0){//实际扣减的钱
						couponMoney=couponMoney.subtract(map.get(orderCode));
						op.setPayRemark(String.valueOf(map.get(orderCode)));
					}else if(couponMoney.doubleValue()>0&&couponMoney.compareTo(map.get(orderCode))<0) {
						op.setPayRemark(String.valueOf(couponMoney.doubleValue()));
						couponMoney=new BigDecimal(0.00);
					}else {
						op.setPayRemark("0");
					}
					order.getOcOrderPayList().add(op);
				}
			}
		}
		return order;
	}
	
	/**
	 *根据hashmap的value进行从小到大排序 
	 * 
	 */
	public List<Map.Entry<String, Double>> mapValueSortDesc(Map<String, Double> ms) {
        List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(ms.entrySet()); 
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>()  
          {   
              public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)  
              {  
               if(o2.getValue()!=null&&o1.getValue()!=null&&o2.getValue().compareTo(o1.getValue())<0){  
                return 1;  
               }else{  
                return -1;  
               }  
                  
              }  
          });
        return list_Data;
	}
	
	/**
	 *根据hashmap的value进行从大到小排序 
	 * 
	 */
	public List<Map.Entry<String, Double>> mapValueSort(Map<String, Double> ms,Map<String, Double> mm) {
        List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(ms.entrySet()); 
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>()  
          {   
              public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)  
              {  
               if(o2.getValue()!=null&&o1.getValue()!=null&&o2.getValue().compareTo(o1.getValue())>0){  
                return 1;  
               }else{  
                return -1;  
               }  
                  
              }  
          });
        Map<String, Double> map = new HashMap<String, Double>();
        if(list_Data.size()>1){
        	Double value = list_Data.get(list_Data.size()-1).getValue();
        	for (Iterator<String> iterator = ms.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				if(ms.get(key).compareTo(value)==0){
					map.put(key, mm.get(key));
				}
			}
        	if (map.size()>1) {
        		for (int i = map.size(); i > 0; i--) {
					list_Data.remove(list_Data.size()-1);
				}
        		list_Data.addAll(mapValueSortDesc(map));
			}
        }
        return list_Data;
	}
	/**
	 * 均摊储值金、暂存款金额到每个小订单上
	 * 
	 * 
	 */
	
	public TeslaXOrder userCzjZckForOrder(TeslaXOrder order){
		Double reCzj = order.getUse().getCzj_money();//剩余储值金
		Double reZck = order.getUse().getZck_money();//剩余暂存款
		for (int i = 0; i < order.getSorderInfo().size(); i++) {
			Double orderCzj = 0.00;
			Double orderZck = 0.00;
			if(i==order.getSorderInfo().size()-1){//最后一个特殊处理
				orderCzj = reCzj;
				orderZck = reZck;
			}else {
				BigDecimal percent = order.getSorderInfo().get(i).getDueMoney().divide(order.getUorderInfo().getDueMoney(),2,BigDecimal.ROUND_DOWN);
				orderCzj = Math.floor(percent.multiply(BigDecimal.valueOf(order.getUse().getCzj_money())).doubleValue());//向下取整
				orderZck = Math.floor(percent.multiply(BigDecimal.valueOf(order.getUse().getZck_money())).doubleValue());//向下取整
			}
			TeslaModelOrderPay opCzj = new TeslaModelOrderPay();//支付信息
			TeslaModelOrderPay opZck = new TeslaModelOrderPay();//支付信息
			opCzj.setMerchantId(order.getUorderInfo().getBuyerCode());
			opZck.setMerchantId(order.getUorderInfo().getBuyerCode());
			opCzj.setOrderCode(order.getSorderInfo().get(i).getOrderCode());
			opZck.setOrderCode(order.getSorderInfo().get(i).getOrderCode());
			opCzj.setPayedMoney(BigDecimal.valueOf(orderCzj));//均摊的钱
			opZck.setPayedMoney(BigDecimal.valueOf(orderZck));//均摊的钱
			opCzj.setPayType("449746280006");
			opZck.setPayType("449746280007");
			order.getOcOrderPayList().add(opCzj);
			order.getOcOrderPayList().add(opZck);
			reCzj = reCzj-orderCzj;
			reZck = reZck-orderZck;
			order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(BigDecimal.valueOf(orderCzj)).subtract(BigDecimal.valueOf(orderZck)));
			order.getSorderInfo().get(i).setDueMoney(order.getSorderInfo().get(i).getDueMoney().subtract(BigDecimal.valueOf(orderCzj)).subtract(BigDecimal.valueOf(orderZck)));
		}
		return order;
	}
	
	
	public void userCzjZckForOrder2(TeslaXOrder order) {

		List<TeslaModelOrderInfo> orderList = order.getSorderInfo();

		BigDecimal reCzj = BigDecimal.valueOf(order.getUse().getCzj_money());//剩余储值金
		BigDecimal reZck = BigDecimal.valueOf(order.getUse().getZck_money());//剩余暂存款
		BigDecimal diff=BigDecimal.ONE;
		
		MObjMap<String, TeslaModelOrderPay> payCzjMap = new MObjMap<String, TeslaModelOrderPay>();
		MObjMap<String, TeslaModelOrderPay> payZckMap = new MObjMap<String, TeslaModelOrderPay>();
		
		////代码结构写成这个，非我之所愿,愿后来者简化之
		
		while((reCzj.compareTo(diff)>=0||reZck.compareTo(diff)>=0)&&order.getUorderInfo().getDueMoney().compareTo(diff)>=0){
			
			for (TeslaModelOrderInfo orderInfo : orderList) {
				
				if(StringUtils.startsWithAny(orderInfo.getSmallSellerCode(), "SI2003","SI2009")){
					
					if(orderInfo.getDueMoney().compareTo(diff)>=0){
						
						if(reCzj.compareTo(diff)>=0){
							
							TeslaModelOrderPay opCzj = payCzjMap.get(orderInfo.getOrderCode());
							
							if(opCzj==null){
								opCzj = new TeslaModelOrderPay();
								opCzj.setMerchantId(order.getUorderInfo().getBuyerCode());
								opCzj.setOrderCode(orderInfo.getOrderCode());
								opCzj.setPayType("449746280006");
								payCzjMap.put(orderInfo.getOrderCode(), opCzj);
							}
							
							opCzj.setPayedMoney(opCzj.getPayedMoney().add(diff));
							reCzj=reCzj.subtract(diff);
							
							
						}else if(reZck.compareTo(diff)>=0) {
							
							TeslaModelOrderPay opZck = payZckMap.get(orderInfo.getOrderCode());
							
							if(opZck==null){
								opZck=new TeslaModelOrderPay();
								opZck.setMerchantId(order.getUorderInfo().getBuyerCode());
								opZck.setOrderCode(orderInfo.getOrderCode());
								opZck.setPayType("449746280007");
								payZckMap.put(orderInfo.getOrderCode(), opZck);
							}
							
							opZck.setPayedMoney(opZck.getPayedMoney().add(diff));//均摊的钱
							reZck=reZck.subtract(diff);
							
						}
						
						orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(diff));
						order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(diff));
						
					}
				}
			}
		}
	}
	
	
	/**
	 * 扣取储值金、暂存款
	 * @param order
	 */
	public void userCzjZckForOrder3(TeslaXOrder order) {
		
		BigDecimal reCzj = BigDecimal.valueOf(order.getUse().getCzj_money());//剩余储值金
		BigDecimal reZck = BigDecimal.valueOf(order.getUse().getZck_money());//剩余暂存款
		BigDecimal killMoney=reCzj.add(reZck);
		BigDecimal dueMoneyLD=BigDecimal.ZERO;
		
		List<TeslaModelOrderInfo> orderForLdList=new ArrayList<TeslaModelOrderInfo>(); 
		for (TeslaModelOrderInfo orderInfo : order.getSorderInfo()) {
			if(StringUtils.startsWithAny(orderInfo.getSmallSellerCode(), "SI2003","SI2009")&&orderInfo.getDueMoney().compareTo(BigDecimal.ZERO)>0){
				orderForLdList.add(orderInfo);
				dueMoneyLD=orderInfo.getDueMoney().add(dueMoneyLD);
			}
		}
		
		if(dueMoneyLD.compareTo(killMoney)<=0){
			
			for (TeslaModelOrderInfo orderInfo : orderForLdList) {
				
				if(orderInfo.getDueMoney().compareTo(reCzj)<=0){
					createPayInfo(order, orderInfo.getOrderCode(), orderInfo.getDueMoney(), "449746280006");
					reCzj=reCzj.subtract(orderInfo.getDueMoney());
				}else if(orderInfo.getDueMoney().compareTo(reZck)<=0){
					createPayInfo(order, orderInfo.getOrderCode(), orderInfo.getDueMoney(), "449746280007");
					reZck=reZck.subtract(orderInfo.getDueMoney());
				}else{
					createPayInfo(order, orderInfo.getOrderCode(), reCzj, "449746280006");
					createPayInfo(order, orderInfo.getOrderCode(), reZck, "449746280007");
				}
				
				orderInfo.setDueMoney(BigDecimal.ZERO);
				order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(orderInfo.getDueMoney()));
				
			}
			
		}else{
			
			int a=String.valueOf(MoneyHelper.roundHalfUp(killMoney)).length();
			
			for (TeslaModelOrderInfo orderInfo : orderForLdList) {
				
				if(reCzj.add(reZck).compareTo(BigDecimal.ONE)<0){
					break;
				}
				
				int b=String.valueOf(MoneyHelper.roundHalfUp(orderInfo.getDueMoney())).length();
				a=a>b?b:a;
				
				BigDecimal due=BigDecimal.valueOf(Math.floor((orderInfo.getDueMoney().multiply(killMoney).divide(dueMoneyLD,a,BigDecimal.ROUND_DOWN)).doubleValue()));
				
				if(due.compareTo(orderInfo.getDueMoney())>0){
					due=orderInfo.getDueMoney();
				}
				
				if(due.compareTo(BigDecimal.ZERO)<=0){
					due=BigDecimal.ONE;
				}
				
				if(due.compareTo(reCzj)<=0){
					createPayInfo(order, orderInfo.getOrderCode(), due, "449746280006");
					reCzj=reCzj.subtract(due);
				}else if(due.compareTo(reZck)<=0){
					createPayInfo(order, orderInfo.getOrderCode(), due, "449746280007");
					reZck=reZck.subtract(due);
				}else{
					
					if(reZck.compareTo(BigDecimal.ONE)>=0){
						createPayInfo(order, orderInfo.getOrderCode(), reZck, "449746280007");
						
					}
					
					if(due.subtract(reZck).compareTo(reCzj)<=0){
						createPayInfo(order, orderInfo.getOrderCode(), due.subtract(reZck), "449746280006");
						reCzj=reCzj.subtract(due.subtract(reZck));
					}else{
						createPayInfo(order, orderInfo.getOrderCode(), reCzj, "449746280006");
						reCzj=BigDecimal.ZERO;
					}
					reZck=BigDecimal.ZERO;
				}
				
				orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(due));
				order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(due));
				
			}
			
			userCzjZckForOrder4(order, reCzj, orderForLdList,"449746280006");
			userCzjZckForOrder4(order, reZck, orderForLdList,"449746280007");
		}
		
	}
	
	//临时解决方案
	private void userCzjZckForOrder4(TeslaXOrder order,BigDecimal reZck,List<TeslaModelOrderInfo> orderForLdList,String payType) {

		BigDecimal diff=BigDecimal.ONE;
		
		MObjMap<String, TeslaModelOrderPay> payZckMap = new MObjMap<String, TeslaModelOrderPay>();
		
		////代码结构写成这个，非我之所愿,望后来者简化之
		
		while(reZck.compareTo(diff)>=0){
			
			for (TeslaModelOrderInfo orderInfo : orderForLdList) {
				
				if(orderInfo.getDueMoney().compareTo(diff)>=0){
					
					if(reZck.compareTo(diff)>=0) {
						
						TeslaModelOrderPay opZck = payZckMap.get(orderInfo.getOrderCode());
						
						if(opZck==null){
							opZck=new TeslaModelOrderPay();
							opZck.setMerchantId(order.getUorderInfo().getBuyerCode());
							opZck.setOrderCode(orderInfo.getOrderCode());
							opZck.setPayType(payType);
							payZckMap.put(orderInfo.getOrderCode(), opZck);
						}
						
						opZck.setPayedMoney(opZck.getPayedMoney().add(diff));//均摊的钱
						reZck=reZck.subtract(diff);
						
						
						
						orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(diff));
						order.getUorderInfo().setDueMoney(order.getUorderInfo().getDueMoney().subtract(diff));
					}
					
				}
			}
		}
		
		
		MObjMap<String, TeslaModelOrderPay> pczkMap = new MObjMap<String, TeslaModelOrderPay>();
		for (TeslaModelOrderPay payInfo : order.getOcOrderPayList()) {
			pczkMap.put(payInfo.getOrderCode()+"-"+payInfo.getPayType(), payInfo);
		}
		
		for (MObjMap.Entry<String, TeslaModelOrderPay> mm : payZckMap.entrySet()) {
			
			String orderc=mm.getKey();
			TeslaModelOrderPay pay=mm.getValue();
			TeslaModelOrderPay payInfo=pczkMap.get(orderc+"-"+payType);
			if(payInfo!=null){
				payInfo.setPayedMoney(payInfo.getPayedMoney().add(pay.getPayedMoney()));
			}else{
				createPayInfo(order, orderc, pay.getPayedMoney(), payType);
			}
			
		}
	}
	
	/**
	 * 使用积分
	 * @param order
	 */
	public TeslaXResult userIntegralForOrder(TeslaXOrder order, GetCustAmtResult custAmt) {
		TeslaXResult result = new TeslaXResult();
		
		List<TeslaModelOrderInfo> orderInfoList = order.getSorderInfo();
		Map<String,BigDecimal> dueMoneyMap = new HashMap<String, BigDecimal>();
		// 总订单金额（不含运费）
		BigDecimal totalMoney = new BigDecimal(0);
		
		// 总使用积分
		int integral = order.getUse().getIntegral();
		
		// 只能使用固定值的整倍数
		if(integral % accm.getRate() != 0){
			result.inErrorMessage(963906050, accm.getRate());
			return result;
		}
		
		// 先查询出客代号
//		String custId = accm.getCustId(order.getUorderInfo().getBuyerCode());
//		GetCustAmtResult custAmt = null;
//		if(StringUtils.isNotBlank(custId)){
//			custAmt = accm.getPlusModelCustAmt(custId);
//		}
		
		// 如果使用了积分但是未查询到积分数据
//		if(custAmt == null){
//			if(order.getUse().getIntegral() > 0){
//				result.inErrorMessage(963906053);
//			}
//			return result;
//		}
		
		// 计算订单应付的金额
		for(TeslaModelOrderInfo orderInfo : orderInfoList){
			// 积分不抵扣运费，所以如果应付款剩余金额比运费小则此单不能使用积分
			if(orderInfo.getDueMoney().compareTo(orderInfo.getTransportMoney()) <= 0){
				continue;
			}
			
			dueMoneyMap.put(orderInfo.getOrderCode(), orderInfo.getDueMoney().subtract(orderInfo.getTransportMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
			totalMoney = totalMoney.add(dueMoneyMap.get(orderInfo.getOrderCode()));
		}
		
		// 设置总积分和最大使用积分
//		order.setCustId(custId);
		order.setAllIntegral(accm.moneyToAccmAmt(custAmt.getPossAccmAmt(),1));
		
		// 取剩余积分和商品金额的最小的一个
		if(custAmt.getPossAccmAmt().compareTo(totalMoney) > 0){
			order.setMaxUseIntegral(accm.moneyToAccmAmt(totalMoney.setScale(0, BigDecimal.ROUND_FLOOR),0));
		}else{
			order.setMaxUseIntegral(accm.moneyToAccmAmt(custAmt.getPossAccmAmt().setScale(0, BigDecimal.ROUND_FLOOR),0));
		}
		
		// 可用最大积分不能是负的
		if(order.getMaxUseIntegral().compareTo(BigDecimal.ZERO) < 0) order.setMaxUseIntegral(BigDecimal.ZERO);
		
		// 使用积分抵扣金额
		BigDecimal useIntegralMoney = accm.accmAmtToMoney(new BigDecimal(integral),2);
		
		// 使用了积分的情况下，订单确认页默认使用全部积分
		if(order.getStatus().getExecStep() == ETeslaExec.Confirm && integral > 0){
			order.getUse().setIntegral(order.getMaxUseIntegral().intValue());
			useIntegralMoney = accm.accmAmtToMoney(new BigDecimal(order.getUse().getIntegral()),2);
		}
		
		// 如果使用了积分则需要校验一下是否超过商品总金额
		if(useIntegralMoney.compareTo(BigDecimal.ZERO) > 0 && totalMoney.compareTo(useIntegralMoney) < 0 && !order.isPointShop()){
			result.setResultCode(963906056);
			result.setResultMessage(bInfo(963906056, accm.moneyToAccmAmt(totalMoney,0).intValue()));
			return result;
		}
		
		// 使用的积分不能超过总积分数
		if(order.getUse().getIntegral() > 0 && order.getUse().getIntegral() > order.getAllIntegral().intValue() && !order.isPointShop()){
			result.inErrorMessage(963906054);
			return result;
		}
		
		// 拆分积分
		if(useIntegralMoney.compareTo(BigDecimal.ZERO) > 0){
			List<TeslaModelOrderInfo> sortOrderInfoList = new ArrayList<TeslaModelOrderInfo>(orderInfoList);
			// 根据金额倒序排列
			Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
				@Override
				public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
					return o2.getDueMoney().subtract(o2.getTransportMoney()).compareTo(o1.getDueMoney().subtract(o1.getTransportMoney()));
				}
			});
			
			// LD订单优先
			Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
				@Override
				public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
					if(o1.getSmallSellerCode().equals("SI2003") && !o2.getSmallSellerCode().equals("SI2003")){
						return -1;
					}
					if(!o1.getSmallSellerCode().equals("SI2003") && o2.getSmallSellerCode().equals("SI2003")){
						return 1;
					}
					return 0;
				}
			});
			
			// 使用的积分和总商品金额的占比
			BigDecimal dueMoney = null;
			// 分到的金额
			BigDecimal useMoney = null;
			// 累计拆分的金额
			BigDecimal allUseMonty = new BigDecimal(0);
			
			for(TeslaModelOrderInfo orderInfo : sortOrderInfoList){
				//当积分商城时，不需要拆商品
				if(!order.isPointShop()) {
					dueMoney = dueMoneyMap.get(orderInfo.getOrderCode());
					
					// 商品金额为0的不参与积分拆分
					if(dueMoney == null || dueMoney.compareTo(BigDecimal.ZERO) <= 0) {
						continue; 
					}
					
					// 按比例拆分金额 ： 商品金额 / 总金额 * 使用积分金额
					useMoney = dueMoney.divide(totalMoney,4,BigDecimal.ROUND_HALF_UP).multiply(useIntegralMoney).setScale(0, BigDecimal.ROUND_UP);
					if(useMoney.compareTo(dueMoney) > 0){
						useMoney = dueMoney.setScale(0, BigDecimal.ROUND_DOWN);  // 最终分到订单上面的积分金额保持为整数
					}
					
					// 不能超过使用的积分金额
					if(allUseMonty.add(useMoney).compareTo(useIntegralMoney) > 0){
						useMoney = useIntegralMoney.subtract(allUseMonty);
					}
					
					// 记录拆分到每单上面的积分
					if(useMoney.compareTo(BigDecimal.ZERO) > 0){
						allUseMonty = allUseMonty.add(useMoney);
						createPayInfo(order, orderInfo.getOrderCode(), useMoney, "449746280008", order.getCustId());
						
						orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(useMoney)); 
						// 拆分到商品
						splitIntegralMoneyForSku(order, orderInfo.getOrderCode(), useMoney);
					}
				}else {
					createPayInfo(order, orderInfo.getOrderCode(), new BigDecimal(order.getUse().getIntegral() / 200), "449746280008", order.getCustId());
				}
			}
			
			// 拆单后修正一下最终可用的积分数
			order.setMaxUseIntegral(accm.moneyToAccmAmt(allUseMonty,0));
			//积分商城重置积分
			if(!order.isPointShop()) {
				order.getUse().setIntegral(order.getMaxUseIntegral().intValue());
			}
		}
		
		return result;
	}
	
	/**
	 * 使用惠币
	 * @param order
	 */
	public TeslaXResult useHjycoinForOrder(TeslaXOrder order, GetCustAmtResult custAmt) {
		TeslaXResult result = new TeslaXResult();
		
		List<TeslaModelOrderInfo> orderInfoList = order.getSorderInfo();
		Map<String,BigDecimal> dueMoneyMap = new HashMap<String, BigDecimal>();
		// 总订单金额（不含运费）
		BigDecimal totalDueMoney = new BigDecimal(0);
		
		// 计算订单应付的金额
		for(TeslaModelOrderInfo orderInfo : orderInfoList){
			// 不抵扣运费，所以如果应付款剩余金额比运费小则此单不能使用积分
			if(orderInfo.getDueMoney().compareTo(orderInfo.getTransportMoney()) <= 0){
				continue;
			}
			
			dueMoneyMap.put(orderInfo.getOrderCode(), orderInfo.getDueMoney().subtract(orderInfo.getTransportMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
			totalDueMoney = totalDueMoney.add(dueMoneyMap.get(orderInfo.getOrderCode()));
		}
		
		order.setAllHjycoin(custAmt.getPossHcoinAmt());
		
		// 取剩余惠币和商品金额的最小的一个，惠币金额只能整数倍使用所以取整
		if(custAmt.getPossHcoinAmt().compareTo(totalDueMoney) > 0){
			order.setMaxUseHjycoin(totalDueMoney.setScale(0, BigDecimal.ROUND_DOWN));
		}else{
			order.setMaxUseHjycoin(custAmt.getPossHcoinAmt().setScale(0, BigDecimal.ROUND_DOWN));
		}
		
		// 可用最大惠币不能是负的
		if(order.getMaxUseHjycoin().compareTo(BigDecimal.ZERO) < 0) order.setMaxUseHjycoin(BigDecimal.ZERO);
		
		// 前端传入的使用惠币金额，取整
		BigDecimal useHjycoinMoney = new BigDecimal(order.getUse().getHjycoin()+"").setScale(0, BigDecimal.ROUND_DOWN);
		
		// 修正使用的惠币金额，可能的负值的情况
		if(useHjycoinMoney.compareTo(BigDecimal.ZERO) < 0) {
			useHjycoinMoney = BigDecimal.ZERO;
			order.getUse().setHjycoin(0);
		}
		
		// 修正使用的惠币金额，使用了惠币的情况下，订单确认页默认使用全部惠币，覆盖前端传入的使用金额
		if(order.getStatus().getExecStep() == ETeslaExec.Confirm && order.getUse().getHjycoin() > 0){
			order.getUse().setHjycoin(order.getMaxUseHjycoin().doubleValue());
			useHjycoinMoney = order.getMaxUseHjycoin();
		}
		
		// 拆分惠币
		if(useHjycoinMoney.compareTo(BigDecimal.ZERO) > 0){
			List<TeslaModelOrderInfo> sortOrderInfoList = new ArrayList<TeslaModelOrderInfo>(orderInfoList);
			// 根据金额倒序排列
			Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
				@Override
				public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
					return o2.getDueMoney().subtract(o2.getTransportMoney()).compareTo(o1.getDueMoney().subtract(o1.getTransportMoney()));
				}
			});
			
			// LD订单优先
			Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
				@Override
				public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
					if(o1.getSmallSellerCode().equals("SI2003") && !o2.getSmallSellerCode().equals("SI2003")){
						return -1;
					}
					if(!o1.getSmallSellerCode().equals("SI2003") && o2.getSmallSellerCode().equals("SI2003")){
						return 1;
					}
					return 0;
				}
			});
			
			// 累计拆分的金额
			BigDecimal allUseMonty = new BigDecimal(0);
			for(TeslaModelOrderInfo orderInfo : sortOrderInfoList){
				// 单个订单的累计使用惠币金额
				BigDecimal orderUseMonty = new BigDecimal(0);
				// 提取订单明细
				for(TeslaModelOrderDetail detail : order.getOrderDetails()){
					// 排除赠品
					if(!"1".equals(detail.getGiftFlag())){
						continue;
					}
					
					// 忽略非当前订单的商品
					if(!detail.getOrderCode().equals(orderInfo.getOrderCode())) {
						continue;
					}
					
					// 商品金额  = (商品单价 x 商品数量 ) - 积分
					BigDecimal skuMoney = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())).subtract(detail.getIntegralMoney());
					// 拆分的金额 = 商品金额  / 总商品金额  x 总使用惠币金额
					BigDecimal skuHjycoinMoney = skuMoney.divide(totalDueMoney, 4, BigDecimal.ROUND_HALF_UP)
							.multiply(useHjycoinMoney)
							.setScale(0, BigDecimal.ROUND_UP);
					
					// 不能超过总使用的惠币金额
					if(allUseMonty.add(skuHjycoinMoney).compareTo(useHjycoinMoney) > 0){
						skuHjycoinMoney = useHjycoinMoney.subtract(allUseMonty);
					}
					
					// 不能超过商品的金额
					if(skuHjycoinMoney.compareTo(skuMoney) > 0){
						skuHjycoinMoney = skuMoney;
					}
					
					// 记录商品使用的惠币金额
					detail.setHjycoin(skuHjycoinMoney);
					
					// 累加已经拆分到商品的积分金额
					allUseMonty = allUseMonty.add(skuHjycoinMoney);
					orderUseMonty = orderUseMonty.add(skuHjycoinMoney);
				}
				
				if(orderUseMonty.compareTo(BigDecimal.ZERO) > 0) {
					orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(orderUseMonty)); 
					createPayInfo(order, orderInfo.getOrderCode(), orderUseMonty, "449746280025", order.getCustId());
				}
			}
			
			// 拆单后修正一下最终可用的积分数
			order.setMaxUseHjycoin(allUseMonty);
			order.getUse().setHjycoin(order.getMaxUseHjycoin().intValue());
		}
		
		return result;
	}
	
	/**
	 * 
	 * 把订单上面的积分拆分到SKU上面
	 * @param orderInfo  订单信息
	 * @param detailList 商品明细
	 * @param useMoney   总积分
	 */
	public void splitIntegralMoneyForSku(TeslaXOrder order, String orderCode, BigDecimal useMoney) {
		// 订单明细按订单分组
		List<TeslaModelOrderDetail> detailList = new ArrayList<TeslaModelOrderDetail>();
		// 总商品金额
		BigDecimal productMoney = BigDecimal.ZERO;
		
		// 提取订单明细
		for(TeslaModelOrderDetail detail : order.getOrderDetails()){
			// 排除赠品
			if(!"1".equals(detail.getGiftFlag())){
				continue;
			}
			if(!orderCode.equals(detail.getOrderCode())){
				continue;
			}
			
			BigDecimal skuMoney = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum()));
			
			detailList.add(detail);
			productMoney = productMoney.add(skuMoney);
		}
		
		BigDecimal skuUseMonty = BigDecimal.ZERO;  // 累计拆分到商品上面的积分金额
		for(TeslaModelOrderDetail detail : detailList){
			if(skuUseMonty.compareTo(useMoney) >= 0){
				break;
			}
			
			// 拆分的金额 = 商品单价 x 商品数量  / 总商品金额  x 整单积分金额
			BigDecimal m = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum()))
					.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP)
					.multiply(useMoney)
					.setScale(0, BigDecimal.ROUND_UP);
			
			// 不能超过订单积分金额
			if(skuUseMonty.add(m).compareTo(useMoney) > 0){
				m = useMoney.subtract(skuUseMonty);
			}
			
			// 不能超过商品的金额
			if(m.compareTo(detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum()))) > 0){
				m = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum()));
			}
			
			// 累加已经拆分到商品的积分金额
			skuUseMonty = skuUseMonty.add(m);
			detail.setIntegralMoney(m);
		}
	}
	
	private void createPayInfo(TeslaXOrder order,String order_code,BigDecimal dueMoney,String payType){
		createPayInfo(order, order_code, dueMoney, payType, "");
	}
	
	private void createPayInfo(TeslaXOrder order,String order_code,BigDecimal dueMoney,String payType, String payRemark){
		TeslaModelOrderPay opZck=new TeslaModelOrderPay();
		opZck.setMerchantId(order.getUorderInfo().getBuyerCode());
		opZck.setOrderCode(order_code);
		opZck.setPayType(payType);
		opZck.setPayedMoney(dueMoney);
		opZck.setPayRemark(payRemark);
		order.getOcOrderPayList().add(opZck);
	}
	
	private String getCustId(String memberCode){
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		return levelInfo.getCustId();
	}
	
	/**
	 * 查询客代号可用积分、暂存款、储值金
	 * @param custId
	 * @return null 查询失败
	 */
	private GetCustAmtResult getPlusModelCustAmt(String custId){
		CustRelAmtRef beanRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		return beanRef.getCustAmt(custId);
	}
}
