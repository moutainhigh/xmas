package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.response.PayCustomsResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 订单报关业务统一入口实现
 * @author zhaojunling
 *
 */
public interface IPayCustomsProcess extends IPayProcess {
	
	/**
	 * 订单报关实现
	 * @param mDataMap
	 * 		必须参数：  orderCode
	 * @return 接口响应信息
	 */
	public PayCustomsResponse process(MDataMap mDataMap);



}
