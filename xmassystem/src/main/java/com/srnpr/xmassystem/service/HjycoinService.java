package com.srnpr.xmassystem.service;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 惠币相关的基础方法
 */
public class HjycoinService {

	/** 推广赚类型： 推广收益 */
	public static String TGZ_TYPE_FX = "4497471600610001";
	/** 推广赚类型： 买家秀 */
	public static String TGZ_TYPE_SHOW = "4497471600610002";
	
	/**
	 * 是否启用赋予惠币
	 * @return
	 */
	public boolean checkFlagEnabled() {
		MDataMap map = DbUp.upTable("zw_define").oneWhere("define_name", "", "", "define_dids", "469923320001", "parent_did", "46992332");
		return map != null && "1".equals(map.get("define_name"));
	}
	
	/**
	 * 判断指定订单来源是否可以使用惠币
	 * @return true 可以使用， 不能使用
	 */
	public boolean checkUseEnabled(String orderSource) {
		// 如果不在排除列表则可以使用惠币
		int i = DbUp.upTable("zw_define").dataCount("define_dids = '469923320004' AND define_name like :orderSource", new MDataMap("orderSource", "%" + orderSource + "%"));
		// 特殊渠道扫码不能使用惠币
		int j = DbUp.upTable("sc_erwei_code_channel").count("order_source",orderSource);
		return i == 0 && j == 0;
	}
	
	/**
	 * 判断指定订单来源是否可以赋予惠币<br>
	 * 需要跟全局开关配合使用  checkFlagEnabled
	 * @return true 可以赋予，false 不能赋予
	 */
	public boolean checkGiveEnabled(String orderSource) {
		// 如果不在排除列表则可以赋予惠币
		int i = DbUp.upTable("zw_define").dataCount("define_dids = '469923320003' AND define_name like :orderSource", new MDataMap("orderSource", "%" + orderSource + "%"));
		// 特殊渠道扫码也不赋予惠币
		int j = DbUp.upTable("sc_erwei_code_channel").count("order_source",orderSource);
		return i == 0 && j == 0;
	}
	
