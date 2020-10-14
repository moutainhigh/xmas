package com.srnpr.xmasorder.channel.service;

import java.util.Map;
import java.util.UUID;

import com.srnpr.xmasorder.channel.model.ChannelConst;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 订单处理
 * @remark 
 * @author 任宏斌
 * @date 2019年12月2日
 */
public class PorscheOrderService {

	/**
	 * 操作渠道商预存款 生成日志
	 * @param channelSellerCode 渠道商编号
	 * @param operationType 操作类型
	 * @param operationMoney 操作金额
	 * @param advanceBalance 操作后预存款余额
	 * @param triggerCode 引起预存款变动的编号
	 */
	public void insertChannelMoneyLog(String channelSellerCode, String operationType, String operationMoney,
			String advanceBalance, String triggerCode, String remark) {
		// 生成扣减日志
		DbUp.upTable("lc_operation_channel_money").insert("uid", UUID.randomUUID().toString().replace("-", ""),
				"channel_seller_code", channelSellerCode, "operation_type", operationType, "operation_money",
				operationMoney, "advance_balance", advanceBalance, "operation_time", FormatHelper.upDateTime(),
				"trigger_code", triggerCode, "remark", remark);
	}
	
	/**
	 * 取消订单 返还预存款
	 * @param channelSellerCode 渠道商编号
	 * @param money 待返还的钱
	 * @return
	 */
	public MWebResult cancelOrderReturnAdvanceBalance(String channelSellerCode, String money) {
		long begin = System.currentTimeMillis();
		MWebResult result = new MWebResult();
		final long outtime = 30000;
		
		String key = ChannelConst.operationMoneyStart+channelSellerCode;
		String addLock = "";
		try {
			while("".equals(addLock = KvHelper.lockCodes(10, key))) {
				if(System.currentTimeMillis() - begin > outtime) {
					result.setResultCode(2);
					result.setResultMessage("取消订单成功，返还预存款失败");
					return result;
				}
			}
			
			String sSql = "UPDATE usercenter.uc_channel_sellerinfo "
					+ "SET advance_balance = advance_balance + :advance_balance WHERE channel_seller_code=:channel_seller_code";
			MDataMap params = new MDataMap("advance_balance", money, "channel_seller_code", channelSellerCode);
			int dataExec = DbUp.upTable("mc_login_info").dataExec(sSql, params);
			if(dataExec != 1) {
				result.setResultCode(2);
				result.setResultMessage("取消订单成功，返还预存款失败");
			}
		} finally {
			if(!"".equals(addLock)) KvHelper.unLockCodes(addLock, key);
		}
		return result;
	}
	
	public boolean checkChannelOrder(String orderCode) {
		MDataMap one = DbUp.upTable("oc_order_channel").one("order_code", orderCode);
		return null != one;
	}
	
	public String getChannelNameByOrder(String orderCode) {
		String channelName = "";
		
		String sql = "select uc.channel_seller_name FROM ordercenter.oc_order_channel oc "
				+ "LEFT JOIN usercenter.uc_channel_sellerinfo uc ON oc.channel_seller_code=uc.channel_seller_code "
				+ "WHERE oc.order_code=:order_code";
		Map<String, Object> channel = DbUp.upTable("oc_order_channel").dataSqlOne(sql, new MDataMap("order_code", orderCode));
		if(null != channel) {
			channelName = channel.get("channel_seller_name")+"";
		}
		
		return channelName;
	}
}
