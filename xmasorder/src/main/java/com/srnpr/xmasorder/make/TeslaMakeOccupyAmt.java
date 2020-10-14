package com.srnpr.xmasorder.make;

import com.srnpr.xmasorder.enumer.CrudFlagEnum;
import com.srnpr.xmasorder.service.TeslaEmployAmtService;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 储值金、暂存款占用
 * @author pangjh
 *
 */
public class TeslaMakeOccupyAmt  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();
		
		if(teslaOrder.getUse().getCzj_money() > 0 || teslaOrder.getUse().getZck_money() > 0){
			
			TeslaEmployAmtService employAmtService = new TeslaEmployAmtService();

			MDataMap mDataMap = new MDataMap();

			mDataMap.put("crud_flag", CrudFlagEnum.C.name());
			
			boolean flag = employAmtService.doProcess(mDataMap,teslaOrder);

			if (flag) {

				teslaOrder.getUse().setAmt_status("1");
				
			}else {
				xResult.setResultCode(963902221);
				xResult.setResultMessage(bInfo(963902221));
			}
			
		}

		return xResult;
	}
	
	

}
