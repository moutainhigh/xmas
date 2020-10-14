package com.srnpr.xmaspay.service.impl;

import java.io.IOException;

import com.srnpr.xmaspay.config.face.IPayGateWayConfig;
import com.srnpr.xmaspay.request.PayGateWayRequest;
import com.srnpr.xmaspay.response.PayGateWayResponse;
import com.srnpr.xmaspay.service.IPayGateWayService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 支付网关业务实现类
 * @author pang_jhui
 *
 */
public class PayGateWayServiceImpl extends BaseClass implements IPayGateWayService {

	@Override
	public PayGateWayResponse doProcess(PayGateWayRequest payGateWayRequest,IPayGateWayConfig payConfig ,MDataMap extendMap) {		
		
		
		
		String returnStr = "";
		
		PayGateWayResponse response = new PayGateWayResponse();
		
		
		try {
			
			MDataMap mDataMap = BeanComponent.getInstance().objectToMap(payGateWayRequest, null,false);
			
			returnStr = WebClientSupport.upPost(payConfig.getRequestPath(), mDataMap);
			
		} catch (Exception e) {
			
			response.setResultcode(-1);
			
			response.setResultmsg(e.getMessage());
			
			bLogError(0, e.getMessage());
			
		}
		
		JsonHelper<PayGateWayResponse> responseHelper = new JsonHelper<PayGateWayResponse>();		
		
		try {
			
			response = responseHelper.StringToObjExp(returnStr, response);
			
		} catch (IOException e) {
			
			response.setResultmsg(returnStr);
			
		}
		
		
		
		return response;
		
	}

}
