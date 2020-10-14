package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IWechatTradeCancelConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatUnifyResponse;

/**
 * 微信交易取消业务实现
 * @author pang_jhui
 *
 */
public interface IWechatTradeCancelService extends IPayService {
	
	/**
	 * 微信交易取消业务实现
	 * @param request
	 * 		请求信息
	 * @param config
	 * 		配置信息
	 * @return 接口响应信息
	 */
	public WechatUnifyResponse doProcess(WechatRequest request, IWechatTradeCancelConfig config);

}
