package com.srnpr.xmaspay.process.impl;

import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.config.face.IAlipayTradeCancelConfig;
import com.srnpr.xmaspay.process.IAlipayTradeCancelProcess;
import com.srnpr.xmaspay.request.AlipayBizContentRequest;
import com.srnpr.xmaspay.request.AlipayTradeCancelRequest;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayUnifyErrorResponse;
import com.srnpr.xmaspay.response.AlipayUnifyResponse;
import com.srnpr.xmaspay.service.IAlipayTradeCancelService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝交易取消解析
 * @author pang_jhui
 * @author zhaojunling 关闭待支付订单逻辑暂时废弃 
 */
public class AlipayTradeCancelProcess extends BasePayProcess implements IAlipayTradeCancelProcess {

	@Override
	public AlipayUnifyResponse process(String orderCode) {
		return new AlipayUnifyResponse();
		/*
		
		IAlipayTradeCancelService tradeCancelService = (IAlipayTradeCancelService) getPayService();
		
		IAlipayTradeCancelConfig tradeCancelConfig = (IAlipayTradeCancelConfig) getPayConfig();
		
		AlipayTradeCancelRequest tradeCancelRequest = new AlipayTradeCancelRequest();
		
		String trade_no = PayServiceFactory.getInstance().getOrderPayService().getAlipayTradeNO(orderCode);
		
		tradeCancelRequest.setOut_trade_no(orderCode);
		
		tradeCancelRequest.setTrade_no(trade_no);
		
		AlipayUnifyRequest alipayUnifyRequest = new AlipayUnifyRequest();
		
		try {
			
			alipayUnifyRequest = initRequest(tradeCancelConfig, tradeCancelRequest);
			
		} catch (Exception e) {
		
			AlipayUnifyErrorResponse errorResponse = new AlipayUnifyErrorResponse();
			
			errorResponse.setIs_success(AlipaySuccessEnum.F.name());
			
			errorResponse.setError(e.getMessage());
			
			return errorResponse;
			
		}
		
		return tradeCancelService.doProcess(alipayUnifyRequest, tradeCancelConfig);
		*/
		
	}

	@Override
	public AlipayTradeCancelRequest initRequest(IAlipayTradeCancelConfig config, AlipayBizContentRequest bizContentRequest)
			throws Exception {

		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();

		request.set_input_charset(config.getRequestCharset());

		request.setService(config.getMethod());
		
		request.setOut_trade_no(bizContentRequest.getOut_trade_no());
		
		request.setTrade_no(bizContentRequest.getTrade_no());
		
		request.setPartner(config.getPartner());

		MDataMap mDataMap = BeanComponent.getInstance().objectToMap(request,AlipayUnifyRequest.class,true);

		String sign = config.getSign(mDataMap);
		
		request.setSign_type(config.getSignType());

		request.setSign(sign);

		return request;
	}
	
	

}
