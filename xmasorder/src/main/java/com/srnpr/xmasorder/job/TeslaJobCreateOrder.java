package com.srnpr.xmasorder.job;

import com.srnpr.xmasorder.service.TeslaOrderService;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.modelnotice.PlusModelNoticeOrder;
import com.srnpr.xmassystem.top.PlusConfigScheduler;
import com.srnpr.xmassystem.top.PlusTopScheduler;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webface.IKvSchedulerConfig;

/***
 * 定时将缓存中的订单信息同步到数据库
 * 
 * @author jlin
 * 
 */
public class TeslaJobCreateOrder extends PlusTopScheduler {

	public IBaseResult execByInfo(String sInfo) {

		RootResultWeb result = new RootResultWeb();

//		PlusModelNoticeOrder pNoticeOrder = new GsonHelper().fromJson(sInfo,new PlusModelNoticeOrder());
		
		PlusModelNoticeOrder plusModelNoticeOrder = new GsonHelper().fromJson(sInfo, new PlusModelNoticeOrder());
		String orderCode = plusModelNoticeOrder.getOrderCode();

		boolean b = new TeslaOrderService().orderSaveToDB(orderCode);
		
		if (!b) {
			result.inErrorMessage(963903002, sInfo);
		}

		return result;
	}

	private final static PlusConfigScheduler plusConfigScheduler = new PlusConfigScheduler(
			EPlusScheduler.CreateOrder);

	public IKvSchedulerConfig getConfig() {

		return plusConfigScheduler;
	}

}
