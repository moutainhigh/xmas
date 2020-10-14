package com.srnpr.xmaspay.aspect.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IPayGateWayAspect;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.MoneyTypeEnum;
import com.srnpr.xmaspay.config.face.IPayGateWayConfig;
import com.srnpr.xmaspay.request.PayGateWayRequest;
import com.srnpr.xmaspay.response.PayGateWayResponse;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;

/**
 * 支付网关相关信息处理
 * @author pang_jhui
 *
 */
public class PayGateWayAspectImpl extends BaseClass implements IPayGateWayAspect {



	@Override
	public PayGateWayResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		PayGateWayResponse payGateWayResponse = new PayGateWayResponse();
		
		PayGateWayRequest payGateWayRequest = (PayGateWayRequest) joinPoint.getArgs()[0];
		
		IPayGateWayConfig payGateWayConfig = (IPayGateWayConfig) joinPoint.getArgs()[1];
		/*扩展参数集合*/
		MDataMap extendMap = (MDataMap) joinPoint.getArgs()[2];
		/*执行前相关返回参数*/
		Map<String,Object> beforeMdataMap = doProcessBefore(payGateWayRequest, extendMap);
		
		boolean orderPayFlag = (Boolean) beforeMdataMap.get(Constants.KEY_ORDER_PAY_STATUS); 
		
		if (!orderPayFlag) {// 订单未支付

			try {
				
				payGateWayRequest = buildRquest(payGateWayRequest,
						extendMap.get(Constants.SERVER_CONTEXT_PATH), payGateWayConfig);
				
				Object[] args = {payGateWayRequest,payGateWayConfig,extendMap};
				
				payGateWayResponse = (PayGateWayResponse) joinPoint.proceed(args);

				doProcessAfter(payGateWayRequest, extendMap);
				
			} catch (Exception e) {
				
				payGateWayResponse.setResultcode(-1);
				
				payGateWayResponse.setResultmsg(e.getMessage());
				
			}

			

		}else{//订单已支付
			
			payGateWayResponse.setResultcode(964101001);
			
			payGateWayResponse.setResultmsg(TopUp.upLogInfo(964101001));
			
		}
		
		return payGateWayResponse;
	}

	@Override
	public Map<String,Object> doProcessBefore(PayGateWayRequest payGateWayRequest, MDataMap mDataMap) {
		
		/*判断订单是否已经支付，true支付，false未支付*/
		boolean flag = false;
		
		Map<String,Object> paramDataMap = new HashMap<String,Object>();

		MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService()
				.getOcPaymentInfo(payGateWayRequest.getC_order());
		
		if(orderMap != null){
			
			String flag_success = orderMap.get("flag_success");
			
			/*判断订单是否支付成功*/
			if(StringUtils.equals(flag_success, Constants.ORDER_PAY_STATUS_SUCCESS)){
				
				flag = true;
				
			}
			
		}
		
		paramDataMap.put(Constants.KEY_ORDER_PAY_STATUS, flag);
		
		return paramDataMap;

	}

	@Override
	public void doProcessAfter(PayGateWayRequest payGateWayRequest, MDataMap mDataMap) {
		
		
		
	}
	
	/**
	 * 构件请求信息
	 * @param orderCode
	 * 		订单编号
	 * @param gateWayConfig
	 * 		配置信息
	 * @param payGate
	 * 		支付网关
	 * @return 支付网关请求信息
	 * @throws Exception 
	 */
	public PayGateWayRequest buildRquest(PayGateWayRequest payGateWayRequest,String contextPath,IPayGateWayConfig gateWayConfig) throws Exception{
		
		String orderCode = payGateWayRequest.getC_order();
		
		MDataMap mDataMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		
		MDataMap orderAddressMap = PayServiceFactory.getInstance().getOrderPayService().getOrderAddressInfo(orderCode);
		
		payGateWayRequest.setC_mid(gateWayConfig.getMerchantCode());
		
		payGateWayRequest.setC_order(orderCode);
		
		payGateWayRequest.setC_name(orderAddressMap.get("receive_person"));
		
		payGateWayRequest.setC_address(orderAddressMap.get("address"));
		
		payGateWayRequest.setC_tel(orderAddressMap.get("mobilephone"));
		
		payGateWayRequest.setC_post(orderAddressMap.get("postcode"));
		
		payGateWayRequest.setC_email(orderAddressMap.get("email"));
		
		/*应付款*/
		BigDecimal dueMoney = new BigDecimal(mDataMap.get("due_money"));
		
		payGateWayRequest.setC_orderamount(dueMoney);
		
		String create_time = mDataMap.get("create_time");
		
		Date date = DateUtil.sdfDateTime.parse(create_time);
		
		payGateWayRequest.setC_ymd(DateUtil.sdfDateOnlyNoSp.format(date));
		
		payGateWayRequest.setC_moneytype(MoneyTypeEnum.RMB.getCode());
		
		payGateWayRequest.setC_retflag(Constants.SERVER_RETURN_FLAG);
		
		String return_url = gateWayConfig.getPayCallBackUrl(contextPath);
		
		payGateWayRequest.setC_returl(return_url);
		
		payGateWayRequest.setNotifytype(Constants.PAYGATEWAY_NOTIFICE_TYPE);
		
		payGateWayRequest.setC_language(Constants.ORDER_LANGUAGE);
		
		payGateWayRequest.setC_version(gateWayConfig.getVersion());
		
		MDataMap fielMap = BeanComponent.getInstance().objectToMap(payGateWayRequest,null,false);
		
		payGateWayRequest.setC_signstr(gateWayConfig.getSign(fielMap));
		
		return payGateWayRequest;
		
		
	}
	


}
