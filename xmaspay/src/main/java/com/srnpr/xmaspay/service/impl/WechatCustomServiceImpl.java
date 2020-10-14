package com.srnpr.xmaspay.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;

import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.face.IWechatCustomConfig;
import com.srnpr.xmaspay.request.WechatCustomRequest;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatCustomResponse;
import com.srnpr.xmaspay.service.IWechatCustomService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmaspay.util.XmlUtil;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 微信通关业务实现
 * @author pang_jhui
 *
 */
public class WechatCustomServiceImpl extends BaseClass implements IWechatCustomService {
	
	@Override
	public WechatCustomResponse doProcess(WechatCustomRequest request, IWechatCustomConfig config) {
		
		WechatCustomResponse wechatCustomResponse = new WechatCustomResponse();
		
		MDataMap headerDataMap = new MDataMap();
		
		headerDataMap.put("Pragma:", "no-cache");
		
		headerDataMap.put("Cache-Control", "no-cache");
		
		headerDataMap.put("Content-Type", "text/xml");
		
		String requestXml = null;
		String responseMsg = null;
		
		String requestTime = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
		String responseTime = null;
		try {
			
			MDataMap requestMap = BeanComponent.getInstance().objectToMap(request,WechatRequest.class,true);
			
			requestXml = XmlUtil.getInstance().mDataMapToXml(requestMap);
			
			StringEntity httpEntity = new StringEntity(requestXml, "UTF-8");
			
			bLogInfo(0, "doProcess req -> "+requestXml);
			responseMsg = WebClientSupport.poolRequest(config.getRequestUrl(), httpEntity, headerDataMap);
			bLogInfo(0, "doProcess resp -> "+responseMsg);
			
			responseTime = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
			
			MDataMap mDataMap = XmlUtil.getInstance().xmlToMDataMap(responseMsg);			
			
			if(mDataMap != null){
				wechatCustomResponse = BeanComponent.getInstance().invoke(WechatCustomResponse.class, mDataMap,true);
			}
			
		} catch(Exception ex) {
			
			wechatCustomResponse.setReturn_code(WechatUnifyResultCodeEnum.FAIL.name());
			wechatCustomResponse.setResult_code(WechatUnifyResultCodeEnum.FAIL.name());
			wechatCustomResponse.setReturn_msg(ex.getMessage());
		
		}
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("order_code", request.getOut_trade_no());
		mDataMap.put("request_data", StringUtils.trimToEmpty(requestXml));
		mDataMap.put("response_data", StringUtils.trimToEmpty(responseMsg));
		if(WechatUnifyResultCodeEnum.SUCCESS.name().equals(wechatCustomResponse.getResult_code())){
			mDataMap.put("process_status", WechatUnifyResultCodeEnum.SUCCESS.name());
		}else{
			mDataMap.put("process_status", WechatUnifyResultCodeEnum.FAIL.name());
		}
		mDataMap.put("request_time", requestTime);
		mDataMap.put("response_time", StringUtils.trimToEmpty(responseTime));
		
		// 记录报关请求日志
		PayServiceFactory.getInstance().getOrderCustomsService().recordLog(mDataMap);
		
		return wechatCustomResponse;
		
	}


	
}
