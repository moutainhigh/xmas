package com.srnpr.xmasorder.make;

import java.util.List;

import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 拼好货逻辑处理
 * 
 * @author fq
 * 
 */
public class TeslaMakeSpellGoods extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) { 
		TeslaXResult result = new TeslaXResult();

		List<TeslaModelOrderActivity> activityList = teslaOrder.getActivityList();
		if(activityList!=null&&!activityList.isEmpty()){
			
			for (TeslaModelOrderActivity teslaModelOrderActivity : activityList) {
				
				if ("4497472600010016".equals(teslaModelOrderActivity.getActivityType())) {
					DbUp.upTable("oc_order_phh").dataInsert(new MDataMap("order_code",teslaModelOrderActivity.getOrderCode(),"activity_code",teslaModelOrderActivity.getActivityCode(),"flag_success","0"));
				}
			}
		}
		return result;
	}

}
