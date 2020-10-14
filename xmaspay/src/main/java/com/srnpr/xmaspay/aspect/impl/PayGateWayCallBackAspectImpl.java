package com.srnpr.xmaspay.aspect.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IPayGateWayCallBackAspect;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateEnum;
import com.srnpr.xmaspay.common.TradeStatusEnum;
import com.srnpr.xmaspay.config.face.IPayGateWayCallBackConfig;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.response.PayGateWayCallBackResponse;
import com.srnpr.xmaspay.service.IPayGateWayCallBackFunc;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.DictManager;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 支付网关回调相关实现
 * @author pang_jhui
 *
 */
public class PayGateWayCallBackAspectImpl extends BaseClass implements IPayGateWayCallBackAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
		
//		PayGateWayCallBackRequest payGateWayCallBackRequest = (PayGateWayCallBackRequest) joinPoint.getArgs()[0];
//		
//		try {
//
//			MDataMap mDataMap = initOcpaymentMap(payGateWayCallBackRequest);
//			/* 保存交易记录 */
//			PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(mDataMap);
//
//			/* 若订单支付成功 */
//			if (StringUtils.equals(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(),
//					payGateWayCallBackRequest.getC_succmark())) {
//
//				/* 更新订单信息表已付金额 */
//				PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(
//						payGateWayCallBackRequest.getC_order(), payGateWayCallBackRequest.getC_orderamount());
//
//				List<MDataMap> orderInfos = PayServiceFactory.getInstance().getOrderPayService()
//						.getOrderInfoList(payGateWayCallBackRequest.getC_order());
//
//				for (MDataMap orderInfoMap : orderInfos) {
//
//					saveOrderPayInfo(orderInfoMap, payGateWayCallBackRequest);
//
//				}
//
//			}
//
//		} catch (ParseException e) {
//			bLogError(0, e.getMessage());
//		}
		

		
	}

	@Override
	public void doAfter(JoinPoint joinPoint) throws Exception {		
		
//		PayGateWayCallBackRequest payGateWayCallBackRequest = (PayGateWayCallBackRequest) joinPoint.getArgs()[0];
//	
//		if(StringUtils.equals(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(), payGateWayCallBackRequest.getC_succmark())){
//			
//			int payGate = payGateWayCallBackRequest.getC_paygate();
//			/*获取相关处理类*/
//			String className = DictManager.getPayGateChildInfo(Integer.toString(payGate), Constants.ZW_DEFINE_PAY_CALLBACK_FUNC);
//			
//			Class<?> payClass = ClassUtils.getClass(className);
//			
//			IPayGateWayCallBackFunc payGateWayCallBackFunc = (IPayGateWayCallBackFunc) payClass.newInstance();
//			
//			payGateWayCallBackFunc.doAfter(payGateWayCallBackRequest);
//			
//		}
		
		
	}
	
	/**
	 * 校验签名
	 * @param mDataMap
	 * 		输入的参数
	 * @param sourceSign
	 * 		需要校验的签名
	 * @return 签名校验是否通过
	 */
	public boolean doCheckSign(MDataMap mDataMap,String sourceSign,PayGateWayCallBackResponse payGateWayCallBackResponse){
		
		/*获取网关传送的签名*/
		String targetSign = mDataMap.get("c_signstr");
		
		boolean flag = true;
		
		/*签名校验未通过*/
		if(!StringUtils.equals(targetSign, sourceSign)){
			
			flag = false;
			
			payGateWayCallBackResponse.setResult(Constants.PAYGATEWAY_CALLBACK_RESULT_FAILURE);
			
			payGateWayCallBackResponse.setReURL(bInfo(964101002));
			
		}
		
		return flag;
		
	}

	@Override
	public PayGateWayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] args = joinPoint.getArgs();
		
		PayGateWayCallBackRequest payGateWayCallBackRequest = (PayGateWayCallBackRequest) args[0];
		
		IPayGateWayCallBackConfig payGateWayCallBackConfig = (IPayGateWayCallBackConfig) args[1];
		
		MDataMap mDataMap = BeanComponent.getInstance().objectToMap(payGateWayCallBackRequest,null,false);
		
		String sourceSign = payGateWayCallBackConfig.getSign(mDataMap);		
		
		PayGateWayCallBackResponse payGateWayCallBackResponse = new PayGateWayCallBackResponse();
		
		boolean flag = doCheckSign(mDataMap, sourceSign, payGateWayCallBackResponse);
		
		boolean validate_flag = true;
		
		/*签名校验通过*/
		if(flag){
			
			if(StringUtils.equals(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(), payGateWayCallBackRequest.getC_succmark())){
				
				
				String orderCode = payGateWayCallBackRequest.getC_order();
				
				MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
				
				if(isExistOrder(orderCode, orderMap, payGateWayCallBackResponse)){
					
					BigDecimal dueMoney = new BigDecimal(orderMap.get("due_money"));	
					
					if(!doCheckPayMoney(dueMoney, payGateWayCallBackRequest.getC_orderamount(), payGateWayCallBackResponse)){
						
						validate_flag = false;
						
					}
					
				}
				
			}
			
			
			if(validate_flag){
				
				payGateWayCallBackResponse = (PayGateWayCallBackResponse) joinPoint.proceed();
				
				if(payGateWayCallBackResponse.upFlagTrue()){
					
					// 操作成功后保存支付信息、更新订单状态
					savePayinfo(payGateWayCallBackRequest);
					updateOrderStatus(payGateWayCallBackRequest);
					
					/*同步支付信息*/
					PayServiceFactory.getInstance().getOrderPayService().createExecPayInfo(payGateWayCallBackRequest.getC_order());
					
					/*支付单号报关信息*/
					PayServiceFactory.getInstance().getOrderPayService().createExecOrderCustoms(payGateWayCallBackRequest.getC_order());
				}
				
			}
				
			
			
			
		}
		
		return payGateWayCallBackResponse;
		
	}
	
	/**
	 * 根据支付网关返回的请求信息保存支付信息
	 * @param payGateWayCallBackRequest
	 * 		支付网关回调请求信息
	 * @return 支付信息
	 * @throws ParseException 
	 */
	public MDataMap initOcpaymentMap(PayGateWayCallBackRequest payGateWayCallBackRequest) throws ParseException {

		MDataMap mdDataMap = new MDataMap();

		mdDataMap.put("out_trade_no", payGateWayCallBackRequest.getC_order());
		/*支付交易成功*/
		if (StringUtils.equalsIgnoreCase(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(),
				payGateWayCallBackRequest.getC_succmark())) {
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_SUCCESS.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_SUCCESS.getFlag_success());

		/*支付交易失败*/
		}else{
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_FAILURE.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_FAILURE.getFlag_success());
			
		}
		
		mdDataMap.put("sign_type", Constants.SIGN_TYPE_MD5);
		
		mdDataMap.put("sign", payGateWayCallBackRequest.getC_signstr());
		
		//mdDataMap.put("trade_no", payGateWayCallBackRequest.getC_transnum());
		mdDataMap.put("trade_no", StringUtils.isEmpty(payGateWayCallBackRequest.getBankorderid()) ? payGateWayCallBackRequest.getC_transnum() : payGateWayCallBackRequest.getBankorderid());
		
		mdDataMap.put("total_fee", payGateWayCallBackRequest.getC_orderamount().toString());
		
		Date dealDate = DateUtil.sdfDateTimeTamp.parse(payGateWayCallBackRequest.getDealtime());
		
		mdDataMap.put("gmt_payment", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		mdDataMap.put("gmt_create", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		JsonHelper<PayGateWayCallBackRequest> requestHelper = new JsonHelper<PayGateWayCallBackRequest>();
		
		mdDataMap.put("param_value", requestHelper.ObjToString(payGateWayCallBackRequest));
		
		mdDataMap.put("mark", Integer.toString(payGateWayCallBackRequest.getC_paygate()));
		
		mdDataMap.put("create_time", FormatHelper.upDateTime());
		
		mdDataMap.put("payment_code", WebHelper.upCode("PM"));
		
		return mdDataMap;


	}
	
	/**
	 * 判断订单是否存在
	 * @param orderCode
	 * 		订单编号
	 * @param mDataMap
	 * 		订单信息
	 * @param payGateWayCallBackResponse
	 * 		回调响应信息
	 * @return true|存在 false|不存在
	 */
	public boolean isExistOrder(String orderCode,MDataMap mDataMap,PayGateWayCallBackResponse payGateWayCallBackResponse){
		
		boolean flag = true;
		
		if(mDataMap == null){
			
			flag = false;
			
			payGateWayCallBackResponse.setResult(Constants.PAYGATEWAY_CALLBACK_RESULT_FAILURE);
			
			payGateWayCallBackResponse.setReURL(bInfo(964101004,orderCode));
			
		}
		
		return flag;
		
	}
	
	/**
	 * 校验支付金额是否与订单金额一致
	 * @param orderMoney
	 * 		订单金额
	 * @param payMoney
	 * 		支付金额
	 * @param payGateWayCallBackResponse
	 * 		支付网关响应信息
	 * @return true|金额一致 false|金额不一致
	 */
	public boolean doCheckPayMoney(BigDecimal orderMoney, BigDecimal payMoney,PayGateWayCallBackResponse payGateWayCallBackResponse){
		
		boolean flag = true;
		
		if(orderMoney.compareTo(payMoney) != 0){
			
			flag = false;
			
			payGateWayCallBackResponse.setResult(Constants.PAYGATEWAY_CALLBACK_RESULT_FAILURE);
			
			payGateWayCallBackResponse.setReURL(bInfo(964101003));
			
		}
		
		return flag;
		
	}
	
	
	public void saveOrderPayInfo(MDataMap orderInfo, PayGateWayCallBackRequest request) {

		/* 保存订单支付信息 */
		MDataMap orderPayInfo = initOrderPayInfo(orderInfo, request);

		PayServiceFactory.getInstance().getOrderPayService().saveOrderPayInfo(orderPayInfo);

		/* 更新订单信息表已付金额 */
		PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(orderInfo.get("order_code"),
				new BigDecimal(orderPayInfo.get("payed_money")));

	}
	
	/**
	 * 初始化订单支付信息
	 * @param request
	 * 		请求信息
	 * @return 订单支付信息
	 */
	public MDataMap initOrderPayInfo(MDataMap orderInfo, PayGateWayCallBackRequest request){
		
		MDataMap orderPayInfo = new MDataMap();
		
		orderPayInfo.put("order_code", orderInfo.get("order_code"));
		
		//orderPayInfo.put("pay_sequenceid", request.getC_transnum());
		orderPayInfo.put("pay_sequenceid", StringUtils.isEmpty(request.getBankorderid()) ? request.getC_transnum() : request.getBankorderid());
		
		
		orderPayInfo.put("payed_money", orderInfo.get("due_money"));
		
		orderPayInfo.put("create_time", FormatHelper.upDateTime());
		
		String payGateName = DictManager.getPayGateName(request.getC_paygate());
		
		String categoryCode = PayGateEnum.getCategoryCodeByName(payGateName);
		
		orderPayInfo.put("pay_type", categoryCode);
		
		if(orderInfo != null){
			
			orderPayInfo.put("merchant_id", orderInfo.get("buyer_code"));
			
		}
		
		orderPayInfo.put("payed_all_fee", orderInfo.get("due_money"));
		
		orderPayInfo.put("payed_fee", BigDecimal.ZERO.toString());
		
		orderPayInfo.put("status", Constants.OC_ORDER_PAY_STATUS_0);
		
		return orderPayInfo;
		
		
	}
	
	public void savePayinfo(PayGateWayCallBackRequest payGateWayCallBackRequest) {
		
		try {

			MDataMap mDataMap = initOcpaymentMap(payGateWayCallBackRequest);
			/* 保存交易记录 */
			PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(mDataMap);

			/* 若订单支付成功 */
			if (StringUtils.equals(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(),
					payGateWayCallBackRequest.getC_succmark())) {

				/* 更新订单信息表已付金额 */
				PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(
						payGateWayCallBackRequest.getC_order(), payGateWayCallBackRequest.getC_orderamount());

				List<MDataMap> orderInfos = PayServiceFactory.getInstance().getOrderPayService()
						.getOrderInfoList(payGateWayCallBackRequest.getC_order());

				for (MDataMap orderInfoMap : orderInfos) {

					saveOrderPayInfo(orderInfoMap, payGateWayCallBackRequest);

				}

			}

		} catch (ParseException e) {
			bLogError(0, e.getMessage());
		}
		

		
	}

	public void updateOrderStatus(PayGateWayCallBackRequest payGateWayCallBackRequest) throws Exception {		
		
		if(StringUtils.equals(TradeStatusEnum.TRADE_SUCCESS.getSuccess_mark(), payGateWayCallBackRequest.getC_succmark())){
			
			int payGate = payGateWayCallBackRequest.getC_paygate();
			/*获取相关处理类*/
			String className = DictManager.getPayGateChildInfo(Integer.toString(payGate), Constants.ZW_DEFINE_PAY_CALLBACK_FUNC);
			
			Class<?> payClass = ClassUtils.getClass(className);
			
			IPayGateWayCallBackFunc payGateWayCallBackFunc = (IPayGateWayCallBackFunc) payClass.newInstance();
			
			payGateWayCallBackFunc.doAfter(payGateWayCallBackRequest);
			
		}
		
		
	}

}
