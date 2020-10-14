package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 惠惠农场兑换
 * @remark 
 * @author 任宏斌
 * @date 2020年3月2日
 */
public class TeslaMakeFarmProduct extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		String treeCode = teslaOrder.getTreeCode();
		
		if(StringUtils.isNotEmpty(treeCode)) {

			//判断当前是否存在惠惠农场活动
			String nowTime = DateUtil.getSysDateTimeString();
			String sSql1 = "SELECT event_code,event_name,event_type_code FROM sc_hudong_event_info "
					+ "WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' "
					+ "AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time DESC LIMIT 1";
			Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
			
			if(null == eventInfoMap) {
				result.setResultCode(91642600);
				result.setResultMessage(bInfo(91642600));
				return result;
			}
			
			MDataMap tree = DbUp.upTable("sc_huodong_farm_user_tree").one("member_code", teslaOrder.getUorderInfo().getBuyerCode(), "tree_code", treeCode);
			if(null == tree) {
				result.setResultCode(91642609);
				result.setResultMessage(bInfo(91642609));
				return result;
			}
			
			String treeStage = tree.get("tree_stage");
			String changeFlag = tree.get("change_flag");
			
			if(!"449748450005".equals(treeStage)) {
				result.setResultCode(91642606);
				result.setResultMessage(bInfo(91642606));
				return result;
			}
			
			if("1".equals(changeFlag)) {
				result.setResultCode(91642607);
				result.setResultMessage(bInfo(91642607));
				return result;
			}
			
			if(teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
				//标识使用
				DbUp.upTable("sc_huodong_farm_user_tree").dataUpdate(new MDataMap("change_flag","1", "tree_code", treeCode), "change_flag", "tree_code");
			}
					
		}
		return result;
	}
}
