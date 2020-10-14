package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 储值金相关功能方法
 */
public class TeslaPpcService extends BaseClass{
	
	/**
	 * 查询客代号可用积分、暂存款、储值金
	 * @param custId
	 * @return null 查询失败
	 */
	public GetCustAmtResult getPlusModelCustAmt(String custId){
		CustRelAmtRef beanRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		return beanRef.getCustAmt(custId);
	}
	
	/**
	 * 根据用户编号查询客代号
	 * @param memberCode
	 * @return null 查询失败
	 */
	public String getCustId(String memberCode){
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		return levelInfo.getCustId();
	}
	
	/**
	 * 下单时分摊储值金<br/>
	 * 优先抵扣tv品订单及运费 且取整  因为现在tv商品价格都为整数  若有变动 此处需调整  0.0<br/>
	 * 然后抵扣自营品订单及运费 精确到分<br/>
	 * @param order
	 * @return
	 */
	public TeslaXResult userCzjForOrder(TeslaXOrder order, GetCustAmtResult custAmt) {
		TeslaXResult result = new TeslaXResult();
		
		List<TeslaModelOrderInfo> orderInfoList = order.getSorderInfo();
		List<TeslaModelOrderInfo> tvOrderInfoList = new ArrayList<TeslaModelOrderInfo>();
		List<TeslaModelOrderInfo> zyOrderInfoList = new ArrayList<TeslaModelOrderInfo>();
		Map<String,BigDecimal> tvDueMoneyMap = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> zyDueMoneyMap = new HashMap<String, BigDecimal>();
		// tv总订单金额
		BigDecimal tvTotalMoney = new BigDecimal(0);
		// 自营总订单金额
		BigDecimal zyTotalMoney = new BigDecimal(0);
		
		// 总使用储值金
		double czj = order.getUse().getCzj_money();
		
		// 计算订单应付的金额
		for(TeslaModelOrderInfo orderInfo : orderInfoList){
			// 储值金不抵扣运费，所以如果应付款剩余金额比运费小则此单不能使用储值金
//			if(orderInfo.getDueMoney().compareTo(orderInfo.getTransportMoney()) <= 0){
//				continue;
//			}
			if(orderInfo.getSmallSellerCode().equals("SI2003") || orderInfo.getSmallSellerCode().equals("SI2009")){
				tvOrderInfoList.add(orderInfo);
				//tv品取应付款时取整
				tvDueMoneyMap.put(orderInfo.getOrderCode(), orderInfo.getDueMoney().subtract(orderInfo.getTransportMoney()).setScale(0, BigDecimal.ROUND_DOWN));
				tvTotalMoney = tvTotalMoney.add(tvDueMoneyMap.get(orderInfo.getOrderCode()));
			}else {
				zyOrderInfoList.add(orderInfo);
				zyDueMoneyMap.put(orderInfo.getOrderCode(), orderInfo.getDueMoney().subtract(orderInfo.getTransportMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
				zyTotalMoney = zyTotalMoney.add(zyDueMoneyMap.get(orderInfo.getOrderCode()));
			}
		}
		
		//如果只有tv品 最大可用要取整
		if(tvOrderInfoList.size() > 0 && zyOrderInfoList.size() == 0) {
			//如果只有tv品 取剩余储值金和商品金额的最小的一个   最大可用要取整
			if(custAmt.getPossPpcAmt().setScale(0, BigDecimal.ROUND_DOWN).compareTo(tvTotalMoney.add(zyTotalMoney)) > 0){
				order.setMaxUseCzj(tvTotalMoney.add(zyTotalMoney));
			}else {
				order.setMaxUseCzj(custAmt.getPossPpcAmt().setScale(0, BigDecimal.ROUND_DOWN));
			}
			//用户拥有的储值金也取整
			order.setAllCzj(custAmt.getPossPpcAmt().setScale(0, BigDecimal.ROUND_DOWN));
		}else {
			//存在tv品和自营品  或只存在自营品时 取剩余储值金和商品金额的最小的一个
			if(custAmt.getPossPpcAmt().compareTo(tvTotalMoney.add(zyTotalMoney)) > 0){
				order.setMaxUseCzj(tvTotalMoney.add(zyTotalMoney));
			}else {
				order.setMaxUseCzj(custAmt.getPossPpcAmt().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			order.setAllCzj(custAmt.getPossPpcAmt().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		
		// 使用的储值金不能超过总储值金
		if(order.getUse().getCzj_money() > 0 && order.getUse().getCzj_money() > order.getAllCzj().doubleValue()){
			result.setResultCode(963907054);
			result.setResultMessage(bInfo(963907054, czj));
			return result;
		}
		
		// 可用最大储值金不能是负的
		if(order.getMaxUseCzj().compareTo(BigDecimal.ZERO) < 0) order.setMaxUseCzj(BigDecimal.ZERO);
		
		// 使用储值金抵扣金额
		BigDecimal useCzjMoney = BigDecimal.valueOf(czj).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		// 如果是订单详情页 且未使用储值金  则直接返回最大可用储值金和用户拥有储值金
		if(order.getStatus().getExecStep() == ETeslaExec.Confirm && czj == 0) {
			return result;
		}
		
		//如果使用储值金 且非PC渠道 则使用全部可用储值金
		if(czj > 0 && !order.getChannelId().equals("449747430004")){
			order.getUse().setCzj_money(order.getMaxUseCzj().doubleValue());
			useCzjMoney = new BigDecimal(order.getUse().getCzj_money()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		// 如果使用了储值金则需要校验一下是否超过商品总金额
		if(useCzjMoney.compareTo(BigDecimal.ZERO) > 0 && tvTotalMoney.add(zyTotalMoney).compareTo(useCzjMoney) < 0){
			result.setResultCode(963907056);
			result.setResultMessage(bInfo(963907056, tvTotalMoney.add(zyTotalMoney).doubleValue()));
			return result;
		}
		
		// 拆分储值金
		if(useCzjMoney.compareTo(BigDecimal.ZERO) > 0 && tvTotalMoney.add(zyTotalMoney).compareTo(BigDecimal.ZERO) > 0){
			
			//优先摊tv品订单 按金额降序摊
			List<TeslaModelOrderInfo> sortOrderInfoList = new ArrayList<TeslaModelOrderInfo>(tvOrderInfoList);
			
			// 根据金额倒序排列
			Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
				@Override
				public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
					return o2.getDueMoney().compareTo(o1.getDueMoney());
				}
			});
			
			// 使用的储值金和总商品金额的占比
			BigDecimal dueMoney = null;
			// 分到的金额
			BigDecimal useMoney = null;
			// 累计拆分的金额
			BigDecimal allUseMonty = new BigDecimal(0);
			for (TeslaModelOrderInfo orderInfo : sortOrderInfoList) {
				dueMoney = tvDueMoneyMap.get(orderInfo.getOrderCode());
				
				// 商品金额为0的不参与储值金拆分
				if(dueMoney == null || dueMoney.compareTo(BigDecimal.ZERO) <= 0) {
					continue; 
				}
				
				if(useCzjMoney.compareTo(dueMoney) >= 0) {
					useMoney = dueMoney;
				}else {
					useMoney = useCzjMoney.setScale(0,BigDecimal.ROUND_DOWN);
				}
				
				// 不能超过使用的储值金
				if(allUseMonty.add(useMoney).compareTo(useCzjMoney) > 0){
					useMoney = useCzjMoney.subtract(allUseMonty).setScale(0,BigDecimal.ROUND_DOWN);
				}
				
				// 记录拆分到每单上面的储值金
				if(useMoney.compareTo(BigDecimal.ZERO) > 0){
					allUseMonty = allUseMonty.add(useMoney);
					createPayInfo(order, orderInfo.getOrderCode(), useMoney, "449746280006", order.getCustId());
					orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(useMoney));
					
					// 拆分到商品
					splitCzjForSku(order, orderInfo, useMoney, "tv");
				}
			}
			
			//如果还有可用储值金 则摊自营品订单 按金额降序摊
			if(useCzjMoney.subtract(allUseMonty).compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal zyUseCzjMoney = useCzjMoney.subtract(allUseMonty);
				sortOrderInfoList = new ArrayList<TeslaModelOrderInfo>(zyOrderInfoList);
				
				// 根据金额倒序排列
				Collections.sort(sortOrderInfoList, new Comparator<TeslaModelOrderInfo>() {
					@Override
					public int compare(TeslaModelOrderInfo o1, TeslaModelOrderInfo o2) {
						return o2.getDueMoney().compareTo(o1.getDueMoney());
					}
				});
				
				for(TeslaModelOrderInfo orderInfo : sortOrderInfoList){
					dueMoney = zyDueMoneyMap.get(orderInfo.getOrderCode());
					
					// 商品金额为0的不参与储值金拆分
					if(dueMoney == null || dueMoney.compareTo(BigDecimal.ZERO) <= 0) {
						continue; 
					}
					
					// 按比例拆分金额 ： 商品金额 / 总金额 * 使用储值金金额
					useMoney = dueMoney.divide(zyTotalMoney,4,BigDecimal.ROUND_UP).multiply(zyUseCzjMoney).setScale(2, BigDecimal.ROUND_UP);
					if(zyTotalMoney.compareTo(zyUseCzjMoney) == 0) {
						useMoney = dueMoney;
					}
					//拆分到的钱不能大于商品金额
					if(useMoney.compareTo(dueMoney) > 0){
						useMoney = dueMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					
					// 不能超过使用的储值金
					if(allUseMonty.add(useMoney).compareTo(useCzjMoney) > 0){
						useMoney = useCzjMoney.subtract(allUseMonty);
					}
					
					// 记录拆分到每单上面的储值金
					if(useMoney.compareTo(BigDecimal.ZERO) > 0){
						allUseMonty = allUseMonty.add(useMoney);
						createPayInfo(order, orderInfo.getOrderCode(), useMoney, "449746280006", order.getCustId());
						orderInfo.setDueMoney(orderInfo.getDueMoney().subtract(useMoney));
						
						// 拆分到商品
						splitCzjForSku(order, orderInfo, useMoney, "zy");
					}
				}
			}
			
			// 拆单后修正一下最终可用的储值金
			order.setMaxUseCzj(allUseMonty);
			order.getUse().setCzj_money(order.getMaxUseCzj().doubleValue());
		}
		
		return result;
	}
	
	private void createPayInfo(TeslaXOrder order,String order_code,BigDecimal dueMoney,String payType, String payRemark){
		TeslaModelOrderPay opCzj=new TeslaModelOrderPay();
		opCzj.setMerchantId(order.getUorderInfo().getBuyerCode());
		opCzj.setOrderCode(order_code);
		opCzj.setPayType(payType);
		opCzj.setPayedMoney(dueMoney);
		opCzj.setPayRemark(payRemark);
		order.getOcOrderPayList().add(opCzj);
	}
	
	/**
	 * 将储值金拆分到每个sku上<br/>
	 * tv品向上取整<br/>
	 * 自营品精确到分
	 * @param order
	 * @param orderInfo
	 * @param useMoney 拆分到订单上的储值金
	 * @param type 订单类型
	 */
	private void splitCzjForSku(TeslaXOrder order, TeslaModelOrderInfo orderInfo, BigDecimal useMoney, String type) {
		
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
			if(!orderInfo.getOrderCode().equals(detail.getOrderCode())){
				continue;
			}
			
			//获取商品价格时  需要扣除积分抵扣金额  如果抵扣优先级变动此次也需更改
			BigDecimal skuMoney = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())).subtract(detail.getIntegralMoney()).subtract(detail.getHjycoin());
			
			detailList.add(detail);
			productMoney = productMoney.add(skuMoney);
		}
		
		//如果只用储值金抵用运费 则不需要拆分
		if(productMoney.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		
		BigDecimal skuUseMonty = BigDecimal.ZERO;  // 累计拆分到商品上面的储值金
		for(TeslaModelOrderDetail detail : detailList){
			if(skuUseMonty.compareTo(useMoney) >= 0){
				break;
			}
			
			// 拆分的金额
			BigDecimal m = BigDecimal.ZERO;
			
			if("tv".equals(type)) {
				// 拆分的金额 = 商品单价  x 商品数量 - 积分抵扣金额 - 惠币  / 总商品金额  x 整单储值金金额
				m = detail.getSkuPrice()
						.multiply(new BigDecimal(detail.getSkuNum()))
						.subtract(detail.getIntegralMoney())
						.subtract(detail.getHjycoin())
						.divide(productMoney, 4, BigDecimal.ROUND_UP)
						.multiply(useMoney)
						.setScale(0, BigDecimal.ROUND_UP);
			}
			if("zy".equals(type)) {
				// 拆分的金额 = 商品单价  x 商品数量 - 积分抵扣金额 - 惠币  / 总商品金额  x 整单储值金金额
				m = detail.getSkuPrice()
						.multiply(new BigDecimal(detail.getSkuNum()))
						.subtract(detail.getIntegralMoney())
						.subtract(detail.getHjycoin())
						.divide(productMoney, 4, BigDecimal.ROUND_UP)
						.multiply(useMoney)
						.setScale(2, BigDecimal.ROUND_UP);
			}
			// 不能超过订单储值金金额
			if(skuUseMonty.add(m).compareTo(useMoney) > 0){
				m = useMoney.subtract(skuUseMonty);
			}
			
			// 不能超过商品的金额
			if(m.compareTo(detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())).subtract(detail.getIntegralMoney()).subtract(detail.getHjycoin())) > 0){
				m = detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())).subtract(detail.getIntegralMoney()).subtract(detail.getHjycoin());
			}
			
			// 累加已经拆分到商品的储值金金额
			skuUseMonty = skuUseMonty.add(m);
			detail.setCzjMoney(m);
			
		}
	}
	
	/**
	 * 添加取消订单退还储值金的定时
	 * @param orderCode
	 * @param remark
	 * @return
	 */
	public void addExecInfoForCancel(String orderCode, String remark){
		MDataMap payData = DbUp.upTable("oc_order_pay").oneWhere("payed_money", "", "", "pay_type", "449746280006", "order_code", orderCode);
		BigDecimal money = null;
		
		if(payData != null){
			money = new BigDecimal(payData.get("payed_money"));
		}
		
		// 未使用储值金，不需要返还
		if(payData == null || money.compareTo(BigDecimal.ZERO) <= 0){
			return;
		}
		
		// LD订单已经产生了外部订单号的情况下不走退还储值金定时，直接走LD系统内退还储值金
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("small_seller_code,out_order_code", "", "", "order_code", orderCode);
		if((orderInfo.get("small_seller_code").equalsIgnoreCase("SI2003") || orderInfo.get("small_seller_code").equalsIgnoreCase("SI2009"))
				&& !orderInfo.get("out_order_code").isEmpty()){
			return;
		}
		
		String code = WebHelper.upCode("PTC");
		JobExecHelper.createExecInfo("449746990009", code, null);
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("pay_return_code", code);
		mDataMap.put("order_code", orderCode);
		mDataMap.put("target_info", "");
		mDataMap.put("pay_type", "449746280006");
		mDataMap.put("return_type", "449748160001");
		mDataMap.put("return_money", payData.get("payed_money"));
		mDataMap.put("return_status", "449748090001");
		mDataMap.put("remark", StringUtils.trimToEmpty(remark));
		mDataMap.put("create_time", FormatHelper.upDateTime());
		mDataMap.put("update_time", mDataMap.get("create_time"));
		DbUp.upTable("oc_order_pay_return").dataInsert(mDataMap);
	}
	
	/**
	 * 订单申请退货时添加储值金返还的定时
	 * @param orderCode
	 * @param returnCode
	 * @param remark
	 * @return
	 */
	public void addExecInfoForReturn(String orderCode, String returnCode, String remark){
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").oneWhere("order_code,expected_return_ppc_money", "", "", "return_code", returnCode);
		BigDecimal ppcMoney = new BigDecimal(returnGoods.get("expected_return_ppc_money"));
		
		// 未使储值金，不需要返还
		if(ppcMoney.compareTo(BigDecimal.ZERO) <= 0){
			return;
		}
		
		String code = WebHelper.upCode("PTC");
		JobExecHelper.createExecInfo("449746990009", code, null);
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("pay_return_code", code);
		mDataMap.put("order_code", orderCode);
		mDataMap.put("target_info", returnCode);
		mDataMap.put("pay_type", "449746280006");
		mDataMap.put("return_type", "449748160002");
		mDataMap.put("return_money", ppcMoney.toString());
		mDataMap.put("return_status", "449748090001");
		mDataMap.put("remark", StringUtils.trimToEmpty(remark));
		mDataMap.put("create_time", FormatHelper.upDateTime());
		mDataMap.put("update_time", mDataMap.get("create_time"));
		DbUp.upTable("oc_order_pay_return").dataInsert(mDataMap);
	}
	
	/**
	 * 退还储值金
	 * @param orderCode 订单号
	 * @param flag  操作标识
	 * @param money 退还储值金金额
	 * @return
	 */
	public RootResult returnForPpcAmt(String orderCode, UpdateCustAmtInput.CurdFlag flag, BigDecimal money){
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("buyer_code,order_code,big_order_code", "", "", "order_code", orderCode);
		MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code", orderCode, "pay_type", "449746280006");
		
		String custId = orderPay.get("pay_remark");
		if(StringUtils.isBlank(custId)){
			custId = getCustId(orderInfo.get("buyer_code"));
		}
		
		CustRelAmtRef beanRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		UpdateCustAmtInput input = new UpdateCustAmtInput();
		input.setCurdFlag(flag);
		input.setCustId(custId);
		input.setBigOrderCode(orderInfo.get("big_order_code"));
		
		UpdateCustAmtInput.ChildOrder childOrder = new UpdateCustAmtInput.ChildOrder();
		childOrder.setAppChildOrdId(orderInfo.get("order_code"));
		childOrder.setChildPpcAmt(money);
		input.getOrderList().add(childOrder);
		
		RootResult rootResult = beanRef.updateCustAmt(input);
		return rootResult;
	}
	
	/**
	 * 计算部分退货时应返还的储值金
	 * @param order_code 订单编号
	 * @param sku_code 
	 * @param produceNum sku数量
	 * @return
	 */
	public BigDecimal handlerReturnCzjMoney(String order_code, String sku_code, int produceNum) {
		
		MDataMap detail=DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
		MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		BigDecimal expected_return_money = (new BigDecimal(produceNum)).multiply(new BigDecimal(detail.get("sku_price")));
		
		// 预期退还的储值金金额
		BigDecimal returnCzjMoney = BigDecimal.ZERO;
		// 此SKU使用的储值金金额
		BigDecimal czjMoney = new BigDecimal(detail.get("czj_money"));
		// 商品明细上面记录储值金金额则按照明细记录计算退还储值金金额
		if(czjMoney.compareTo(BigDecimal.ZERO) > 0){
			// 此SKU已经退还的储值金金额
			BigDecimal alreadyReturnCzjMoneyForSku = getAlreadyReturnCzjMoney(order_code, sku_code);
			// 此SKU已经退货的商品数量
			int alreadyReturnSkuNumForSku = getAlreadyReturnSkuNum(order_code, sku_code);
			// 退货SKU总金额
			BigDecimal returnSkuMoney = new BigDecimal(produceNum).multiply(new BigDecimal(detail.get("sku_price")));
			// SKU总金额
			BigDecimal skuMoney = new BigDecimal(detail.get("sku_num")).multiply(new BigDecimal(detail.get("sku_price")));
			
			// 如果已经全部退还则把剩余储值金都退还
			if((alreadyReturnSkuNumForSku + produceNum) >= getOrderSkuNum(order_code, sku_code)){
				returnCzjMoney = czjMoney.subtract(alreadyReturnCzjMoneyForSku);
			}else{
				if(orderInfo.get("small_seller_code").equals("SI2003") || orderInfo.get("small_seller_code").equals("SI2009")) {
					//tv品向上取整
					// 按金额占比计算退还的储值金金额
					returnCzjMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(czjMoney).setScale(0, BigDecimal.ROUND_UP);
				}else {
					//自营品精确到分
					// 按金额占比计算退还的储值金金额
					// 必须向上取整，不然就会出现  102/306*306 结果不等于102的情况
					returnCzjMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(czjMoney).setScale(0, BigDecimal.ROUND_UP);
				}
				// 不能超过总使用储值金
				if(alreadyReturnCzjMoneyForSku.add(returnCzjMoney).compareTo(czjMoney) > 0){
					returnCzjMoney = czjMoney.subtract(alreadyReturnCzjMoneyForSku);
				}
				// 不能超过退货商品金额
				if(returnCzjMoney.compareTo(returnSkuMoney) > 0){
					returnCzjMoney = returnSkuMoney;
				}
			}
			
			if(returnCzjMoney.compareTo(BigDecimal.ZERO) < 0){
				returnCzjMoney = BigDecimal.ZERO;
			}
		}

//		// 储值金支付的钱
//		BigDecimal usedMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280006"));
//		// 商品明细未记录储值金金额的情况下按照整单占比拆分储值金
//		if(usedMoney != null && usedMoney.compareTo(BigDecimal.ZERO) > 0 && getOrderDetailCzjMoney(order_code).compareTo(BigDecimal.ZERO) == 0){
//			// 已经退还的储值金金额
//			BigDecimal alreadyReturnCzjMoney = getAlreadyReturnCzjMoney(order_code);
//			// 已经退货的商品数量
//			int alreadyReturnSkuNum = getAlreadyReturnSkuNum(order_code);
//			// 订单总的商品数量
//			int orderAllSkuNum = getOrderSkuNum(order_code);
//			// 订单总商品金额
//			BigDecimal productMoney = getOrderProductMoney(order_code);
//
//			// 订单中的商品已经全部退货则退还剩余的全部储值金
//			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
//				returnCzjMoney = usedMoney.subtract(alreadyReturnCzjMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//			}
//			
//			// 还有商品没有退货则根据比例拆分退货的储值金
//			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
//				// 根据商品金额和总单商品金额的占比计算退还储值金数量
//				returnCzjMoney = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(usedMoney).setScale(0, BigDecimal.ROUND_UP);
//				// 退还储值金不能超过退货商品金额
//				if (expected_return_money.compareTo(returnCzjMoney) < 0) {
//					returnCzjMoney = expected_return_money;
//				}
//				// 总的退还储值金不能大于总使用的储值金
//				if(usedMoney.compareTo(alreadyReturnCzjMoney.add(returnCzjMoney)) < 0){
//					returnCzjMoney = usedMoney.subtract(alreadyReturnCzjMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//				}
//			}
//			
//			if(returnCzjMoney.compareTo(BigDecimal.ZERO) < 0){
//				returnCzjMoney = BigDecimal.ZERO;
//			}
//			
//		}
		
		return returnCzjMoney;
	}
	
	/**
	 * 已经退还的储值金金额
	 * @param order_code
	 * @return
	 */
	private BigDecimal getAlreadyReturnCzjMoney(String order_code){
		Object allReturnMoney = DbUp.upTable("oc_return_goods").dataGet("IFNULL(sum(expected_return_ppc_money),0.0)", "order_code = :order_code AND `status` NOT IN('4497153900050006','4497153900050007')", new MDataMap("order_code", order_code));
		return allReturnMoney == null ? BigDecimal.ZERO : new BigDecimal(allReturnMoney.toString());
	}
	
	/**
	 * 按SKU查询已经退还的储值金金额
	 * @param order_code
	 * @param skuCode
	 * @return
	 */
	private BigDecimal getAlreadyReturnCzjMoney(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(rgd.return_ppc_money),0) money FROM oc_return_goods rg, oc_return_goods_detail rgd"
				+ " WHERE rg.return_code = rgd.return_code"
				+ " AND rg.order_code = :order_code "
				+ " AND rgd.sku_code = :sku_code"
				+ " AND rg.`status` NOT IN('4497153900050006','4497153900050007')";
		Map<String, Object> result = DbUp.upTable("oc_return_goods").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? BigDecimal.ZERO : new BigDecimal(result.get("money").toString());
	}
	
	/**
	 * 已经退货的商品数量
	 * @param order_code
	 * @return
	 */
	private int getAlreadyReturnSkuNum(String order_code){
		Map<String, Object> result = DbUp.upTable("oc_return_goods_detail").dataSqlOne("SELECT IFNULL(SUM(`count`),0) skuNum FROM oc_return_goods_detail WHERE return_code IN (SELECT rg.return_code FROM oc_return_goods rg WHERE rg.order_code = :order_code AND status NOT IN('4497153900050006','4497153900050007'))", new MDataMap("order_code", order_code));
		return result == null ? 0 : Integer.parseInt(result.get("skuNum").toString());
	}
	
	/**
	 * 按SKU查询已经退货的商品数量
	 * @param order_code
	 * @param skuCode
	 * @return
	 */
	private int getAlreadyReturnSkuNum(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(`count`),0) skuNum FROM oc_return_goods_detail WHERE"
				+ " return_code IN (SELECT rg.return_code FROM oc_return_goods rg WHERE rg.order_code = :order_code AND status NOT IN('4497153900050006','4497153900050007')) "
				+ " AND sku_code = :sku_code";
		Map<String, Object> result = DbUp.upTable("oc_return_goods_detail").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? 0 : Integer.parseInt(result.get("skuNum").toString());
	}
	
	/**
	 * 订单总商品数量
	 * @param order_code
	 * @return
	 */
	private int getOrderSkuNum(String order_code){
		Object result = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return result == null ? 0 : Integer.parseInt(result.toString());
	}
	
	/**
	 * 按SKU查询总商品数量
	 * @param order_code
	 * @param skuCode
	 * @return
	 */
	private int getOrderSkuNum(String order_code,String skuCode){
		Object result = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num)", "", new MDataMap("order_code", order_code, "sku_code", skuCode, "gift_flag", "1"));
		return result == null ? 0 : Integer.parseInt(result.toString());
	}
	
	/**
	 * 统计商品明细上面的储值金总金额
	 * @param order_code
	 * @return
	 */
	private BigDecimal getOrderDetailCzjMoney(String order_code){
		Object money = DbUp.upTable("oc_orderdetail").dataGet("sum(czj_money)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return money == null ? BigDecimal.ZERO : new BigDecimal(money.toString());
	}
	
	/**
	 * 订单总商品金额
	 * @param order_code
	 * @return
	 */
	private BigDecimal getOrderProductMoney(String order_code){
		Object productMoney = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num*sku_price)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return productMoney == null ? BigDecimal.ZERO : new BigDecimal(productMoney.toString());
	}
	
}
