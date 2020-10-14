package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelGroupPayInput;
import com.srnpr.xmasorder.model.TeslaModelGroupPayResult;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrInput;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrResult;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.websupport.ApiCallSupport;

/**
 * 活动扣减(券,微公社等钱类相关的扣减)
 * 
 * @author xiegj
 * 
 */
public class TeslaMakeActivity extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		Map<String, BigDecimal> couponCodeMap = new HashMap<String, BigDecimal>(); // key:coupon_code;value:money
		for (int k = 0; k < teslaOrder.getOcOrderPayList().size(); k++) {// 微公社扣减
			TeslaModelOrderPay pay = teslaOrder.getOcOrderPayList().get(k);
			if ("449746280009".equals(pay.getPayType())
					&& StringUtils.isNotBlank(pay.getPayRemark())
					&& !"0".equals(pay.getPayRemark())
					&& Double.valueOf(pay.getPayRemark()) > 0) {
				/*
				 * 
				 * PlusModelGroupMoneyChange plusModelGroupMoneyChange = new
				 * PlusModelGroupMoneyChange();
				 * plusModelGroupMoneyChange.setChangeMoney
				 * (BigDecimal.valueOf(Double.valueOf(pay.getPayRemark())));
				 * plusModelGroupMoneyChange
				 * .setChangeOrderCode(pay.getOrderCode());
				 * plusModelGroupMoneyChange.setCreateTime(DateHelper.upNow());
				 * plusModelGroupMoneyChange
				 * .setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
				 * plusModelGroupMoneyChange
				 * .setManageCode(teslaOrder.getUorderInfo().getSellerCode());
				 * PlusHelperScheduler .sendSchedler(
				 * EPlusScheduler.GroupMoneyChangeLog,
				 * KvHelper.upCode(EPlusScheduler.GroupMoneyChangeLog
				 * .toString()), plusModelGroupMoneyChange);
				 */

				TeslaModelGroupPayInput gi = new TeslaModelGroupPayInput();
				gi.setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
				gi.setOrderCode(pay.getOrderCode());
				gi.setOrderCreateTime(DateHelper.upNow());
				gi.setTradeMoney(BigDecimal.valueOf(
						Double.valueOf(pay.getPayRemark())).toString());

				ApiCallSupport<TeslaModelGroupPayInput, TeslaModelGroupPayResult> apiCallSupport = new ApiCallSupport<TeslaModelGroupPayInput, TeslaModelGroupPayResult>();
				TeslaModelGroupPayResult gr = new TeslaModelGroupPayResult();
				try {
					gr = apiCallSupport.doCallApi(
							bConfig("xmassystem.group_pay_url"),
							bConfig("xmassystem.group_pay_face"),
							bConfig("xmassystem.group_pay_key"),
							bConfig("xmassystem.group_pay_pass"), gi,
							new TeslaModelGroupPayResult());
				} catch (Exception e) {
					result.inErrorMessage(963902217);
				}

				// TeslaModelGroupPayResult gr = new
				// GroupPayService().GroupPay(gi, plusChange.getManageCode());
				if (gr.upFlagTrue()) {

					pay.setPaySequenceid(gr.getTradeCode());

				} else {
					result.inOtherResult(gr);
				}

			} else if ("449746280002".equals(pay.getPayType())) {
				//优惠券扣减放到订单信息持久化后操作 -rhb 20181119
//				if (couponCodeMap.containsKey(pay.getPaySequenceid())) {
//					couponCodeMap.put(
//							pay.getPaySequenceid(),
//							couponCodeMap.get(pay.getPaySequenceid()).add(
//									pay.getPayedMoney()));
//				} else {
//					couponCodeMap.put(pay.getPaySequenceid(),
//							pay.getPayedMoney());
//				}
			} else if("449746280015".equals(pay.getPayType())) {//扣减惠豆
				
				String crntSysTime = DateHelper.upNow();
				BigDecimal useBean = HjybeanService.reverseRMBToHjyBean(pay.getPayedMoney());
				
				HjyBeanCtrInput input = new HjyBeanCtrInput();//惠豆扣减输入参数
				input.setAmount(useBean.intValue());//积分金额
				input.setInout(2);//资金方向   2：出
				input.setMemo(pay.getOrderCode());//备注
				input.setOrderdate(crntSysTime);//订单日期
				input.setTradetype(5);//操作类型  5、	购物使用惠豆
				HjyBeanCtrResult ctrResult = new HjybeanService().ctrhjyBeanByMemberCode(teslaOrder.getUorderInfo().getBuyerCode(), input);
				if(null != ctrResult && ctrResult.getResultCode() == 1) {
					DbUp.upTable("fh_hd_change_detail").insert(
							"change_type","449747940002",
							"info",pay.getOrderCode(),//订单号
							"change_amount",String.valueOf(useBean.intValue()),//消费的惠豆数
							"remark","TeslaMakeActivityCreateOrder",//创建订单使用惠豆
							"serialno",ctrResult.getCenterserialno(),//支付流水号
							"trade_time",DateHelper.upNow(),//支付接口返回时间(和创建时间有时差)
							"create_time",crntSysTime//创建时间
							);
				} else {
					result.inOtherResult(ctrResult);
				}
			}
		}
		
		//优惠券扣减放到订单信息持久化后操作 -rhb 20181119
//		for (String couponCode : couponCodeMap.keySet()) {// 优惠券扣减
//			MDataMap updateMap = new MDataMap();
//			updateMap.put("coupon_code", couponCode);
//			updateMap.put("status", "1");
//			updateMap.put("surplus_money", "0");
//			updateMap.put("update_time", DateHelper.upNow());
//			updateMap.put("last_mdf_id", "app");
//			
//			Map<String, Object> map = DbUp.upTable("oc_coupon_info").dataSqlOne("select ct.money_type from oc_coupon_type ct,oc_coupon_info ci where ci.coupon_type_code = ct.coupon_type_code and ci.coupon_code = :couponCode", new MDataMap("couponCode",couponCode));
//			String sSql = "update oc_coupon_info set last_mdf_id=:last_mdf_id,status=:status,update_time=:update_time,surplus_money=(case when initial_money-:surplus_money < 0 then 0 else initial_money-:surplus_money end  ) where coupon_code=:coupon_code";
//			if(map != null && "449748120002".equals(map.get("money_type"))){
//				// 折扣券特殊处理，剩余金额值固定不变
//				sSql = "update oc_coupon_info set last_mdf_id=:last_mdf_id,status=:status,update_time=:update_time where coupon_code=:coupon_code";
//			}
//			
//			DbUp.upTable("oc_coupon_info").dataExec(sSql, updateMap);
//		}

		return result;
	}

}
