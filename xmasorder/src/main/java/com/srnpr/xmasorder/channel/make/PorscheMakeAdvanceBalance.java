package com.srnpr.xmasorder.channel.make;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.ChannelConst;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.service.PorscheOrderService;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.helper.PlusHelperScheduler;
import com.srnpr.xmassystem.modelproduct.PlusModelStockChange;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 扣减预存款
 * @remark 
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeAdvanceBalance extends PorscheTopOrderMake {

	private static final long TIMEOUT = 30000;
	
	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {

		long begin = System.currentTimeMillis();
		PorscheGtResult gtResult = new PorscheGtResult();
		boolean operationFlag = false; //操作标识 失败时回滚库存
		String channelSellerCode = porscheGtOrder.getChannelSellerCode();
		String lockKey = ChannelConst.operationMoneyStart + channelSellerCode;
		String lockCode = "";
		try {
			//锁定渠道商
			while("".equals(lockCode = KvHelper.lockCodes(10, lockKey))) {
				if((System.currentTimeMillis() - begin) > TIMEOUT) {
					gtResult.setResultCode(0);
					gtResult.setResultMessage("请求超时");
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					gtResult.setResultCode(2);
					gtResult.setResultMessage("系统异常");
				}
			}
			
			if(gtResult.upFlagTrue()) {
				//扣除之前 再次校验余额
				MDataMap channel = DbUp.upTable("uc_channel_sellerinfo").one("channel_seller_code", channelSellerCode);
				BigDecimal advanceBalance = new BigDecimal(channel.get("advance_balance"));
				BigDecimal orderMoney = porscheGtOrder.getOrderInfoUpper().getOrderMoney();
				if(advanceBalance.compareTo(orderMoney) >= 0) {
					// 扣减
					BigDecimal newAdvanceBalance = advanceBalance.subtract(orderMoney).setScale(2,RoundingMode.HALF_DOWN);
					DbUp.upTable("uc_channel_sellerinfo")
							.update(new MDataMap("zid", channel.get("zid"), "uid", channel.get("uid"),
									"channel_seller_code", channelSellerCode, "advance_balance",newAdvanceBalance.toString()));
					// 生成扣减日志
					String remark = "下单扣减，第三方订单号：" + porscheGtOrder.getOutOrderCode();
					new PorscheOrderService().insertChannelMoneyLog(channelSellerCode, "449748420001",
							orderMoney.toString(), newAdvanceBalance.toString(),
							porscheGtOrder.getOrderInfo().get(0).getOrderCode(), remark);
					operationFlag = true;
				}else {
					gtResult.setResultCode(0);
					gtResult.setResultMessage("渠道商账号余额不足");
				}
			}
		} finally {
			if(!"".equals(lockCode)) KvHelper.unLockCodes(lockCode, lockKey);
		}
		
		//无论啥原因导致扣款失败 回滚库存
		if(!operationFlag) rollbackStock(porscheGtOrder);
		
		return gtResult;
	}

	/**
	 * 回滚库存
	 * @param porscheGtOrder
	 */
	private void rollbackStock(PorscheGtOrder porscheGtOrder) {
		List<PorscheModelOrderDetail> orderDetails = porscheGtOrder.getOrderDetails();
		if(null != orderDetails && !orderDetails.isEmpty()) {
			for (PorscheModelOrderDetail orderDetail : orderDetails) {
				String skuCode = orderDetail.getSkuCode();
				String storeCodes = orderDetail.getStoreCode();
				String[] split = storeCodes.split("_");
				String storeCode = split[0];
				String num = split[1];
				
				//先回滚分库存
				XmasKv.upFactory(EKvSchema.SkuStoreStock).hincrBy(skuCode, storeCode, Long.parseLong(num));
				//后回滚总库存
				XmasKv.upFactory(EKvSchema.Stock).incrBy(skuCode, Long.parseLong(num));
				//生成异步将库存写入数据库任务
				PlusModelStockChange plusModelStockChange = new PlusModelStockChange();
				plusModelStockChange.setChangeCode("");
				plusModelStockChange.setChangeNumber(Integer.parseInt(num));
				plusModelStockChange.setCreateTime(DateHelper.upNow());
				plusModelStockChange.setSkuCode(skuCode);
				plusModelStockChange.setStoreCode(storeCode);
				PlusHelperScheduler.sendSchedler(EPlusScheduler.StockChangeLog,
						KvHelper.upCode(EPlusScheduler.StockChangeLog.toString()), plusModelStockChange);
			}
		}
	}

}
