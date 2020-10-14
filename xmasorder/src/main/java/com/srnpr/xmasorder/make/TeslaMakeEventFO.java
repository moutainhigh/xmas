package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.AverageMoneyModel;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.service.AverageMoneyService;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelProObject;
import com.srnpr.xmassystem.modelevent.PlusModelSaleProObject;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.service.PlusServiceSale;
import com.srnpr.xmassystem.support.PlusSupportEvent;
/**
 * 促销计算(获取商品的最新价格)
 * 
 * @author xiegj
 * 
 * modifyed: zhangbo  version :5.4.2
 *
 */
public class TeslaMakeEventFO extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		//多彩下单商品不参加促销活动
		//积分商城不参与促销活动
		//兑换码兑换不参与促销活动
		//惠惠农场兑换不参与促销活动
		if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource()) 
				|| "449715200024".equals(teslaOrder.getUorderInfo().getOrderType())
				|| StringUtils.isNotEmpty(teslaOrder.getRedeemCode())
				|| StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
			return result;
		}
		
		// 互动活动不跟其他促销叠加
		if(teslaOrder.getHuDongEvent() != null) {
			return result;
		}
		
		PlusModelSaleProObject pmo = new PlusModelSaleProObject();//满减查询参数
		pmo.setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
		
		PlusModelEventFull eventFullInfo = null;
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
			TeslaModelShowGoods teslaModelShowGoods = teslaOrder.getShowGoods().get(i);
			//是赠品&加价购品都不参加满减
			if("0".equals(teslaModelShowGoods.getGiftFlag())
					||"1".equals(teslaModelShowGoods.getIfJJGFlag())
					// 分销商品不参与满减
					|| teslaModelShowGoods.getFxFlag() == 1) {
				continue;
			}
			
			//购物车流程,非上架商品不参加满减计算
			if(teslaOrder.getStatus().getExecStep()==ETeslaExec.shopCart
					&&!"4497153900060002".equals(teslaModelShowGoods.getProductStatus())) {
				teslaModelShowGoods.setChoose_flag("0");
				continue;
			}
			
			PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(teslaModelShowGoods.getProductCode());
			PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
			PlusModelProObject ppo = new PlusModelProObject();
			
			String eventType = teslaModelShowGoods.getEventType();
			eventFullInfo = new PlusSupportEvent().upEventSalueByMangeCodeAndProductCode(teslaOrder.getUorderInfo().getSellerCode(),teslaModelShowGoods.getProductCode(),eventType);
			if(!teslaModelShowGoods.isIs_activity()) {
				// 当前商品参与其他促销活动,取sku原销售价
				ppo.setOrig_sku_price(teslaModelShowGoods.getOrig_sku_price());
				ppo.setSku_price(teslaModelShowGoods.getOrig_sku_price());
			}
			//5.4.2前的逻辑  
			/*else if( (   "4497472600010002".equals(eventType) || "4497472600010004".equals(eventType) || "4497472600010015".equals(eventType)  ) && eventFullInfo != null  ) {
								
				 * 活动支持   特价-满减 或  扫码购-满减 活动并存， (此满减为 特殊满减  ，满减类型为：每满X减Y-LD多重促销活动_449747630008)
				 * 4497472600010002 : 特价
				 * 4497472600010004 : 扫码购-微信
				 * 4497472600010015 : 扫码购-APP
				 * eventFullInfo != null 说明此商品参与了特殊满减
				 
//				String fullCutCalType = eventFullInfo.getFullCutCalType();
				ppo.setOrig_sku_price(teslaModelShowGoods.getSkuPrice());
				ppo.setSku_price(teslaModelShowGoods.getSkuPrice());
			}*/
			
			//5.4.2后的新规则  ：满减活动绑定的可叠加活动类型，都可以进行满减的叠加使用 
			else if(eventFullInfo!=null) {
				ppo.setOrig_sku_price(teslaModelShowGoods.getSkuPrice());
				ppo.setSku_price(teslaModelShowGoods.getSkuPrice());
			}
			
			else {
				continue;
			}
			ppo.setSkuCode(teslaModelShowGoods.getSkuCode());
			ppo.setSkuNum(teslaModelShowGoods.getSkuNum());
			ppo.setBrandCode(plusModelProductinfo.getBrandCode());
			ppo.setCategoryCodes(plusModelProductinfo.getCategorys());
