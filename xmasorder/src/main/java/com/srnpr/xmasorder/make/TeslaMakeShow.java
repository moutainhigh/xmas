package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TelsaModelOrderStockInfo;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapweb.helper.WebHelper;
/**
 * 展示信息
 * @author shiyz
 *
 */
public class TeslaMakeShow extends TeslaTopOrderMake {
	
	public static String notPayStatus = "4497153900010001";//下单成功未付款（在线支付）
	public static String PayStatus = "4497153900010002";//下单成功未发货（货到付款）
	public static String T_OLP = "449716200001";//在线支付
	public static String T_COD = "449716200002";//货到付款
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		BigDecimal	wgsMoney = BigDecimal.ZERO;//实际使用的微公社金额
		BigDecimal 	couponMoney = BigDecimal.ZERO;//实际使用的微公社金额
		BigDecimal  hjyBean = BigDecimal.ZERO;//实际使用惠豆金额
		boolean flagTheSea = false;//是否包含海外购商品
		int skuNum = teslaOrder.getOrderDetails().size();
		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {//确认订单页金额及提示信息
			TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(i);
			wgsMoney=wgsMoney.add(detail.getGroupPrice().multiply(BigDecimal.valueOf(detail.getSkuNum())));
			couponMoney=couponMoney.add(detail.getCouponPrice().multiply(BigDecimal.valueOf(detail.getSkuNum())));
			hjyBean = hjyBean.add(detail.getHjyBean().multiply(BigDecimal.valueOf(detail.getSkuNum())));
			if("1".equals(detail.getGiftFlag())&&"1".equals(teslaOrder.getShowGoods().get(i).getFlagTheSea())&&!flagTheSea){
				flagTheSea=true;
			}
			if("0".equals(detail.getGiftFlag())){
				skuNum--;
			}
		}
		BigDecimal 	cMoney = BigDecimal.ZERO;
		if(teslaOrder.getUse().getCoupon_Moneys() != null && teslaOrder.getUse().getCoupon_Moneys().size() > 0) {
			for(BigDecimal m : teslaOrder.getUse().getCoupon_Moneys()) {
				cMoney = cMoney.add(m);
			}
		}
		if(cMoney.compareTo(BigDecimal.ZERO)>0){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmasorder.coupon_name"));
			discount.setDis_type("0");
			discount.setDis_price(cMoney.compareTo(couponMoney)<0?cMoney.doubleValue():couponMoney.doubleValue());
			teslaOrder.getShowMoney().add(discount);
		}
		if(hjyBean.compareTo(HjybeanService.reverseHjyBeanToRMB( teslaOrder.getUse().getHjyBean() )) > 0 || wgsMoney.compareTo(teslaOrder.getUse().getWgs_money())>0||couponMoney.compareTo(cMoney)>0
				||teslaOrder.getUse().getMoral_character_money().doubleValue()>0)
		{
			TeslaModelDiscount discount = new TeslaModelDiscount();
			BigDecimal disLimit = BigDecimal.ZERO;
			discount.setDis_name(bConfig("xmasorder.coupon_name_limit"));
			discount.setDis_type("0");
			if(couponMoney.compareTo(cMoney)>0){
				disLimit = couponMoney.subtract(cMoney);
			}
			if(wgsMoney.compareTo(teslaOrder.getUse().getWgs_money())>0){
				disLimit=disLimit.add(wgsMoney.subtract(teslaOrder.getUse().getWgs_money()));
			}
			if(hjyBean.compareTo(HjybeanService.reverseHjyBeanToRMB( teslaOrder.getUse().getHjyBean() )) > 0){
				disLimit=disLimit.add(hjyBean.subtract(HjybeanService.reverseHjyBeanToRMB( teslaOrder.getUse().getHjyBean() )));
			}
			discount.setDis_price(disLimit.add(teslaOrder.getUse().getMoral_character_money()).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue());
			if(discount.getDis_price() > 0) {
				teslaOrder.getShowMoney().add(discount);
			}
		}
		if(flagTheSea){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name("关税");
			discount.setDis_type("1");
			teslaOrder.getShowMoney().add(discount);
		}
		BigDecimal allDueMoney = BigDecimal.ZERO;//应付款总额
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			allDueMoney=allDueMoney.add(teslaOrder.getSorderInfo().get(i).getDueMoney());
		}
		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
			TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(i);
			if("1".equals(detail.getGiftFlag())){
				BigDecimal pm = (detail.getSkuPrice().add(detail.getCouponPrice()).add(detail.getGroupPrice()).add(detail.getHjyBean())).multiply(BigDecimal.valueOf(detail.getSkuNum()));
				teslaOrder.getUorderInfo().setProductMoney(teslaOrder.getUorderInfo().getProductMoney().add(pm));
			}
		}
		if(teslaOrder.getUse().getHjycoin()>0){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name("惠币抵扣");
			discount.setDis_type("0");
			discount.setDis_price(teslaOrder.getUse().getHjycoin());
			teslaOrder.getShowMoney().add(discount);
		}
		if(teslaOrder.getUse().getZck_money()>0/*&&allDueMoney.compareTo(BigDecimal.ZERO)>0*/){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmasorder.zck_name"));
			discount.setDis_type("0");
			discount.setDis_price(teslaOrder.getUse().getZck_money());
			teslaOrder.getShowMoney().add(discount);
		}
		if(teslaOrder.getUse().getCzj_money()>0/*&&allDueMoney.compareTo(BigDecimal.ZERO)>0*/){
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmasorder.czj_name"));
			discount.setDis_type("0");
			discount.setDis_price(teslaOrder.getUse().getCzj_money());
			teslaOrder.getShowMoney().add(discount);
		}
		if(StringUtils.isNotEmpty(teslaOrder.getRedeemCode())) {
			TeslaModelDiscount discount = new TeslaModelDiscount();
			discount.setDis_name(bConfig("xmasorder.redeem_name"));
			discount.setDis_type("0");
			discount.setDis_price(Double.valueOf(teslaOrder.getUorderInfo().getProductMoney().toString()));
			teslaOrder.getShowMoney().add(discount);
		}
		//if(StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
		//	TeslaModelDiscount discount = new TeslaModelDiscount();
		//	discount.setDis_name(bConfig("xmasorder.farm_name"));
		//	discount.setDis_type("0");
		//	discount.setDis_price(Double.valueOf(teslaOrder.getUorderInfo().getDueMoney().toString()));
		//	teslaOrder.getShowMoney().add(discount);
		//}
		if((teslaOrder.getStatus().getExecStep() == ETeslaExec.Create||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate)
				&&allDueMoney.compareTo(teslaOrder.getCheck_pay_money())!=0&&!teslaOrder.isPointShop()&&StringUtils.isEmpty(teslaOrder.getRedeemCode())
				&&StringUtils.isEmpty(teslaOrder.getTreeCode())&&StringUtils.isEmpty(teslaOrder.getEventCode())){
			
			/*
			 * 如果是一元购订单、百度外卖导入、电视宝商城导入的订单则跳过验证  fq++
			 */
			String orderTypeStr = WebHelper.getImportOrderSource();
			if(!"449715200014".equals(teslaOrder.getUorderInfo().getOrderType()) && !orderTypeStr.contains(teslaOrder.getUorderInfo().getOrderType())) {
				
				result.setResultCode(963902248);
				result.setResultMessage(allDueMoney.toString());
				
			}
			//判断支付类型是否为空(为空也进行价格校验)
			if(StringUtils.isBlank(teslaOrder.getUorderInfo().getOrderType())) {
				result.setResultCode(963902248);
				result.setResultMessage(allDueMoney.toString());
			}
		}
		if (result.upFlagTrue()) {
			result = checkPayType(teslaOrder);
		}
		if(result.upFlagTrue()){
			for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
				TeslaModelShowGoods good = teslaOrder.getShowGoods().get(i);
				if("1".equals(good.getGiftFlag())&&good.getSkuNum()<good.getMiniOrder()){
					if(skuNum == 1) {
						result.setResultCode(916421266);
						result.setResultMessage(bInfo(916421266, good.getSkuName(),good.getMiniOrder()));
					} else {
						result.setResultCode(916421262);
						result.setResultMessage(bInfo(916421262, good.getSkuName(),good.getMiniOrder()));
					}					
					break;
				}
			}
		}
		/**
		 * 确认订单，如果是单件商品订单，返回库存信息
		 */
		if(teslaOrder.getStatus().getExecStep() == ETeslaExec.Confirm) {			
			if(skuNum == 1) {			
				TelsaModelOrderStockInfo stockInfo = new TelsaModelOrderStockInfo();
				TeslaModelShowGoods good = teslaOrder.getShowGoods().get(0); 
				stockInfo.setShowLimitNum(good.getShowLimitNum());
				stockInfo.setLimitBuy(Long.valueOf(good.getLimitBuy()).intValue());
				stockInfo.setMaxBuyCount(Long.valueOf(good.getMaxBuyCount()).intValue());
				stockInfo.setMiniOrder(Long.valueOf(good.getMiniOrder()).intValue());
				stockInfo.setStockNumSum(Long.valueOf(good.getStockNumSum()).intValue());
				
				// 兑换的最大商品数量固定是1
				if(StringUtils.isNotEmpty(teslaOrder.getRedeemCode()) && stockInfo.getMaxBuyCount() > 1) {
					stockInfo.setMaxBuyCount(1);
				}
				
				teslaOrder.setStockInfo(stockInfo);
			}
		}
		return result;
	}
	public TeslaXResult checkPayType(TeslaXOrder order){
		TeslaXResult result = new TeslaXResult();
		boolean flag = true;//是否可货到付款
		for (int i = 0; i < order.getShowGoods().size(); i++) {
			if((order.getShowGoods().get(i).isIs_activity()
					&&(!StringUtils.startsWith(order.getShowGoods().get(i).getSkuActivityCode(), "IC_SMG_")||"4497472600010006".equals(order.getShowGoods().get(i).getEventType())))
					||"Y".equals(order.getShowGoods().get(i).getValidateFlag())){
				if(!StringUtils.startsWithAny(order.getShowGoods().get(i).getSmallSellerCode(), "SI2003","SI2009")||"4497472600010006".equals(order.getShowGoods().get(i).getEventType())){
					flag=false;
					break;
				}
			}
		}
		
		// 再过滤一遍每个商品的设置，确认是否支持货到付款
		if(flag){
			Date now = new Date();
			Date start,end;
			for (TeslaModelOrderDetail detail : order.getOrderDetails()) {
				start = null;
				end = null;
				if("449747110002".equals(detail.getOnlinepayFlag())){
					// 不再判断时间，只要设置了仅支持在线支付就永久有效
					flag = false;
					break;
					
//					try {
//						start = DateUtils.parseDate(detail.getOnlinepayStart(), "yyyy-MM-dd HH:mm:ss");
//						end = DateUtils.parseDate(detail.getOnlinepayEnd(), "yyyy-MM-dd HH:mm:ss");
//					} catch (Exception e) {
//						//e.printStackTrace();
//					}
//					
//					// 生效时间未设置则不生效
//					if(start == null || end == null){
//						continue;
//					}
//					
//					// 如果设置了仅支持在线支付且当前时间在生效时间内，则当前订单不支持货到付款
//					if(start.before(now) && end.after(now)){
//						flag = false;
//						break;
//					}
				}
			}
		}
		
		if(order.getStatus().getExecStep() == ETeslaExec.Confirm){//确认订单时
			order.getUorderInfo().setPayType(flag?T_COD:T_OLP);
		}else if (StringUtils.isNotBlank(order.getUorderInfo().getPayType())
				&&!flag&&T_COD.equals(order.getUorderInfo().getPayType())
				&& (order.getStatus().getExecStep() == ETeslaExec.Create || order.getStatus().getExecStep() == ETeslaExec.PCCreate)) {//支付方式与商品的支付方式冲突时
				result.setResultCode(963901133);
				result.setResultMessage(bInfo(963901133));
		}
		if (result.upFlagTrue()) {
			for (int i = 0; i < order.getSorderInfo().size(); i++) {
				if(T_COD.equals(order.getUorderInfo().getPayType())||order.getSorderInfo().get(i).getDueMoney().compareTo(BigDecimal.ZERO)==0){
					order.getSorderInfo().get(i).setOrderStatus(PayStatus);
				}
			}
		}
		return result;
	}
}
