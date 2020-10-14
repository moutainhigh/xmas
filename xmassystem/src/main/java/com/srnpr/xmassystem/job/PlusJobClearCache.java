package com.srnpr.xmassystem.job;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.plusconfig.PlusStaticClearCache;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.LogSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

public class PlusJobClearCache extends RootJob {

	int iSum = 0;

	public void doExecute(JobExecutionContext context) {

		PlusStaticClearCache pluseCache = new PlusStaticClearCache();

		String sDateString = WebHelper.upStaticValue(pluseCache);

		/*
		 * for (MDataMap map : DbUp.upTable("oc_orderinfo").queryAll(
		 * "order_code,big_order_code", "", "big_order_code='OS1186370102'", new
		 * MDataMap())) {
		 * 
		 * clearOrder(map);
		 * 
		 * }
		 */

		// 清除一个月前的订单
		for (MDataMap map : DbUp.upTable("oc_orderinfo").queryAll(
				"order_code,big_order_code",
				"",
				"(create_time>'" + sDateString + "' and create_time<'"
						+ DateHelper.upDateTimeAdd("-30d")
						+ "' ) and seller_code in('SI2003','SI3003')",
				new MDataMap())) {

			clearOrder(map);

		}

		LogSupport.getInstance().sendLogToServer();
		WebHelper.updateStaticValue(pluseCache,
				DateHelper.upDateTimeAdd("-40d"));
	}

	private void clearOrder(MDataMap map) {
		String sOrderInfo = XmasKv.upFactory(EKvSchema.CreateOrder).get(
				map.get("big_order_code"));
		if (StringUtils.isNotBlank(sOrderInfo)) {
			LogSupport.getInstance().addLog("order_info", sOrderInfo);
			XmasKv.upFactory(EKvSchema.CreateOrder).del(
					map.get("big_order_code"));

		}

		
		if (!XmasKv.upFactory(EKvSchema.LogOrderStock).hgetAll(map.get("order_code")).isEmpty()) {

			XmasKv.upFactory(EKvSchema.LogOrderStock)
					.del(map.get("order_code"));
		}

		String sPayForm = XmasKv.upFactory(EKvSchema.PayFrom).get(
				map.get("big_order_code"));
		if (StringUtils.isNotBlank(sPayForm)) {

			XmasKv.upFactory(EKvSchema.PayFrom).del(map.get("big_order_code"));

		}
		String sTimeCancelOrder = XmasKv.upFactory(EKvSchema.TimeCancelOrder)
				.get(map.get("order_code"));
		if (StringUtils.isNotBlank(sTimeCancelOrder)) {

			XmasKv.upFactory(EKvSchema.TimeCancelOrder).del(
					map.get("order_code"));
		}

		checkLog();

	}

	private void checkLog() {
		iSum++;
		if (iSum % 800 == 0) {
			LogSupport.getInstance().sendLogToServer();
		}
	}

}