//			ppo.setOrig_sku_price(teslaOrder.getShowGoods().get(i).getOrig_sku_price());
//			ppo.setSku_price(teslaModelShowGoods.getOrig_sku_price());
			ppo.setProductCode(teslaModelShowGoods.getProductCode());
			ppo.setChoose_flag(teslaModelShowGoods.getChoose_flag());
			ppo.setSmallSellerCode(teslaModelShowGoods.getSmallSellerCode());
			ppo.setEventCode(teslaModelShowGoods.getEventCode());
			pmo.getSaleObject().add(ppo);
		}
		Map<String, List<PlusModelProObject>> pmos = new HashMap<String, List<PlusModelProObject>>();
		pmo = new PlusServiceSale().getEventSale(pmo, teslaOrder.getUorderInfo().getSellerCode(), teslaOrder.getChannelId());
		for (int i = 0; i < pmo.getSaleObject().size(); i++) {
			if(pmo.getSaleObject().get(i).isEventTrue()&&StringUtils.isNotBlank(pmo.getSaleObject().get(i).getEventCode())){
				if(pmos.containsKey(pmo.getSaleObject().get(i).getEventCode())){
					pmos.get(pmo.getSaleObject().get(i).getEventCode()).add(pmo.getSaleObject().get(i));
				}else {
					List<PlusModelProObject> ll = new ArrayList<PlusModelProObject>();
					ll.add(pmo.getSaleObject().get(i));
					pmos.put(pmo.getSaleObject().get(i).getEventCode(), ll);
				}
			}
		}
		Iterator<String> iterator = pmos.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();

			if("449747630006".equals(pmos.get(key).get(0).getFullType())){
				// 第X件Y折的满减需要按商品编号分组单独计算
				averageActivityMoneyForManZhe(pmo, pmos.get(key), teslaOrder);
			}else{
				// 其他满减的促销金额
				averageActivityMoneyNew(pmo, pmos.get(key), teslaOrder);
			}
		}
		if(pmo!=null&&pmo.getSaleObject()!=null&&!pmo.getSaleObject().isEmpty()){
			for (int i = 0; i < pmo.getSaleObject().size(); i++) {
				PlusModelProObject pb = pmo.getSaleObject().get(i);
				for (int j = 0; j < teslaOrder.getShowGoods().size(); j++) {
					if(!pb.getSkuCode().equals(teslaOrder.getShowGoods().get(j).getSkuCode())||"1".equals(teslaOrder.getShowGoods().get(j).getIfJJGFlag())){continue;}
					teslaOrder.getShowGoods().get(j).setSkuPrice(pb.getOrig_sku_price());
					teslaOrder.getShowGoods().get(j).setIs_activity(pb.isEventTrue());
					teslaOrder.getShowGoods().get(j).setSkuCode(pb.getSkuCode());
					teslaOrder.getShowGoods().get(j).setFullMoneys(pb.getFullMoneys());
					//此处会覆盖掉原有活动类型活动编号  却不覆盖IC编号 会导致打折促销扣减活动库存失败时 无法判断 临时解决方案 如果原有活动类型是打折促销 则此处不处理
					if(!"4497472600010030".equals(teslaOrder.getShowGoods().get(j).getEventType())) {
						teslaOrder.getShowGoods().get(j).setActivity_name(pb.getEventTypeTame());
						teslaOrder.getShowGoods().get(j).setEventType(pb.getEventType());
						teslaOrder.getShowGoods().get(j).setEventCode(pb.getEventCode());
						teslaOrder.getShowGoods().get(j).setItem_code(pb.getItem_code());
					}
					teslaOrder.getShowGoods().get(j).setStart_time(pb.getBeginTime());
					teslaOrder.getShowGoods().get(j).setEnd_time(pb.getEndTime());
					teslaOrder.getShowGoods().get(j).setActivityUrl(pb.getActivityUrl());
					teslaOrder.getShowGoods().get(j).setFullType(pb.getFullType());
					for (int m = 0; m < teslaOrder.getOrderDetails().size(); m++) {//orderdetail内的商品价格及saveAmt
						TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(m);
						if(detail.getSkuCode().equals(pb.getSkuCode())&&"0".equals(detail.getIfJJGFlag())){
							teslaOrder.getOrderDetails().get(m).setSaveAmt(pb.getOrig_sku_price().subtract(pb.getSku_price()));
							teslaOrder.getOrderDetails().get(m).setSkuPrice(pb.getSku_price());
							teslaOrder.getOrderDetails().get(m).setSkuCode(pb.getSkuCode());
							teslaOrder.getOrderDetails().get(m).setShowPrice(pb.getOrig_sku_price());
						}
					}
					if(!pb.isEventTrue()){continue;}
					TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
					activity.setOrderCode(teslaOrder.getShowGoods().get(j).getOrderCode());
					activity.setActivityCode(pb.getEventCode());
					activity.setActivityName(pb.getEventTypeTame());
					activity.setActivityType(pb.getEventType());
					activity.setPreferentialMoney(pb.getOrig_sku_price().subtract(pb.getSku_price()));
					activity.setProductCode(pb.getProductCode());
					activity.setSkuCode(pb.getSkuCode());
					activity.setOutActiveCode(pb.getFullMoneys().getAdvTitle());//用此字段存sku的活动商品IC编号
					teslaOrder.getActivityList().add(activity);
				}
			}
		}
		if(teslaOrder.getUse().getFull_money().compareTo(BigDecimal.ZERO)>0){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmasorder.fullMoney_show"));
			discount.setDis_type("0");
			discount.setDis_price(teslaOrder.getUse().getFull_money().doubleValue());
			teslaOrder.getShowMoney().add(discount);
		}
		return result;
	}
	
	/**
	 * 类似满减金额的均摊，会出现因均摊金额误差的人品奖励情况。 
	 * 
	 * @author fq  重写
	 * @param pmo
	 * @param list
	 * @param teslaOrder
	 * @return
	 */
	private PlusModelSaleProObject averageActivityMoneyNew(PlusModelSaleProObject pmo,List<PlusModelProObject> list,TeslaXOrder teslaOrder){
		
		BigDecimal initCutMoney = BigDecimal.ZERO;//记录初始优惠金额  
		Map<String, Integer> skuNumMap = new HashMap<String, Integer>();//<商品编号,商品数量>
		Map<String, BigDecimal> skuMonMap = new HashMap<String, BigDecimal>();//<商品编号,商品金额>
		Map<String, String> skuSmallSellerCodeMap = new HashMap<String, String>();//<商品编号,商户编号>
		
		for (int j = 0; j < list.size(); j++) {//各参数赋值
			PlusModelProObject od = list.get(j);
			
			if(initCutMoney.compareTo(BigDecimal.ZERO) == 0) {
				initCutMoney = od.getFullMoneys().getCutMoney();//由于list里的满减金额时一样的，因此取一次即可
			}
			
			skuMonMap.put(od.getSkuCode(), od.getOrig_sku_price());
			skuNumMap.put(od.getSkuCode(),od.getSkuNum());
			skuSmallSellerCodeMap.put(od.getSkuCode(), od.getSmallSellerCode());
		}
		
		if(skuNumMap!=null&&!skuNumMap.isEmpty()){
			Iterator<String> skuNumIterator = skuNumMap.keySet().iterator();
			
			List<AverageMoneyModel> averageModel = new ArrayList<AverageMoneyModel>();
			while (skuNumIterator.hasNext()) {
				String key = (String) skuNumIterator.next();
				
				AverageMoneyModel averageMoneyModel = new AverageMoneyModel();
				averageMoneyModel.setSkuCode(key);
				averageMoneyModel.setSkuNum(skuNumMap.get(key));
				averageMoneyModel.setSkuPrice(skuMonMap.get(key).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				averageMoneyModel.setSmallSellerCode(skuSmallSellerCodeMap.get(key));
				averageModel.add(averageMoneyModel);
				
			}
			
			if(averageModel.size() > 0) {
				//平摊优惠金额，返回值map（key：sku编号，value：优惠的金额）
				Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, initCutMoney);
				BigDecimal allCutMoney = BigDecimal.ZERO;//记录总优惠的金额，（为后续计算人品奖励用）
				Iterator<String> it = averageMoney.keySet().iterator();
				while (it.hasNext()) {
					String skuCode = it.next();
					for (int i = 0; i < pmo.getSaleObject().size(); i++) {
						if(pmo.getSaleObject().get(i).getSkuCode().equals(skuCode) && averageMoney.containsKey(skuCode)){
							pmo.getSaleObject().get(i).setSku_price(pmo.getSaleObject().get(i).getOrig_sku_price()
									.subtract(averageMoney.get(skuCode)).setScale(2, BigDecimal.ROUND_HALF_UP));
							allCutMoney=allCutMoney.add(averageMoney.get(skuCode).multiply(BigDecimal.valueOf(skuNumMap.get(skuCode))));
						}
					}
				}
				
				Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
				for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
					TeslaModelOrderDetail od = teslaOrder.getOrderDetails().get(j);
					if(!averageMoney.containsKey(od.getSkuCode())||"1".equals(od.getIfJJGFlag())) {
						continue;
					}
					Double eveCouMoney = averageMoney.get(od.getSkuCode()).doubleValue();
					if(map.containsKey(od.getOrderCode())&&eveCouMoney!=null){
						map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(new BigDecimal(eveCouMoney).multiply(new BigDecimal(od.getSkuNum()))));
					}else if(eveCouMoney!=null){
						map.put(od.getOrderCode(), new BigDecimal(eveCouMoney).multiply(new BigDecimal(od.getSkuNum())));
					}
				}
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
					String orderCode = iterator.next();
					
					TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条优惠券支付信息
					op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
					op.setOrderCode(orderCode);
					op.setPayedMoney(map.get(orderCode));
					op.setPayType("449746280012");
					teslaOrder.getOcOrderPayList().add(op);
				}
				if(allCutMoney.compareTo(BigDecimal.ZERO)>0){
					teslaOrder.getUse().setFull_money(teslaOrder.getUse().getFull_money().add(allCutMoney.compareTo(initCutMoney)>0?initCutMoney:allCutMoney));
				}
				teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(allCutMoney));
				//计算人品奖励
				BigDecimal rpjl = allCutMoney.subtract(initCutMoney);
				teslaOrder.getUse().setMoral_character_money(teslaOrder.getUse().getMoral_character_money().add(rpjl));
				if(rpjl.compareTo(BigDecimal.ZERO)>0) {
					for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
						if(teslaOrder.getOrderDetails().get(j).getSkuCode().equals(averageMoney.keySet().toArray()[0])){
							TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
							activity.setOrderCode(teslaOrder.getOrderDetails().get(j).getOrderCode());
							activity.setActivityName("人品奖励(满减引起)");
							activity.setActivityType("4497472600010009");
							activity.setPreferentialMoney(rpjl);
							teslaOrder.getActivityList().add(activity);
							break;
						}
					}
				}
				
			}
		}
		
		return pmo;
	
	}
	
	
	/**
	 *类似满减金额的均摊，会出现因均摊金额误差的人品奖励情况。 (一切都是为了LD订单不能写小数，大坑，不要问为什么，我就这样一直被坑着)
	 * 
	 */
	private PlusModelSaleProObject averageActivityMoney(PlusModelSaleProObject pmo,List<PlusModelProObject> list,TeslaXOrder teslaOrder){
		
		BigDecimal skusCount = new BigDecimal(0.00);//可满减的商品总金额
		Map<String, Double> skuMonMap = new HashMap<String, Double>();//<商品编号,商品金额>
		Map<String, Double> skuNumMap = new HashMap<String, Double>();//<商品编号,商品数量>
		Map<String, Double> skuCouMon = new HashMap<String, Double>();//<商品编号,单品使用满减额度>
		int num = 0;//随机获取单品的数量
		for (int j = 0; j < list.size(); j++) {//各参数赋值
			PlusModelProObject od = list.get(j);
			skuMonMap.put(od.getSkuCode(), od.getOrig_sku_price().doubleValue());
			skuNumMap.put(od.getSkuCode(),Double.valueOf(od.getSkuNum()));
			num = od.getSkuNum();
		}
		if(skuNumMap!=null&&!skuNumMap.isEmpty()){
			Iterator<String> skuNumIterator = skuNumMap.keySet().iterator();
			while (skuNumIterator.hasNext()) {
				String key = (String) skuNumIterator.next();
				skusCount=skusCount.add(BigDecimal.valueOf(skuMonMap.get(key)).multiply(BigDecimal.valueOf(skuNumMap.get(key))));
			}
			BigDecimal reCouponMoney = list.get(0).getFullMoneys().getCutMoney();//满减的金额
			BigDecimal ccMon = list.get(0).getFullMoneys().getCutMoney();//初始优惠金额
			BigDecimal overcouponMoney = new BigDecimal(0.00);//已使用优惠金额
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
			if(reCouponMoney.doubleValue()>0&&list.size()>0){
				List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String,Double>>();
				if(bjqk){//边界情况
					Map<String, Double> skuSPMap = new HashMap<String, Double>();//<商品编号,商品金额>
					Iterator<String> ks = skuNumMap.keySet().iterator();
					while (ks.hasNext()) {
						String key = (String) ks.next();
						skuSPMap.put(key, skuMonMap.get(key));
					}
					list_Data = new TeslaMakeBalanced().mapValueSortDesc(skuSPMap);//数量相等时，按金额倒序
				}else{
					//一般情况处理逻辑
					list_Data = new TeslaMakeBalanced().mapValueSort(skuNumMap,skuMonMap);//一般情况按数量倒序
				}
				for (int i = 0; i < list_Data.size(); i++) {
					Map.Entry<String, Double> entry = list_Data.get(i);
					String skuCode = entry.getKey();
					Double skuNum = skuNumMap.get(skuCode);
					BigDecimal skuPrice = BigDecimal.valueOf(skuMonMap.get(skuCode));
					if(i==list_Data.size()-1){//最后一个商品特殊处理
						Double eveSkuCouMon = Math.ceil(((reCouponMoney.subtract(overcouponMoney)).divide(BigDecimal.valueOf(skuNum),2,BigDecimal.ROUND_UP)).doubleValue());//向上取整 单品优惠金额
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
			}
			BigDecimal useMoney = BigDecimal.ZERO;
			Iterator<String> it = skuCouMon.keySet().iterator();
			while (it.hasNext()) {
				String skuCode = it.next();
				for (int i = 0; i < pmo.getSaleObject().size(); i++) {
					if(pmo.getSaleObject().get(i).getSkuCode().equals(skuCode)){
						pmo.getSaleObject().get(i).setSku_price(pmo.getSaleObject().get(i).getOrig_sku_price()
								.subtract(BigDecimal.valueOf(skuCouMon.get(skuCode))).setScale(2, BigDecimal.ROUND_HALF_UP));
						useMoney=useMoney.add(BigDecimal.valueOf(skuCouMon.get(skuCode)).multiply(BigDecimal.valueOf(skuNumMap.get(skuCode))));
					}
				}
			}
			Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
			for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
				TeslaModelOrderDetail od = teslaOrder.getOrderDetails().get(j);
				if("1".equals(od.getIfJJGFlag())){continue;}
				Double eveCouMoney = skuCouMon.get(od.getSkuCode());
				if(map.containsKey(od.getOrderCode())&&eveCouMoney!=null){
					map.put(od.getOrderCode(), map.get(od.getOrderCode()).add(BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum()))));
				}else if(eveCouMoney!=null){
					map.put(od.getOrderCode(), BigDecimal.valueOf(eveCouMoney).multiply(BigDecimal.valueOf(od.getSkuNum())));
				}
			}
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String orderCode = iterator.next();
				
				TeslaModelOrderPay op = new TeslaModelOrderPay();//插入一条优惠券支付信息
				op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
				op.setOrderCode(orderCode);
				op.setPayedMoney(map.get(orderCode));
				op.setPayType("449746280012");
				teslaOrder.getOcOrderPayList().add(op);
			}
			if(useMoney.compareTo(BigDecimal.ZERO)>0){
				teslaOrder.getUse().setFull_money(teslaOrder.getUse().getFull_money().add(useMoney.compareTo(ccMon)>0?ccMon:useMoney));
			}
			teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(useMoney));
			//计算人品奖励
			BigDecimal rpjl = useMoney.subtract(ccMon);
			teslaOrder.getUse().setMoral_character_money(teslaOrder.getUse().getMoral_character_money().add(rpjl));
			if(rpjl.compareTo(BigDecimal.ZERO)>0){
				for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
					if(teslaOrder.getOrderDetails().get(j).getSkuCode().equals(skuCouMon.keySet().toArray()[0])&&teslaOrder.getOrderDetails().get(j).getIfJJGFlag().equals("0")){
						TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
						activity.setOrderCode(teslaOrder.getOrderDetails().get(j).getOrderCode());
						activity.setActivityName("人品奖励(满减引起)");
						activity.setActivityType("4497472600010009");
						activity.setPreferentialMoney(rpjl);
						teslaOrder.getActivityList().add(activity);
						break;
					}
				}
			}
		}
		return pmo;
	
	}
	
	/**
	 * 均摊满折活动的金额
	 */
	private PlusModelSaleProObject averageActivityMoneyForManZhe(PlusModelSaleProObject pmo,List<PlusModelProObject> list,TeslaXOrder teslaOrder){
		// 现根据商品编号对参与的活动分组
		Map<String,List<PlusModelProObject>> groupByProductCode = new HashMap<String, List<PlusModelProObject>>();
		Map<String,PlusModelProObject> proObjMap = new HashMap<String, PlusModelProObject>();
		for(PlusModelProObject obj : list){
			if(groupByProductCode.get(obj.getProductCode()) == null){
				groupByProductCode.put(obj.getProductCode(), new ArrayList<PlusModelProObject>());
			}
			groupByProductCode.get(obj.getProductCode()).add(obj);
			
			proObjMap.put(obj.getSkuCode(), obj);
		}
		
		Map<String,TeslaModelOrderDetail> orderDetailMap = new HashMap<String, TeslaModelOrderDetail>();
		for (TeslaModelOrderDetail item : teslaOrder.getOrderDetails()) {
			if("0".equals(item.getIfJJGFlag())) {
				orderDetailMap.put(item.getSkuCode(), item);
			}
		}
		
		// 对每个分组的商品单独计算优惠金额
		BigDecimal skuCouponMoney = new BigDecimal(0);  // 单个SKU优惠金额
		BigDecimal initCutMoney = new BigDecimal(0); // 初始优惠金额
		BigDecimal allCutMoney = new BigDecimal(0); // 总使用优惠金额
		PlusModelProObject proObj = null;
		
		List<AverageMoneyModel> averageModel = null;
		AverageMoneyModel averageMoneyModel = null;
		for(List<PlusModelProObject> proList : groupByProductCode.values()){
			averageModel = new ArrayList<AverageMoneyModel>();
			for(PlusModelProObject obj : proList){
				averageMoneyModel = new AverageMoneyModel();
				averageMoneyModel.setSkuCode(obj.getSkuCode());
				averageMoneyModel.setSkuNum(obj.getSkuNum());
				averageMoneyModel.setSkuPrice(obj.getSku_price().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				averageMoneyModel.setSmallSellerCode(obj.getSmallSellerCode());
				averageModel.add(averageMoneyModel);
			}
			
			skuCouponMoney = new BigDecimal(0);
			allCutMoney = new BigDecimal(0);
			initCutMoney = proList.get(0).getFullMoneys().getCutMoney();
			
			// 均摊金额
			Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, initCutMoney);
			Set<Entry<String, BigDecimal>> moneySet = averageMoney.entrySet();
			for(Entry<String, BigDecimal> moneyEntry : moneySet){
				proObj = proObjMap.get(moneyEntry.getKey());
				skuCouponMoney = moneyEntry.getValue();
				
				allCutMoney = allCutMoney.add(skuCouponMoney.multiply(new BigDecimal(proObj.getSkuNum())));
				proObj.setSku_price(proObj.getOrig_sku_price().subtract(skuCouponMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			
			if(allCutMoney.compareTo(BigDecimal.ZERO)>0){
				teslaOrder.getUse().setFull_money(teslaOrder.getUse().getFull_money().add(allCutMoney.compareTo(initCutMoney)>0?initCutMoney:allCutMoney));
				teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(allCutMoney));
			}
			
			// 保存满减记录
			if(skuCouponMoney.compareTo(BigDecimal.ZERO) > 0){
				TeslaModelOrderPay op = new TeslaModelOrderPay();
				op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
				op.setOrderCode(orderDetailMap.get(proObj.getSkuCode()).getOrderCode());
				op.setPayedMoney(allCutMoney);
				op.setPayType("449746280012");
				op.setPayRemark("满减商品["+proObj.getProductCode()+"]");
				teslaOrder.getOcOrderPayList().add(op);
			}
			
			// 均分后累加优惠金额大于满减金额则出现人品奖励
			if(allCutMoney.compareTo(initCutMoney) > 0){
				TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
				activity.setOrderCode(orderDetailMap.get(proObj.getSkuCode()).getOrderCode());
				activity.setActivityName("人品奖励(满折引起)");
				activity.setActivityType("4497472600010009");
				activity.setPreferentialMoney(allCutMoney.subtract(initCutMoney));
				teslaOrder.getActivityList().add(activity);
				
				teslaOrder.getUse().setMoral_character_money(teslaOrder.getUse().getMoral_character_money().add(activity.getPreferentialMoney()));
			}
		}
	
		return pmo;
	}
}
