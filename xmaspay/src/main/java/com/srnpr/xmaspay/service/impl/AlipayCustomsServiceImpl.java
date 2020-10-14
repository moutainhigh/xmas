package com.srnpr.xmaspay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.srnpr.xmaspay.common.AlipayResponseNodeEnum;
import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.face.IAlipayCustomsConfig;
import com.srnpr.xmaspay.request.AlipayCustomsRequest;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;
import com.srnpr.xmaspay.service.IAlipayCustomsService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmaspay.util.XmlUtil;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;

/**
 * 支付宝报关业务实现
 * @author pang_jhui
 *
 */
public class AlipayCustomsServiceImpl extends BaseClass implements IAlipayCustomsService {
	
	@Override
	public AlipayCustomsResponse doProcess(AlipayCustomsRequest request, IAlipayCustomsConfig config) {
		
		AlipayCustomsResponse alipayCustomsResponse = new AlipayCustomsResponse();
		
		MDataMap headerDataMap = new MDataMap();
		
		headerDataMap.put("Cache-Control", "no-cache");
		headerDataMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headerDataMap.put("User-Agent", "Mozilla/4.0");
		
		String requestStr = null;
		String responseMsg = null;
		
		String requestTime = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
		String responseTime = null;
		try {
			
			MDataMap requestMap = BeanComponent.getInstance().objectToMap(request,AlipayUnifyRequest.class,true);
				
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String sKey : requestMap.keySet()) {
				nvps.add(new BasicNameValuePair(sKey, requestMap.get(sKey)));
			}
			// 取得发送请求的参数，便于后面保存到数据库
			HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, TopConst.CONST_BASE_ENCODING);
			requestStr = EntityUtils.toString(httpEntity);
			
			bLogInfo(0, "doProcess req -> "+requestStr);
			responseMsg = WebClientSupport.poolRequest(config.getRequestUrl()+"?_input_charset="+config.getRequestCharset(), httpEntity, headerDataMap);
			bLogInfo(0, "doProcess resp -> "+responseMsg);

			responseTime = DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME);
			// 把响应的业务参数值转到Map
			MDataMap mDataMap = convertRespXmlToMap(responseMsg);			
			
			alipayCustomsResponse = BeanComponent.getInstance().invoke(AlipayCustomsResponse.class, mDataMap,false);
			
		} catch(Exception ex) {
			alipayCustomsResponse.setIs_success(AlipaySuccessEnum.F.name());
			alipayCustomsResponse.setResult_code(AlipayUnifyResultCodeEnum.FAIL.name());
			alipayCustomsResponse.setError(ex.getMessage());
			alipayCustomsResponse.setDetail_error_des(alipayCustomsResponse.getError());
		}
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("order_code", request.getSub_out_biz_no());
		mDataMap.put("request_data", StringUtils.trimToEmpty(requestStr));
		mDataMap.put("response_data", StringUtils.trimToEmpty(responseMsg));
		if(AlipayUnifyResultCodeEnum.SUCCESS.name().equals(alipayCustomsResponse.getResult_code())){
			mDataMap.put("process_status", alipayCustomsResponse.getResult_code());
		}else{
			mDataMap.put("process_status", AlipayUnifyResultCodeEnum.FAIL.name());
		}
		mDataMap.put("request_time", requestTime);
		mDataMap.put("response_time", StringUtils.trimToEmpty(responseTime));
		
		// 记录报关请求日志
		PayServiceFactory.getInstance().getOrderCustomsService().recordLog(mDataMap);
		
		return alipayCustomsResponse;
		
	}


	/**
	 * 解析返回内容到map
	 * @param responseMsg
	 * @return
	 */
	private MDataMap convertRespXmlToMap(String responseMsg){
		MDataMap mDataMap = XmlUtil.getInstance().xmlToMDataMap(responseMsg);	
		if(mDataMap == null) throw new RuntimeException(responseMsg);
		
		if(!AlipaySuccessEnum.T.name().equals(mDataMap.get("is_success"))){
			throw new RuntimeException(mDataMap.get("error"));
		}
		
		MDataMap respDataMap = XmlUtil.getInstance().xmlToMDataMap(mDataMap.get(AlipayResponseNodeEnum.response.name()));	
		if(respDataMap == null){
			throw new RuntimeException(responseMsg);
		}
		
		mDataMap.putAll(respDataMap);
		
		return mDataMap;
	}
	
}
