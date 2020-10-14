package com.srnpr.xmasorder.make;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 特殊促销活动校验 目前仅针对投票换购
 * @remark 
 * @author 任宏斌
 * @date 2019年7月9日
 * 
 * @editor 周恩至
 * @date 2020年05月08号
 * 橙意卡零毛利活动需要用到eventTCode，活动类型：4497472600010032，需要校验用户是不是橙意卡用户。
 */
public class TeslaMakeSpecialEvent extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		if(StringUtils.isNotEmpty(teslaOrder.getEventCode())) {
			String eventCode = teslaOrder.getEventCode();
			MDataMap mmap = DbUp.upTable("sc_event_info").one("event_code",eventCode);
			String eventType = "";
			if(mmap != null && !mmap.isEmpty()) {
				eventType = mmap.get("event_type_code");
			}
			if(eventType.equals("4497472600010029")) {
				// 判断用户是否参与过
				String sql = "SELECT count(1) count FROM ordercenter.oc_order_activity oa LEFT JOIN ordercenter.oc_orderinfo oi ON oa.order_code=oi.order_code " + 
						" WHERE oa.activity_code=:activity_code AND oi.buyer_code=:buyer_code";
				Map<String, Object> countMap = DbUp.upTable("oc_order_activity")
						.dataSqlOne(sql, new MDataMap("activity_code", teslaOrder.getEventCode(), "buyer_code", teslaOrder.getUorderInfo().getBuyerCode()));
				if(MapUtils.getInteger(countMap, "count") > 0) {
					result.setResultCode(916426001);
					result.setResultMessage(bInfo(916426001));
					return result;
				}
			}else if(eventType.equals("4497472600010032")) {
				String memberCode = teslaOrder.getUorderInfo().getBuyerCode();
				//校验用户是否是否是橙意卡用户
				PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
				if(StringUtils.isBlank(levelInfo.getPlusEndDate())) {//非橙意卡会员
					result.setResultCode(916426002);
					result.setResultMessage(bInfo(916426002));
					return result;
				}
			}
		}
		return result;
	}
}
