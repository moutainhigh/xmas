package com.srnpr.xmaspay.aspect.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import com.srnpr.xmaspay.aspect.IAlipayTradeCancelAspect;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateTypeEnum;
import com.srnpr.xmaspay.request.AlipayTradeCancelRequest;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayUnifyResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝交易取消方面
 * @author pang_jhui
 *
 */
public class AlipayTradeCancelAspectImpl implements IAlipayTradeCancelAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
		
		AlipayTradeCancelRequest alipayUnifyRequest = (AlipayTradeCancelRequest) joinPoint.getArgs()[0];
		
		JsonHelper<AlipayUnifyRequest> helper = new JsonHelper<AlipayUnifyRequest>();
		
		MDataMap mDataMap = new MDataMap();
		
		mDataMap.put("order_code", alipayUnifyRequest.getOut_trade_no());
		
		mDataMap.put("pay_type", PayGateTypeEnum.ALIPAY.name());
		
		mDataMap.put("request_content", helper.ObjToString(alipayUnifyRequest));
		
		PayServiceFactory.getInstance().getTradeCancelLogService().recordLog(mDataMap);
		
	}

	@Override
	public AlipayUnifyResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		AlipayTradeCancelRequest alipayTradeCancelRequest = (AlipayTradeCancelRequest) joinPoint.getArgs()[0];
		
		AlipayUnifyResponse response = (AlipayUnifyResponse) joinPoint.proceed();
		
		doAfter(alipayTradeCancelRequest.getOut_trade_no(),response);
		
		return response;
	}

	@Override
	public void doAfter(String order_code,AlipayUnifyResponse response) {
		
		JsonHelper<AlipayUnifyResponse> helper = new JsonHelper<AlipayUnifyResponse>();
		
		MDataMap mDataMap = new MDataMap();
		
		mDataMap.put("order_code", order_code);
		
		mDataMap.put("pay_type", PayGateTypeEnum.ALIPAY.name());
		
		mDataMap.put("response_content", helper.ObjToString(response));
		
		if(response.upFlagTrue()){
			
			mDataMap.put("flag", Constants.FLAG_SUCCESS);			
			
		}else{
			
			mDataMap.put("flag", Constants.FLAG_SUCCESS);
			
		}
		
		PayServiceFactory.getInstance().getTradeCancelLogService().recordLog(mDataMap);
		
	}

}