	/**
	 * 查询预估运费
	 * @return
	 */
	public BigDecimal getShipmentMoney() {
		MDataMap map = DbUp.upTable("zw_define").oneWhere("define_name", "", "", "define_dids", "469923320002", "parent_did", "46992332");
		
		BigDecimal money = BigDecimal.ZERO;
		if(map != null) {
			String v = map.get("define_name");
			if(StringUtils.isNotBlank(v)) {
				try {
					money = new BigDecimal(v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return money;
	}
	
	/**
	 * 查询推广类型对应的配置利润比
	 * @param tgzType
	 * @return null 表示未启用
	 */
	public MDataMap getTgzTypeConfigMap(String tgzType) {
		return DbUp.upTable("fh_tgz_profit_setting").oneWhere("tgz_type,tgz_rate,company_rate", "", "", "tgz_type", tgzType, "status", "4497473700010001");
	}
	
	/**
	 * 计算商品推广赚的金额
	 * @param profitMoney 利润： 扣除所有减项后的金额
	 * @param skuNum
	 * @param tgzRate
	 * @return
	 */
	public BigDecimal getProductTgzMoney(BigDecimal profitMoney, int skuNum, String tgzRate) {
		BigDecimal rate = new BigDecimal(tgzRate);
		// 把1到100的数值，转换为0-1的小数
		rate = rate.divide(new BigDecimal(100),2);
		
		// 计算单个商品的推广金额
		BigDecimal tgzMoney = profitMoney.multiply(rate).divide(new BigDecimal(skuNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		// 计算总推广金额
		return tgzMoney.multiply(new BigDecimal(skuNum));
	}
	
	/**
	 * 计算商品推广赚的金额
	 * @param sellMoney 总实付金额
	 * @param costMoney 总成本金额（原始成本）
	 * @param smallSellerCode 
	 * @param skuNum    件数
	 * @param cspsFlag 是否厂商配送
	 * @return
	 */
	public BigDecimal getProductTgzMoney(MDataMap configMap, BigDecimal sellMoney, BigDecimal costMoney, String smallSellerCode, int skuNum, boolean cspsFlag) {
		BigDecimal shipmentMoney = BigDecimal.ZERO;
		if("SI2003".equals(smallSellerCode)) {
			costMoney = costMoney.multiply(new BigDecimal("1.1"));
			
			// 非厂商配送商品需要计算预估配送费
			if(!cspsFlag) {
				shipmentMoney = getShipmentMoney().multiply(new BigDecimal(skuNum));
			}
		}
		
		BigDecimal profitMoney = sellMoney.subtract(costMoney).subtract(shipmentMoney);
		if(profitMoney.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		}
		
		return getProductTgzMoney(profitMoney, skuNum, configMap.get("tgz_rate"));
	}
	
	/**
	 * 
	 * @param type 449746990038	取消退货赋予惠币定时
	 *			   449746990037	退货扣除惠币定时
	 *			   449746990036	取消订单扣除惠币定时
	 *			   449746990035	下单送惠币定时
	 *			   449746990039  取消订单返还惠币
	 *			   449746990040  退货返还惠币
	 * @param infoCode 定时任务执行code，当type 是449746990035、449746990036时，code为订单号，当449746990037、449746990038时，code为退货编号。
	 * 2020-7-15
	 * Angel Joy
	 * void
	 */
	public static void addExecTimer(String type, String infoCode) {
		if(DbUp.upTable("za_exectimer").count("exec_type",type,"exec_info",infoCode) <= 0) {
			JobExecHelper.createExecInfo(type, infoCode, DateUtil.getSysDateTimeString());
		}
	}
	
	/**
	  *  取消订单，返还使用的惠币逻辑处理
	 * @param orderCode
	 * @param remark
	 * 2020-7-16
	 * Angel Joy
	 * void
	 */
	public void addExecInfoForCancel(String orderCode, String remark){
		MDataMap payData = DbUp.upTable("oc_order_pay").oneWhere("payed_money", "", "", "pay_type", "449746280025", "order_code", orderCode);
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
		JobExecHelper.createExecInfo("449746990039", code, null);
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("pay_return_code", code);
		mDataMap.put("order_code", orderCode);
		mDataMap.put("target_info", "");
		mDataMap.put("pay_type", "449746280025");
		mDataMap.put("return_type", "449748640003");
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
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").oneWhere("order_code,expected_return_hjycoin_money", "", "", "return_code", returnCode);
		BigDecimal hjycoin = new BigDecimal(returnGoods.get("expected_return_hjycoin_money"));
		
		// 使用了积分则添加积分返回的定时
		if(hjycoin.compareTo(BigDecimal.ZERO) > 0){
			String code = WebHelper.upCode("PTC");
			JobExecHelper.createExecInfo("449746990040", code, null);
			
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("pay_return_code", code);
			mDataMap.put("order_code", orderCode);
			mDataMap.put("target_info", returnCode);
			mDataMap.put("pay_type", "449746280025");
			mDataMap.put("return_type", "449748640004");
			mDataMap.put("return_money", hjycoin.toString());
			mDataMap.put("return_status", "449748090001");
			mDataMap.put("remark", StringUtils.trimToEmpty(remark));
			mDataMap.put("create_time", FormatHelper.upDateTime());
			mDataMap.put("update_time", mDataMap.get("create_time"));
			DbUp.upTable("oc_order_pay_return").dataInsert(mDataMap);
		}
	}
	
	/**
	 * 惠币变更
	 * @param flag
	 * @param money
	 * @param custId
	 * @param bigOrderCode
	 * @param orderCode
	 * @param type 20: 预估惠币，10：正式惠币
	 * @return
	 */
	public RootResult changeForHjycoin(UpdateCustAmtInput.CurdFlag flag, BigDecimal money, String custId, String bigOrderCode, String orderCode,String type){
		CustRelAmtRef beanRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		UpdateCustAmtInput input = new UpdateCustAmtInput();
		input.setCurdFlag(flag);
		input.setCustId(custId);
		input.setBigOrderCode(bigOrderCode);
		input.setHcoinStatCd(type);
		
		UpdateCustAmtInput.ChildOrder childOrder = new UpdateCustAmtInput.ChildOrder();
		childOrder.setAppChildOrdId(orderCode);
		childOrder.setChildHcoinAmt(money);
		input.getOrderList().add(childOrder);
		
		RootResult rootResult = beanRef.updateCustAmt(input);
		return rootResult;
	}
	
	/**
	 * 退还惠币
	 * @param orderCode 订单号
	 * @param flag  操作标识
	 * @param money 退还积分金额
	 * @return
	 */
	public RootResult returnForHjycoin(String orderCode, UpdateCustAmtInput.CurdFlag flag, BigDecimal money){
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("buyer_code,order_code,big_order_code", "", "", "order_code", orderCode);
		MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code", orderCode, "pay_type", "449746280025");
		
		String custId = orderPay.get("pay_remark");
		if(StringUtils.isBlank(custId)){
			custId = getCustId(orderInfo.get("buyer_code"));
		}
		
		return changeForHjycoin(flag, money, custId, orderInfo.get("big_order_code"), orderCode,"10");
	}
	
	/**
	 * 根据用户编号查询客代号
	 * @param memberCode
	 * @return
	 */
	public String getCustId(String memberCode){
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		return levelInfo.getCustId();
	}
	
}
