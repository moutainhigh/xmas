package com.srnpr.xmasorder.channel.make;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 验证区域编码有效性
 * @remark 
 * @author 任宏斌
 * @date 2019年12月3日
 */
public class PorscheMakeCheckAreaCode extends PorscheTopOrderMake {

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		PorscheGtResult result = new PorscheGtResult();
		
		String areaCode = porscheGtOrder.getAddress().getAreaCode();
		if(areaCode != null && !"".equals(areaCode)) {
			int count = 0;
			if(areaCode.length() == 12) {
				count = DbUp.upTable("sc_tmp").count("code", areaCode, "code_lvl", "4", "use_yn", "Y", "send_yn", "Y");
			}else {
				count = DbUp.upTable("sc_tmp").count("code", areaCode, "use_yn", "Y", "send_yn", "Y");
			}
			if(count <= 0) {
				result.setResultCode(0);
				result.setResultMessage("区域编码不存在");
			}
		}
		
		return result;
	}

}
