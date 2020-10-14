package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 订单报关成功信息记录
 * @author zhaojunling
 *
 */
public interface IOrderCustomsService extends IPayService {
	
	/**
	 * 订单报关成功信息记录
	 * @param mDataMap
	 */
	public void saveOrderCustoms(MDataMap mDataMap);
	
	/**
	 * 保存或者更新已存在的报关信息
	 * @param mDataMap
	 */
	public void saveOrUpdateOrderCustoms(MDataMap mDataMap);
	
	/**
	 * 报关请求日志记录
	 * @param mDataMap
	 */
	public void recordLog(MDataMap mDataMap);
	
	/**
	 * 获取订单的报关信息
	 * @param orderCode
	 * @return mDataMap
	 */
	public MDataMap getOrderCustoms(String orderCode);

}
