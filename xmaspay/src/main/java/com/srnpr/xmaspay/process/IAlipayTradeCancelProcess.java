package com.srnpr.xmaspay.process;

import com.srnpr.xmaspay.config.face.IAlipayTradeCancelConfig;
import com.srnpr.xmaspay.face.IPayProcess;
import com.srnpr.xmaspay.request.AlipayBizContentRequest;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayUnifyResponse;

/**
 * 支付宝交易取消
 * @author pang_jhui
 *
 */
public interface IAlipayTradeCancelProcess extends IPayProcess {
	
	/**
	 * 根据订单编号取消交易
	 * @param orderCode
	 * 		订单编号
	 * @return 支付宝响应信息
	 */
	public AlipayUnifyResponse process(String orderCode);
	
	/**
	 * 初始化请求信息
	 * @param config
	 * 		配置信息
	 * @param bizContentRequest
	 * 		请求业务内容
	 * @return 支付宝统一请求对象
	 * @throws Exception 
	 */
	public AlipayUnifyRequest initRequest(IAlipayTradeCancelConfig config, AlipayBizContentRequest bizContentRequest) throws Exception;

}
