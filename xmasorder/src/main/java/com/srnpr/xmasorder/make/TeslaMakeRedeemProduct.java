package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 兑换码兑换
 * @remark 
 * @author 任宏斌
 * @date 2019年7月9日
 */
public class TeslaMakeRedeemProduct extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		if(StringUtils.isNotEmpty(teslaOrder.getRedeemCode())) {
			//活动编号非空校验
			if(StringUtils.isEmpty(teslaOrder.getActivityCode())) {
				result.setResultCode(916424000);
				result.setResultMessage(bInfo(916424000));
				return result;
			}
			
			String sSql = "select a.activity_name,a.activity_type,r.is_redeem,a.begin_time,a.end_time,a.flag from oc_coupon_redeem r,oc_activity a "
					+ "where r.activity_code=a.activity_code and r.member_code=:member_code and r.activity_code=:activity_code "
					+ "and r.activity_cdkey=:activity_cdkey ";
			Map<String, Object> couponRedeem = DbUp.upTable("oc_coupon_redeem").dataSqlOne(sSql, new MDataMap("member_code", teslaOrder.getUorderInfo().getBuyerCode(), 
					"activity_code", teslaOrder.getActivityCode(), "activity_cdkey", teslaOrder.getRedeemCode()));
			if(null!= couponRedeem) {
				if("0".equals(couponRedeem.get("flag")+"")) {
					result.setResultCode(916424001);
					result.setResultMessage(bInfo(916424001));
					return result;
				}
				
				if(DateUtil.compareDate1(couponRedeem.get("begin_time")+"", DateUtil.getSysDateTimeString())) {
					result.setResultCode(916424002);
					result.setResultMessage(bInfo(916424002));
					return result;
				}
				
				if(DateUtil.compareDate1(DateUtil.getSysDateTimeString(), couponRedeem.get("end_time")+"")) {
					result.setResultCode(916424003);
					result.setResultMessage(bInfo(916424003));
					return result;
				}
				
				if(!"1".equals(couponRedeem.get("is_redeem")+"")) {
					
					if(teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
						//标识使用
						DbUp.upTable("oc_coupon_redeem").dataUpdate(new MDataMap("is_redeem","1", "member_code", teslaOrder.getUorderInfo().getBuyerCode(), 
								"activity_code", teslaOrder.getActivityCode(), "activity_cdkey", teslaOrder.getRedeemCode()), 
								"is_redeem", "member_code,activity_code,activity_cdkey");
					}
					
					//生成支付信息
					TeslaModelOrderPay op = new TeslaModelOrderPay();
					op.setPaySequenceid(teslaOrder.getRedeemCode());
					op.setMerchantId(teslaOrder.getUorderInfo().getBuyerCode());
					op.setOrderCode(teslaOrder.getSorderInfo().get(0).getOrderCode());//兑换码兑换 一货一单
					op.setPayedMoney(teslaOrder.getSorderInfo().get(0).getProductMoney());
					op.setPayType("449746280021");
					teslaOrder.getOcOrderPayList().add(op);
					
					//添加活动信息
					TeslaModelOrderActivity oa = new TeslaModelOrderActivity();
					oa.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
					oa.setActivityCode(teslaOrder.getActivityCode());
					oa.setActivityName(couponRedeem.get("activity_name")+"");
					oa.setActivityType(couponRedeem.get("activity_type")+"");
					oa.setTicketCode(teslaOrder.getRedeemCode());
					oa.setOrderCode(teslaOrder.getSorderInfo().get(0).getOrderCode());//兑换码兑换 一货一单
					oa.setPreferentialMoney(teslaOrder.getOrderDetails().get(0).getSkuPrice());
					oa.setProductCode(teslaOrder.getShowGoods().get(0).getProductCode());
					oa.setSkuCode(teslaOrder.getShowGoods().get(0).getSkuCode());
					teslaOrder.getActivityList().add(oa);
					
					//处理商品展示信息
					teslaOrder.getShowGoods().get(0).setIsSkuPriceToBuy("1");
					teslaOrder.getShowGoods().get(0).setEventType("");
					teslaOrder.getShowGoods().get(0).setEventCode("");
					
					//处理订单详情
					teslaOrder.getOrderDetails().get(0).setCouponPrice(teslaOrder.getOrderDetails().get(0).getSkuPrice());
					teslaOrder.getOrderDetails().get(0).setSkuPrice(BigDecimal.ZERO);//售后退款时用这个字段 
					
					//处理订单信息
					teslaOrder.getSorderInfo().get(0).setDueMoney(teslaOrder.getSorderInfo().get(0).getTransportMoney());//应付款即为运费
					teslaOrder.getSorderInfo().get(0).setOrderMoney(BigDecimal.ZERO);//取消发货时用这个字段
					//处理订单状态
					if(teslaOrder.getSorderInfo().get(0).getDueMoney().compareTo(BigDecimal.ZERO) == 0) {
						teslaOrder.getSorderInfo().get(0).setOrderStatus("4497153900010002");
					}
					
				}else {
					result.setResultCode(916424205);
					result.setResultMessage(bInfo(916424205, teslaOrder.getRedeemCode()));
				}
			}else {
				result.setResultCode(916424104);
				result.setResultMessage(bInfo(916424104, teslaOrder.getRedeemCode()));
			}
		}
		return result;
	}
}
