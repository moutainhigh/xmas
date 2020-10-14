package com.srnpr.xmaspay.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.service.ITradeCancelLogService;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 交易取消日志记录
 * @author pang_jhui
 *
 */
public class TradeCancelLogServiceImpl implements ITradeCancelLogService {

	@Override
	public void recordLog(MDataMap mDataMap) {
		
		MDataMap logMap = DbUp.upTable("lc_pay_order_cancel").one("order_code",mDataMap.get("order_code"),"pay_type",mDataMap.get("pay_type"));
		
		String date_time = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
		
		if(logMap != null){
			
			logMap.put("update_time", date_time);
			
			String request_content = mDataMap.get("request_content");
			
			String response_content = mDataMap.get("response_content");
			
			if(StringUtils.isNotBlank(request_content)){
				
				logMap.put("request_content", request_content);
				
			}
			
			if(StringUtils.isNotBlank(response_content)){
				
				logMap.put("response_content", response_content);
				
			}
			
			String flag = StringUtils.isNotBlank(mDataMap.get("flag"))?mDataMap.get("flag"):"";
			
			logMap.put("flag", flag);
			
			DbUp.upTable("lc_pay_order_cancel").update(logMap);
			
		}else{			
			
			mDataMap.put("create_time", date_time);
			
			mDataMap.put("update_time", date_time);
			
			DbUp.upTable("lc_pay_order_cancel").dataInsert(mDataMap);
			
		}

	}

}
