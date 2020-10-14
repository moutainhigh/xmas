package com.srnpr.xmasorder.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadBlackList;
import com.srnpr.xmassystem.modelproduct.PlusModelBlackList;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;

/**
 * 黑名单校验
 * 
 * @author xiegj
 *
 */
public class TeslaMakeBlacklisted extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		boolean flag = false;//是否黑名单成员
		TeslaXResult result = new TeslaXResult();
		if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getBuyerMobile())){
			PlusModelProductQuery query = new PlusModelProductQuery("all");
			PlusModelBlackList list = new LoadBlackList().upInfoByCode(query);
			if(list!=null&&list.getMobiles()!=null&&!list.getMobiles().isEmpty()){
				for (int i = 0; i < list.getMobiles().size(); i++) {
					if(teslaOrder.getUorderInfo().getBuyerMobile().equals(list.getMobiles().get(i))
							||teslaOrder.getAddress().getMobilephone().equals(list.getMobiles().get(i))){
						flag=true;
						break;
					}
				}
			}
		}
		if (flag) {
			result.setResultCode(963904006);
			result.setResultMessage(bInfo(963904006));
		}
		return result;
	}

}
