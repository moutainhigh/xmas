package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.common.AlipayCustomsEnum;
import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝报关
 * @author zhaojunling
 *
 */
public interface IAlipayCustomsProcess extends IPayProcess {
	
	/**
	 * 根据订单编号报关
	 * @param orderCode
	 * 		订单编号
	 * @param customs
	 * 		海关
	 * @return 报关处理结果
	 */
	public AlipayCustomsResponse process(String orderCode, AlipayCustomsEnum customs,MDataMap mDataMap);
	

}
