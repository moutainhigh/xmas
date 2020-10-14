package com.srnpr.xmasorder.make;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 校验地址编码是否为四级(5.4.2版本以前，地址编码为三级，以后版本为四级)
 */
public class TeslaMakeCheckCodeLevel extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		
		String version = teslaOrder.getUorderInfo().getAppVersion();
		String areaCode = teslaOrder.getAddress().getAreaCode();
		if(areaCode != null && !"".equals(areaCode)) {
			if(AppVersionUtils.compareTo(version, "5.4.2") >= 0) {
				int count = DbUp.upTable("sc_tmp").count("code", areaCode, "code_lvl", "4", "use_yn", "Y", "send_yn", "Y");
				if(count <= 0) {
					xResult.setResultCode(963910001);
					xResult.setResultMessage(bInfo(963910001));
					return xResult;
				}
			}else {
				int count = DbUp.upTable("sc_tmp").count("code", areaCode, "use_yn", "Y", "send_yn", "Y");
				if(count <= 0) {
					xResult.setResultCode(963910001);
					xResult.setResultMessage(bInfo(963910001));
					return xResult;
				}
			}
		}
		
		return xResult;
	}

}
