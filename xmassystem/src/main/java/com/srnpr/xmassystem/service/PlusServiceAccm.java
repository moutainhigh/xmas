package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 积分相关功能方法
 */
public class PlusServiceAccm {
	
	// 积分和金额转换的系数
	private static int RATE = 200;
	
	// 最低毛利率 5%
	private static BigDecimal MIN_PROFIT_TAX = new BigDecimal("0.05");
	// 1元赋予1个积分
	private static BigDecimal INTEGRAL_TAX = new BigDecimal(1);
	// 最大计算积分的金额
	private static BigDecimal MAX_PRODUCT_MONEY = new BigDecimal(20000);
	
	/**
	 * 积分数转金额，默认保留3位小数
	 * @param amt
	 * @return
	 */
	public BigDecimal accmAmtToMoney(BigDecimal amt){
		return amt.divide(new BigDecimal(RATE), 3, BigDecimal.ROUND_HALF_UP);
	}
	
	/**积分数转金额，支持保留小说位控制
	 * @param amt
	 * @param scale
	 * @return
	 */
	public BigDecimal accmAmtToMoney(BigDecimal amt, int scale){
		return amt.divide(new BigDecimal(RATE), scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 金额转积分数，支持保留小说位控制
	 * @param money
	 * @param scale
	 * @return 四舍五入后的值
	 */
	public BigDecimal moneyToAccmAmt(BigDecimal money, int scale){
		return money.multiply(new BigDecimal(RATE)).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 金额转积分数，支持小说位以及舍入控制
	 * @param money
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public BigDecimal moneyToAccmAmt(BigDecimal money, int scale, int roundingMode){
		return money.multiply(new BigDecimal(RATE)).setScale(scale, roundingMode);
	}
	
	/**
	 * 计算下单赠与积分<br>
	 * LD商品积分赋予数量和商品金额一致
	 * @param productMoney     商品金额
	 * @param payMoney         实付金额
	 * @param productCost      商品成本
	 * @param smallSellerCode  商户编号，用于区分LD商品积分赋予
	 * @return 积分个数
	 */
	public int productForAccmAmt(BigDecimal productMoney, BigDecimal payMoney, BigDecimal productCost, String smallSellerCode){
		// LD的商品积分赋予以商品金额为准
		if("SI2003".equalsIgnoreCase(smallSellerCode)){
			return productMoney.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		
		// 毛利 
		BigDecimal profit = payMoney.subtract(productCost);
		// 毛利小于等于0的不赠与积分
		if(profit.compareTo(BigDecimal.ZERO) <= 0){
			return 0;
		}
		
		// 毛利率
		BigDecimal profitTax = profit.divide(productMoney,2,BigDecimal.ROUND_DOWN);
		
		// 低于最低毛利率的不赋予积分
		if(profitTax.compareTo(MIN_PROFIT_TAX) < 0){
			return 0;
		}
		
		// 最大赋予积分的计算金额不超过最大值
		BigDecimal money = payMoney;
		if(payMoney.compareTo(MAX_PRODUCT_MONEY) > 0){
			money = MAX_PRODUCT_MONEY;
		}
		
		// 总共赋予多少积分 = 实付金额  / 1
		return money.divide(INTEGRAL_TAX,0,BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	/**
	 * 计算下单赠与积分<br>
	 * LD商品积分赋予数量和商品金额一致
	 * @param productMoney     商品金额
	 * @param payMoney         实付金额
	 * @param productCost      商品成本
	 * @param smallSellerCode  商户编号，用于区分LD商品积分赋予
	 * @return 积分个数
	 */
	public int productForAccmAmt(BigDecimal productMoney, BigDecimal payMoney, BigDecimal czjMoney, BigDecimal productCost, String smallSellerCode){
		// LD的商品积分赋予以商品金额为准
		if("SI2003".equalsIgnoreCase(smallSellerCode)){
			return productMoney.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		
		// 毛利 
		BigDecimal profit = payMoney.subtract(productCost);
		// 毛利小于等于0的不赠与积分
		if(profit.compareTo(BigDecimal.ZERO) <= 0){
			return 0;
		}
		
		// 毛利率
		BigDecimal profitTax = profit.divide(productMoney,2,BigDecimal.ROUND_DOWN);
		
		// 低于最低毛利率的不赋予积分
		if(profitTax.compareTo(MIN_PROFIT_TAX) < 0){
			return 0;
		}
		
		// 最大赋予积分的计算金额不超过最大值
		//BigDecimal money = payMoney.subtract(czjMoney);
		
		// 储值金也赋予积分
		BigDecimal money = payMoney;
		if(money.compareTo(BigDecimal.ZERO) <= 0){
			return 0;
		}
		
		if(money.compareTo(MAX_PRODUCT_MONEY) > 0){
			money = MAX_PRODUCT_MONEY;
		}
		
		// 总共赋予多少积分 = 实付金额  / 1
		return money.divide(INTEGRAL_TAX,0,BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	/**
	 * 积分金额转换系数
	 * @return
	 */
	public int getRate(){
		return RATE;
	}
	
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
	 * @return
	 */
	public String getCustId(String memberCode){
//		MDataMap map = DbUp.upTable("mc_extend_info_homehas").oneWhere("homehas_code", "", "member_code = :member_code AND homehas_code != ''", "member_code", memberCode);
//		return map == null ? null : map.get("homehas_code");
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		return levelInfo.getCustId();
	}
	
	/**
	 * 根据用户编号查询客代号和客户等级
	 * @param memberCode
	 * @return
	 */
	public MDataMap getCustInfo(String memberCode){
		return DbUp.upTable("mc_extend_info_homehas").oneWhere("", "", "member_code = :member_code AND homehas_code != ''", "member_code", memberCode);
	}
	
	/**
	 * 添加取消订单退还积分的定时
	 * @param orderCode
	 * @param remark
	 * @return
	 */
	public void addExecInfoForCancel(String orderCode, String remark){
		MDataMap payData = DbUp.upTable("oc_order_pay").oneWhere("payed_money", "", "", "pay_type", "449746280008", "order_code", orderCode);
		BigDecimal money = null;
		
		if(payData != null){
			money = new BigDecimal(payData.get("payed_money"));
		}
		
		// 未使用积分，不需要返还
		if(payData == null || money.compareTo(BigDecimal.ZERO) <= 0){
			return;
		}
		
		// LD订单已经产生了外部订单号的情况下不走退还积分定时，直接走LD系统内退还积分
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("small_seller_code,out_order_code", "", "", "order_code", orderCode);
		if((orderInfo.get("small_seller_code").equalsIgnoreCase("SI2003") || orderInfo.get("small_seller_code").equalsIgnoreCase("SI2009"))
				&& !orderInfo.get("out_order_code").isEmpty()){
			return;
		}
		
		String code = WebHelper.upCode("PTC");
		JobExecHelper.createExecInfo("449746990003", code, null);
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("pay_return_code", code);
		mDataMap.put("order_code", orderCode);
		mDataMap.put("target_info", "");
		mDataMap.put("pay_type", "449746280008");
		mDataMap.put("return_type", "449748080001");
		mDataMap.put("return_money", payData.get("payed_money"));
		mDataMap.put("return_status", "449748090001");
		mDataMap.put("remark", StringUtils.trimToEmpty(remark));
		mDataMap.put("create_time", FormatHelper.upDateTime());
		mDataMap.put("update_time", mDataMap.get("create_time"));
		DbUp.upTable("oc_order_pay_return").dataInsert(mDataMap);
	}
	
	/**
	 * 订单退货时添加积分返还的定时
	 * @param orderCode
	 * @param returnCode
	 * @param remark
	 * @return
	 */
	public void addExecInfoForReturn(String orderCode, String returnCode, String remark){
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").oneWhere("order_code,expected_return_accm_money", "", "", "return_code", returnCode);
		BigDecimal accmMoney = new BigDecimal(returnGoods.get("expected_return_accm_money"));
		
		// 使用了积分则添加积分返回的定时
		if(accmMoney.compareTo(BigDecimal.ZERO) > 0){
			String code = WebHelper.upCode("PTC");
			JobExecHelper.createExecInfo("449746990003", code, null);
			
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("pay_return_code", code);
			mDataMap.put("order_code", orderCode);
			mDataMap.put("target_info", returnCode);
			mDataMap.put("pay_type", "449746280008");
			mDataMap.put("return_type", "449748080002");
			mDataMap.put("return_money", accmMoney.toString());
			mDataMap.put("return_status", "449748090001");
			mDataMap.put("remark", StringUtils.trimToEmpty(remark));
			mDataMap.put("create_time", FormatHelper.upDateTime());
			mDataMap.put("update_time", mDataMap.get("create_time"));
			DbUp.upTable("oc_order_pay_return").dataInsert(mDataMap);
		}
	}
	
	/**
	 * 订单签收时添加赋予积分的定时
	 * @param orderCode
	 * @param returnCode
	 * @param remark
	 * @return
	 */
	public void addExecInfoForSuccess(String orderCode){
		BigDecimal money = (BigDecimal)DbUp.upTable("oc_orderdetail").dataGet("sum(give_integral_money*sku_num)", "", new MDataMap("order_code", orderCode));
		if(money != null && money.compareTo(BigDecimal.ZERO) <= 0) return;
		
		// LD 订单不走惠家有赋予
		if("SI2003".equals(DbUp.upTable("oc_orderinfo").dataGet("small_seller_code", "", new MDataMap("order_code", orderCode)))){
			return;
		}
		
		// 15天后赋予积分,第16天开始执行赋予任务
		JobExecHelper.createExecInfo("449746990006", orderCode, FormatHelper.upDateTime(DateUtils.addDays(new Date(), 16), "yyyy-MM-dd")+" 00:00:00");
	}
	
	/**
	 * 退还积分
	 * @param orderCode 订单号
	 * @param flag  操作标识
	 * @param money 退还积分金额
	 * @return
	 */
	public RootResult returnForAccmAmt(String orderCode, UpdateCustAmtInput.CurdFlag flag, BigDecimal money){
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("buyer_code,order_code,big_order_code", "", "", "order_code", orderCode);
		MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code", orderCode, "pay_type", "449746280008");
		
		String custId = orderPay.get("pay_remark");
		if(StringUtils.isBlank(custId)){
			custId = getCustId(orderInfo.get("buyer_code"));
		}
		
		return changeForAccmAmt(flag, money, custId, orderInfo.get("big_order_code"), orderCode);
	}
	
	/**
	 * 订单退货时退还下单赋予的积分任务，调用后会立即调用一次接口，如果失败则加入到定时任务
	 * @param returnGoodsCode
	 * @param flag
	 * @return
	 */
	public RootResult addExecInfoForReturnGiveAccmAmt(String returnCode){
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").one("return_code", returnCode);
		MDataMap changeMap = null;
		
		if(returnGoods != null){
			changeMap = DbUp.upTable("mc_member_integral_change").one("member_code",returnGoods.get("buyer_code"),"change_type","449748080004","remark",returnGoods.get("order_code")); 
		}
		
		// 退货单不存在的情况下插入到定时日志表进行重试
		if(returnGoods == null){
			JobExecHelper.createExecInfo("449746990007", returnCode, null);
			return new RootResult();
		}
		
		// 还没有赋予过积分则不调用退货的接口
		if(changeMap == null){
			return new RootResult();
		}
		
		BigDecimal money = new BigDecimal(returnGoods.get("expected_return_give_accm_money"));
		if(money.compareTo(BigDecimal.ZERO) <= 0){
			return new RootResult();
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", returnGoods.get("order_code"));
		RootResult rootResult = changeForAccmAmt(UpdateCustAmtInput.CurdFlag.ZB, money, changeMap.get("cust_id"), orderInfo.get("big_order_code"), orderInfo.get("order_code"));
		if(rootResult.getResultCode() != 1){
			// 操作不成功，加入到定时任务中进行重试
			JobExecHelper.createExecInfo("449746990007", returnCode, null);
		}else{
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", orderInfo.get("buyer_code"));
			changeDataMap.put("cust_id", changeMap.get("cust_id"));
			changeDataMap.put("change_type", "449748080005");
			changeDataMap.put("change_money", money.toString());
			changeDataMap.put("remark", returnCode);
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
			
			//增加积分共享逻辑 rhb 20190315
			List<MDataMap> teamList = getIntegralTeamList(orderInfo.get("buyer_code"),orderInfo.get("create_time"));
			if(null != teamList && !teamList.isEmpty()) {
				for (MDataMap team : teamList) {
					String teamMemberCode = team.get("invitee_code");
					String teamCustId = getCustId(teamMemberCode);
					RootResult teamResult = changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GR, money, teamCustId, orderInfo.get("big_order_code"), returnCode);
					// 记录积分变更日志  - 积分共享减少
					if(teamResult.getResultCode() == 1) {
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("member_code", teamMemberCode);
						mDataMap.put("cust_id", teamCustId);
						mDataMap.put("change_type", "449748080007");
						mDataMap.put("change_money", money.toString());
						mDataMap.put("remark", returnCode);
						mDataMap.put("create_time", FormatHelper.upDateTime());
						DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
					}
					
				}
			}
		}
		
		return rootResult;
	}
	
	/**
	 * 取消退货单需要还原赋予的积分
	 * @param returnCode
	 * @return
	 */
	public RootResult addExecInfoForCancelReturnGiveAccmAmt(String returnCode){
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").one("return_code", returnCode);
		MDataMap changeMap = DbUp.upTable("mc_member_integral_change").one("member_code",returnGoods.get("buyer_code"),"change_type","449748080005","remark",returnCode); 

		// 没有调用过退货的接口则也不调用取消退货的接口
		if(changeMap == null){
			return new RootResult();
		}
		
		BigDecimal money = new BigDecimal(returnGoods.get("expected_return_give_accm_money"));
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", returnGoods.get("order_code"));
		RootResult rootResult = changeForAccmAmt(UpdateCustAmtInput.CurdFlag.ZC, money, changeMap.get("cust_id"), orderInfo.get("big_order_code"), orderInfo.get("order_code"));
		if(rootResult.getResultCode() != 1){
			// 操作不成功，加入到定时任务中进行重试
			JobExecHelper.createExecInfo("449746990008", returnCode, null);
		}else{
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", orderInfo.get("buyer_code"));
			changeDataMap.put("cust_id", changeMap.get("cust_id"));
			changeDataMap.put("change_type", "449748080006");
			changeDataMap.put("change_money", money.toString());
			changeDataMap.put("remark", returnCode);
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
			
			//增加积分共享逻辑 rhb 20190315
			List<MDataMap> teamList = getIntegralTeamList(orderInfo.get("buyer_code"),orderInfo.get("create_time"));
			if(null != teamList && !teamList.isEmpty()) {
				for (MDataMap team : teamList) {
					String teamMemberCode = team.get("invitee_code");
					String teamCustId = getCustId(teamMemberCode);
					RootResult teamResult = changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GRC, money, teamCustId, orderInfo.get("big_order_code"), orderInfo.get("order_code"));
					// 记录积分变更日志  - 积分共享增加
					if(teamResult.getResultCode() == 1) {
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("member_code", teamMemberCode);
						mDataMap.put("cust_id", teamCustId);
						mDataMap.put("change_type", "449748080007");
						mDataMap.put("change_money", money.toString());
						mDataMap.put("remark", orderInfo.get("order_code"));
						mDataMap.put("create_time", FormatHelper.upDateTime());
						DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
					}
					
				}
			}
		}
		
		return rootResult;
	}
	
	/**
	 * 积分变更
	 * @param flag
	 * @param money
	 * @param custId
	 * @param bigOrderCode
	 * @param orderCode
	 * @return
	 */
	public RootResult changeForAccmAmt(UpdateCustAmtInput.CurdFlag flag, BigDecimal money, String custId, String bigOrderCode, String orderCode){
		CustRelAmtRef beanRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		UpdateCustAmtInput input = new UpdateCustAmtInput();
		input.setCurdFlag(flag);
		input.setCustId(custId);
		input.setBigOrderCode(bigOrderCode);
		
		UpdateCustAmtInput.ChildOrder childOrder = new UpdateCustAmtInput.ChildOrder();
		childOrder.setAppChildOrdId(orderCode);
		childOrder.setChildAccmAmt(money);
		input.getOrderList().add(childOrder);
		
		RootResult rootResult = beanRef.updateCustAmt(input);
		return rootResult;
	}
	
	/**
	 * 积分启用的控制
	 * @return
	 */
	public boolean isEnabledAccm(){
		return "Y".equalsIgnoreCase(TopConfig.Instance.bConfig("xmassystem.integral_enabled"));
	}
	
	/**
	 * 查询积分共享关系列表
	 * @param inviterCode 邀请人编号
	 * @param createTime 订单创建时间
	 * @return
	 */
	public List<MDataMap> getIntegralTeamList(String inviterCode, String createTime){
		List<MDataMap> teamList = new ArrayList<MDataMap>();
		String sSql = "SELECT event_code FROM sc_hudong_event_info WHERE event_status='4497472700020002' AND "
				+ "event_type_code='449748210003' AND begin_time<=:that_time AND end_time>=:that_time";
		Map<String, Object> event = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql, new MDataMap("that_time",createTime));
		if(null!=event) {
			String eventCode = event.get("event_code")+"";
			teamList = DbUp.upTable("mc_integral_relation").query("", "update_time",
					"inviter_code=:member_code and is_valid='1' and create_time<=:create_time and event_code=:event_code",
					new MDataMap("member_code", inviterCode, "create_time", createTime,"event_code",eventCode),-1, -1);
		}
		
		return teamList;
	}
}
