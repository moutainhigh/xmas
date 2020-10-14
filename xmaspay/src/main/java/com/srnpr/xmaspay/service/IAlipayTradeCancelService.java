package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IAlipayTradeCancelConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayUnifyResponse;

/**
 * 支付宝交易取消业务实现
 * @author pang_jhui
 *
 */
public interface IAlipayTradeCancelService extends IPayService {
	
	/**
	 * 支付宝交易取消业务实现
	 * @param request
	 * 		请求信息
	 * @param config
	 * 		配置信息
	 * @return 接口响应信息
	 */
	public AlipayUnifyResponse doProcess(AlipayUnifyRequest request, IAlipayTradeCancelConfig config);

}
