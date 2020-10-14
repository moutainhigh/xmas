package com.srnpr.xmaspay.aspect.impl;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IWechatCustomsAspect;
import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.request.WechatCustomRequest;
import com.srnpr.xmaspay.response.WechatCustomResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信报关信息处理
 * @author zhaojunling
 *
 */
public class WechatCutomsAspectImpl extends BaseClass implements IWechatCustomsAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
	}

	@Override
	public void doAfter(JoinPoint joinPoint) throws Exception {		
	}
	
	@Override
	public WechatCustomResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		WechatCustomRequest wechatCustomRequest = (WechatCustomRequest) joinPoint.getArgs()[0];
		WechatCustomResponse wechatCustomResponse = (WechatCustomResponse) joinPoint.proceed();
		
		if(WechatUnifyResultCodeEnum.SUCCESS.name().equalsIgnoreCase(wechatCustomResponse.getReturn_code())){
			if(WechatUnifyResultCodeEnum.SUCCESS.name().equalsIgnoreCase(wechatCustomResponse.getResult_code())){
				// 报关成功，记录报关信息
				saveOrderCustoms(wechatCustomRequest, wechatCustomResponse);
			}
		}
		
		return wechatCustomResponse;
	}

	/** 保存报关成功的订单信息 */
	private void saveOrderCustoms(WechatCustomRequest wechatCustomRequest, WechatCustomResponse wechatCustomResponse){
		MDataMap orderInfoMap = PayServiceFactory.getInstance().getOrderPayService().getOrderInfo(wechatCustomRequest.getOut_trade_no());
		MDataMap mDataMap = new MDataMap();
		
		// TODO test
		/*
		orderInfoMap = new MDataMap();
		orderInfoMap.put("seller_code", "SI2003");
		orderInfoMap.put("small_seller_code", "SF03100294");
		orderInfoMap.put("due_money", "18.00");
		orderInfoMap.put("transport_money", "0");
		*/
		
		mDataMap.put("order_code", wechatCustomRequest.getOut_trade_no());
		mDataMap.put("trade_no", wechatCustomRequest.getTransaction_id());
		mDataMap.put("seller_code", orderInfoMap.get("seller_code"));
		mDataMap.put("small_seller_code", orderInfoMap.get("small_seller_code"));
		mDataMap.put("customs_code", wechatCustomRequest.getCustoms());
		mDataMap.put("mch_customs_no", wechatCustomRequest.getMch_customs_no());
		mDataMap.put("due_amount", orderInfoMap.get("due_money"));
		mDataMap.put("transport_amount", orderInfoMap.get("transport_money"));
		mDataMap.put("sub_trade_no", wechatCustomResponse.getSub_order_id());
		
		// 不存在子支付单号时，则表明直接使用原支付单号
		if(StringUtils.isBlank(wechatCustomResponse.getSub_order_id())){
			mDataMap.put("sub_trade_no", wechatCustomRequest.getTransaction_id());
		}
		
		PayServiceFactory.getInstance().getOrderCustomsService().saveOrUpdateOrderCustoms(mDataMap);
	}
}
