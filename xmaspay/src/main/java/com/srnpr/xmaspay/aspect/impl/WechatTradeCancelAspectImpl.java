package com.srnpr.xmaspay.aspect.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import com.srnpr.xmaspay.aspect.IWechatTradeCancelAspect;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateTypeEnum;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatUnifyResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝交易取消方面
 * @author pang_jhui
 *
 */
public class WechatTradeCancelAspectImpl implements IWechatTradeCancelAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
		
		WechatRequest wechatRequest = (WechatRequest) joinPoint.getArgs()[0];
		
		JsonHelper<WechatRequest> helper = new JsonHelper<WechatRequest>();
		
		MDataMap mDataMap = new MDataMap();
		
		mDataMap.put("order_code", wechatRequest.getOut_trade_no());
		
		mDataMap.put("pay_type", PayGateTypeEnum.WECHAT.name());
		
		mDataMap.put("request_content", helper.ObjToString(wechatRequest));
		
		PayServiceFactory.getInstance().getTradeCancelLogService().recordLog(mDataMap);
		
	}

	@Override
	public WechatUnifyResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		WechatRequest wechatRequest = (WechatRequest) joinPoint.getArgs()[0];
		
		WechatUnifyResponse response = (WechatUnifyResponse) joinPoint.proceed();
		
		doAfter(wechatRequest.getOut_trade_no(),response);
		
		return response;
	}

	@Override
	public void doAfter(String orderCode,WechatUnifyResponse response) {
		
		JsonHelper<WechatUnifyResponse> helper = new JsonHelper<WechatUnifyResponse>();
		
		MDataMap mDataMap = new MDataMap();
		
        mDataMap.put("order_code", orderCode);
		
		mDataMap.put("response_content", helper.ObjToString(response));
		
		mDataMap.put("pay_type", PayGateTypeEnum.WECHAT.name());
		
		if(response.upFlagTrue()){
			
			mDataMap.put("flag", Constants.FLAG_SUCCESS);			
			
		}else{
			
			mDataMap.put("flag", Constants.FLAG_SUCCESS);
			
		}
		
		PayServiceFactory.getInstance().getTradeCancelLogService().recordLog(mDataMap);
		
	}

}
