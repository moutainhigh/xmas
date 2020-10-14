package com.srnpr.xmasorder.job;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.service.TeslaOrderServiceJD;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 京东商品订单 定时向京东下单
 * @remark 
 * @author 任宏斌
 * @date 2019年5月14日
 */
public class TeslaJobCreateOrderToJd extends RootJobForExec {

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		
		String onOff = bConfig("xmasorder.jd_create_order_switch");
		if(!"on".equals(onOff)) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("开关未开启");
			return mWebResult;
		}
		
		// 异步下单时锁定一下订单，防止订单在下单成功的同时订单又被用户取消的情况
		String lockKey = KvHelper.lockCodes(20, Constants.LOCK_ORDER_UPDATE + sInfo);
		if(StringUtils.isBlank(lockKey)) {
			// 订单正在操作中，请稍候重试！
			mWebResult.setResultCode(918590001);
			mWebResult.setResultMessage(TopUp.upLogInfo(918590001));
			return mWebResult;
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", sInfo);
		if ("4497153900010001".equals(orderInfo.get("order_status"))) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("不是已付款状态：" + orderInfo.get("order_status"));
			return mWebResult;
		}
		if ("4497153900010006".equals(orderInfo.get("order_status"))) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单已取消!");
			return mWebResult;
		}
		
		//根据订单号查询是否是拼团单。
		MDataMap groupOrderMap = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",sInfo);
		if(groupOrderMap != null && !groupOrderMap.isEmpty()){//不为空时，证明是拼团单，然后检查是否已经拼团成功。
			String collageCode = groupOrderMap.get("collage_code");
			MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
			//判断此团是否拼团成功
			String collageStatus = collageInfo.get("collage_status");
			if(!"449748300002".equals(collageStatus)){//非拼团成功的订单做以下操作
				//操作失败标识
				mWebResult.setResultCode(99);
				return mWebResult;
			}
		}
		
		TeslaOrderServiceJD teslaOrderServiceJD = new TeslaOrderServiceJD();
		
		mWebResult =  teslaOrderServiceJD.createOrder(sInfo);
		//只有接口调用异常 才重试
		if(mWebResult.getResultCode() != 99) {
			mWebResult.setResultCode(1);
		}
		
		// 操作执行完成解除锁定
		KvHelper.unLockCodes(lockKey, Constants.LOCK_ORDER_UPDATE + sInfo);
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990017");
		config.setMaxExecNumber(30);
		return config;
	}

}
