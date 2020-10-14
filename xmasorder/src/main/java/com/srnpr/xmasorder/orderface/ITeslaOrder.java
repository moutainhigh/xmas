package com.srnpr.xmasorder.orderface;

import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;

/**
 * 订单统一处理接口
 * 
 * @author srnpr
 *
 */
public interface ITeslaOrder {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder);

}
