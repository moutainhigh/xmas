package com.srnpr.xmaspay.aspect.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IAlipayCustomsAspect;
import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;
import com.srnpr.xmaspay.request.AlipayCustomsRequest;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝报关信息处理
 * @author zhaojunling
 *
 */
public class AlipayCutomsAspectImpl extends BaseClass implements IAlipayCustomsAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
	}

	@Override
	public void doAfter(JoinPoint joinPoint) throws Exception {		
	}
	
	@Override
	public AlipayCustomsResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		AlipayCustomsRequest alipayCustomsRequest = (AlipayCustomsRequest) joinPoint.getArgs()[0];
		AlipayCustomsResponse alipayCustomsResponse = (AlipayCustomsResponse) joinPoint.proceed();
		
		if(AlipaySuccessEnum.T.name().equalsIgnoreCase(alipayCustomsResponse.getIs_success())){
			if(AlipayUnifyResultCodeEnum.SUCCESS.name().equalsIgnoreCase(alipayCustomsResponse.getResult_code())){
				// 报关成功，记录报关信息
				saveOrderCustoms(alipayCustomsRequest, alipayCustomsResponse);
			}
		}
		
		return alipayCustomsResponse;
	}

	/** 保存报关成功的订单信息 */
	private void saveOrderCustoms(AlipayCustomsRequest alipayCustomsRequest, AlipayCustomsResponse alipayCustomsResponse){
		MDataMap orderInfoMap = PayServiceFactory.getInstance().getOrderPayService().getOrderInfo(alipayCustomsRequest.getSub_out_biz_no());
		MDataMap mDataMap = new MDataMap();
		
		// TODO test
		/*
		orderInfoMap = new MDataMap();
		orderInfoMap.put("seller_code", "SI2003");
		orderInfoMap.put("small_seller_code", "SF03100294");
		orderInfoMap.put("transport_money", "0");
		*/
		
		mDataMap.put("order_code", alipayCustomsRequest.getSub_out_biz_no());
		mDataMap.put("trade_no", alipayCustomsRequest.getTrade_no());
		mDataMap.put("sub_trade_no", alipayCustomsResponse.getTrade_no());
		mDataMap.put("seller_code", orderInfoMap.get("seller_code"));
		mDataMap.put("small_seller_code", orderInfoMap.get("small_seller_code"));
		mDataMap.put("customs_code", alipayCustomsRequest.getCustoms_place());
		mDataMap.put("mch_customs_no", alipayCustomsRequest.getMerchant_customs_code());
		mDataMap.put("due_amount", alipayCustomsRequest.getAmount().toString());
		mDataMap.put("transport_amount", orderInfoMap.get("transport_money"));
		
		PayServiceFactory.getInstance().getOrderCustomsService().saveOrUpdateOrderCustoms(mDataMap);
	}
}
