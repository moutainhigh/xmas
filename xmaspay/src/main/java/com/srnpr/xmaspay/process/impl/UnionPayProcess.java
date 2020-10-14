package com.srnpr.xmaspay.process.impl;

import java.util.Calendar;
import java.util.Date;
import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.process.IUnionPayProcess;
import com.srnpr.xmaspay.request.UnionPayRequest;
import com.srnpr.xmaspay.response.UnionPayResponse;
import com.srnpr.xmaspay.service.IUnionPayService;
import com.srnpr.xmaspay.util.CalendarUtil;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmaspay.util.UnionPayCertUtil;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 银联支付业务解析实现类
 * @author pang_jhui
 *
 */
public class UnionPayProcess extends BasePayProcess implements IUnionPayProcess {

	@Override
	public UnionPayResponse process(String orderCode) {
		
		MDataMap mDataMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		
		UnionPayResponse unionPayResponse = new UnionPayResponse();
		
		if(mDataMap != null){			

			IUnionPayService unionPayService = (IUnionPayService) getPayService();
			
			IUnionPayConfig unionPayConfig = (IUnionPayConfig) getPayConfig();
			
			UnionPayRequest unionPayRequest = new UnionPayRequest();
			
			unionPayRequest.setAccessType(unionPayConfig.getAccessType());
			
			unionPayRequest.setBackUrl(unionPayConfig.getBackUrl());
			
			unionPayRequest.setBizType(unionPayConfig.getBizType());
			
			unionPayRequest.setCertId(UnionPayCertUtil.getInstance().getSignCertId());
			
			unionPayRequest.setChannelType(unionPayConfig.getChannelType());
			
			unionPayRequest.setCurrencyCode(unionPayConfig.getCurrencyCode());
			
			unionPayRequest.setEncoding(unionPayConfig.getEncoding());
			
			unionPayRequest.setMerId(unionPayConfig.getMerId());
			
			unionPayRequest.setOrderId(orderCode);
			
			Date createDate= DateUtil.toDate(mDataMap.get("create_time"), DateUtil.sdfDateTime);
			
			int diff = PayServiceFactory.getInstance().getOrderPayService().getOrderValidTime(orderCode);
			
			/*计算订单有效时间，差异类型：默认为分钟*/
			Calendar createCalendar = CalendarUtil.calCalendar(createDate, diff, Calendar.MINUTE);
			
			String payTimeOut = DateUtil.toString(createCalendar.getTime(),DateUtil.sdfDateTimeTamp);
			
			unionPayRequest.setPayTimeout(payTimeOut);
			
			unionPayRequest.setSignMethod(Boolean.toString(unionPayConfig.getSingleMode()));
			
			unionPayRequest.setTxnAmt(mDataMap.get("due_money"));
			
			unionPayRequest.setTxnSubType(unionPayConfig.getTxnSubType());
			
			String txnTime = DateUtil.toString(new Date(), DateUtil.sdfDateTimeTamp);
			
			unionPayRequest.setTxnTime(txnTime);
			
			unionPayRequest.setTxnType(unionPayConfig.getTnxType());
			
			unionPayRequest.setVersion(unionPayConfig.getVersion());
			
			unionPayResponse = unionPayService.doProcess(unionPayRequest, unionPayConfig);
			
		}else{
			
			unionPayResponse.setResultcode(-1);
			
			unionPayResponse.setResultmsg("订单编号：【"+orderCode+"】在系统中不存在");
			
		}
		
		return unionPayResponse;
	}

}
