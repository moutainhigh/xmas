package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 计算积分赋予 / 计算惠币赋予
 * 所有优惠以及金额抵扣都计算完成后，最后计算一下赋予的积分数
 */
public class TeslaMakeGiveIntegral  extends TeslaTopOrderMake {

	HjycoinService hjycoinService = new HjycoinService();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult xResult = new TeslaXResult();
		String orderSource = teslaOrder.getUorderInfo().getOrderSource();
		int count = DbUp.upTable("oc_import_define").count("order_source",orderSource);
		//多彩宝创建订单 跳过此环节 -rhb 20180813   渠道商订单和商家采购订单(待添加)
		if(count > 0 ||"449715190014".equals(orderSource) || "449715190034".equals(orderSource)) {
			return xResult;
		}
		
		List<TeslaModelOrderDetail> detailList = teslaOrder.getOrderDetails();
		
		PlusServiceAccm accm = new PlusServiceAccm();
		
		Set<String> hjycoinOrderCodeList = new HashSet<String>();
		for(TeslaModelOrderDetail detail : detailList){
			// 忽略赠品和不赋予积分的商品
			if(!"1".equals(detail.getGiftFlag()) 
					|| "N".equalsIgnoreCase(detail.getAccmYn())
					|| "SI2003".equalsIgnoreCase(detail.getSmallSellerCode())
					|| teslaOrder.isPointShop()){
				continue;
			}
			
			// 参与秒杀活动的商品不赋予积分
			boolean giveFlag = true;
			for(TeslaModelShowGoods goods : teslaOrder.getShowGoods()){
				if(goods.getSkuCode().equals(detail.getSkuCode())){
					if("4497472600010001".equalsIgnoreCase(goods.getEventType())){
						giveFlag = false;
						break;
					}
				}
			}
			
			if(!giveFlag){
				continue;
			}
			
			// 忽略成本价为0的商品
			if(detail.getCostPrice().compareTo(BigDecimal.ZERO) <= 0){
				continue;
			}
			
			BigDecimal priceMoney = detail.getSkuPrice();
			BigDecimal payMoney = priceMoney.subtract(detail.getIntegralMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP)); // 实付金额不包含积分抵扣的金额
			payMoney = payMoney.subtract(detail.getHjycoin().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP)); // 实付金额不包含惠币抵扣的金额
			BigDecimal czjMoney = detail.getCzjMoney().divide(new BigDecimal(detail.getSkuNum()),2,BigDecimal.ROUND_HALF_UP); //  储值金金额
			BigDecimal costMoney = detail.getCostPrice();
			
			// 每件商品赋予多少积分
			int giveIntegralNum = 0;
			if(new BigDecimal(100).compareTo(priceMoney) < 0) {
				giveIntegralNum = accm.productForAccmAmt(priceMoney, payMoney, czjMoney, costMoney, detail.getSmallSellerCode());
			}					
			// 每件商品赋予的积分金额，保留三位小数
			BigDecimal singleSkuIntegralMoney = accm.accmAmtToMoney(new BigDecimal(giveIntegralNum), 3);
			
			// 记录下商品赋予的总积分金额
			if(singleSkuIntegralMoney.compareTo(BigDecimal.ZERO) > 0){
				if(hjycoinService.checkFlagEnabled() && hjycoinService.checkGiveEnabled(teslaOrder.getUorderInfo().getOrderSource())) {
					// 如果惠币标识启用则赋予惠币
					detail.setGiveHjycoin(singleSkuIntegralMoney.multiply(new BigDecimal(detail.getSkuNum())));
					
					// 如果是商户品则需要插入预估收益的定时
					if(!"SI2003".equals(detail.getSmallSellerCode())) {
						hjycoinOrderCodeList.add(detail.getOrderCode());
					}
				} else {
					// 否则继续正常赋予积分
					detail.setGiveIntegralMoney(singleSkuIntegralMoney.multiply(new BigDecimal(detail.getSkuNum())));
				}
			}

		}
		
		// 插入惠币预估收益定时
		for(String code : hjycoinOrderCodeList) {
			teslaOrder.getExecInfoMap().put("449746990035", code);
		}
		
		return xResult;
	}
	

}
