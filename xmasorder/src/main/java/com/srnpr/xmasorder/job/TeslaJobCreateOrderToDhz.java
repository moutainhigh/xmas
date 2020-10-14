package com.srnpr.xmasorder.job;

import com.srnpr.xmasorder.service.TeslaOrderServiceDuohz;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

public class TeslaJobCreateOrderToDhz extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		
		String onOff = bConfig("xmasorder.dhz_create_order_switch");
		if(!"on".equals(onOff)) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("开关未开启");
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
				//将同步次数改为1，防止长时间未拼团成功。此订单通知次数用完之后，不再通知。
				DbUp.upTable("za_exectimer").dataUpdate(new MDataMap("exec_info", sInfo,"exec_type","449746990018" ,"exec_number","1","remark",""),"exec_number,remark","exec_info,exec_type");
				//操作失败标识
				mWebResult.setResultCode(66);
				return mWebResult;
			}
		}
		TeslaOrderServiceDuohz teslaDuohzOrderService = new TeslaOrderServiceDuohz();
		
		mWebResult = teslaDuohzOrderService.createOrder(sInfo);
		
		return mWebResult;
	}

	
	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990018");
		config.setMaxExecNumber(30);
		return config;
	}

}
