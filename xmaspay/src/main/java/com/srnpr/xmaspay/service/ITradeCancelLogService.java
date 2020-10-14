package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 交易取消日志记录
 * @author pang_jhui
 *
 */
public interface ITradeCancelLogService extends IPayService {
	
	/**
	 * 日志记录
	 * @param mDataMap
	 */
	public void recordLog(MDataMap mDataMap);

}
