package com.srnpr.xmasorder.channel.make;

import java.math.BigDecimal;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 渠道商状态、余额校验
 * @remark 
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeCheckChannel extends PorscheTopOrderMake {

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		
		PorscheGtResult gtResult = new PorscheGtResult();
		String channelSellerCode = porscheGtOrder.getChannelSellerCode();
		MDataMap channel = DbUp.upTable("uc_channel_sellerinfo").one("channel_seller_code", channelSellerCode);
		if(null != channel) {
			//判断是否冻结
			String freezeStatus = channel.get("freeze_status");
			if("4497471600090002".equals(freezeStatus)) {
				gtResult.setResultCode(0);
				gtResult.setResultMessage("渠道商账号已冻结");
				return gtResult;
			}
			
			//判断余额
			BigDecimal allProductMoney = BigDecimal.ZERO;
			for(PorscheModelOrderDetail detail : porscheGtOrder.getOrderDetails()) {
				allProductMoney = allProductMoney.add(detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())));
			}
			BigDecimal advanceBalance = new BigDecimal(channel.get("advance_balance"));
			if(allProductMoney.compareTo(advanceBalance) > 0) {
				gtResult.setResultCode(0);
				gtResult.setResultMessage("渠道商账号余额不足");
				return gtResult;
			}
		}
		return gtResult;
	}
	
}
