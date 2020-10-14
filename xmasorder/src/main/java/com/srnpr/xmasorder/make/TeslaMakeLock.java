package com.srnpr.xmasorder.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapdata.helper.KvHelper;
/**
 * 锁定各种需要锁定的方法
 * @author shiyz
 *
 */
public class TeslaMakeLock extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		
	   TeslaXResult result = new TeslaXResult();
	   //只有创建订单时锁
	   if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Create||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate){
	   //锁定用户
	   if(!"449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())){//多彩下单不锁定用户
		   
	       String sLockKey=KvHelper.lockCodes(10, teslaOrder.getUorderInfo().getBuyerCode());
			if(StringUtils.isBlank(sLockKey)){
				result.inErrorMessage(963902124);
				return result;
			}
	   }	   
	}
	   
	   return result;
	}
}
