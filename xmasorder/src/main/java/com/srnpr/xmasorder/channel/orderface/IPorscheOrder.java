package com.srnpr.xmasorder.channel.orderface;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;

/**
 * 第三方订单统一处理接口
 * @remark 
 * @author 任宏斌
 * @date 2019年12月2日
 */
public interface IPorscheOrder {

	public PorscheGtResult doRefresh(PorscheGtOrder porscheOrder);

}
