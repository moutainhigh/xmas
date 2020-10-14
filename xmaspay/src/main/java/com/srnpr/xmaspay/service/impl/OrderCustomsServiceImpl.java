package com.srnpr.xmaspay.service.impl;

import java.util.Date;

import com.srnpr.xmaspay.service.IOrderCustomsService;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 订单报关业务
 * @author zhaojunling
 *
 */
public class OrderCustomsServiceImpl implements IOrderCustomsService {

	@Override
	public void saveOrderCustoms(MDataMap mDataMap) {
		String date_time = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
		
		mDataMap.put("create_time", date_time);
		mDataMap.put("update_time", date_time);
		
		DbUp.upTable("oc_order_customs").dataInsert(mDataMap);

	}
	
	@Override
	public void saveOrUpdateOrderCustoms(MDataMap mDataMap) {
		MDataMap orderCustoms = getOrderCustoms(mDataMap.get("order_code"));
		if(orderCustoms == null){
			saveOrderCustoms(mDataMap);
		}else{
			mDataMap.put("zid", orderCustoms.get("zid"));
			mDataMap.put("uid", orderCustoms.get("uid"));
			
			mDataMap.put("update_time", DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME));
			DbUp.upTable("oc_order_customs").update(mDataMap);
		}
	}

	@Override
	public void recordLog(MDataMap mDataMap) {
		String date_time = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
		
		mDataMap.put("create_time", date_time);
		
		DbUp.upTable("lc_order_customs_log").dataInsert(mDataMap);

	}

	@Override
	public MDataMap getOrderCustoms(String orderCode) {
		return DbUp.upTable("oc_order_customs").one("order_code", orderCode);
	}

}
