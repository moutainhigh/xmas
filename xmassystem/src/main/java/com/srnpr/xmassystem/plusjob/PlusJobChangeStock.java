package com.srnpr.xmassystem.plusjob;

import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.modelproduct.PlusModelStockChange;
import com.srnpr.xmassystem.top.PlusConfigScheduler;
import com.srnpr.xmassystem.top.PlusTopScheduler;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basehelper.LogHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webface.IKvSchedulerConfig;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 定时将缓存中的库存变更同步到数据库
 * @author srnpr
 *
 */
public class PlusJobChangeStock extends PlusTopScheduler {

	public IBaseResult execByInfo(String sInfo) {
		//从redis中取key  zdkv-Scheduler-StockChangeLog
		//key: StockChangeLog2834399102
		//value: {"createTime":"2016-10-20 10:56:03","skuCode":"8019885565","changeCode":"DD4929879102","changeNumber":-1,"storeCode":"TDS1"}
		//key: StockChangeLog2834400102
		//value: {"createTime":"2016-10-20 10:56:29","skuCode":"150788","changeCode":"DD4929881102","changeNumber":-1,"storeCode":"C04"}
		PlusModelStockChange plusChange = new GsonHelper().fromJson(sInfo,
				new PlusModelStockChange());

		RootResultWeb result = new RootResultWeb();

		if (plusChange != null) {

			DbUp.upTable("sc_store_skunum")
					.dataExec(
							"update sc_store_skunum set stock_num=stock_num+:stock_num where sku_code=:sku_code and store_code=:store_code",
							new MDataMap("stock_num", String.valueOf(plusChange
									.getChangeNumber()), "sku_code", plusChange
									.getSkuCode(), "store_code", plusChange
									.getStoreCode()));

			LogHelper.addLog("stock_change", plusChange);
		}

		return result;
	}

	private final static PlusConfigScheduler plusConfigScheduler = new PlusConfigScheduler(
			EPlusScheduler.StockChangeLog);

	public IKvSchedulerConfig getConfig() {

		return plusConfigScheduler;
	}

}
