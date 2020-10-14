package com.srnpr.xmaspay.aspect.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IApplePayCallBackAspect;
import com.srnpr.xmaspay.common.ApplePayEnum;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateEnum;
import com.srnpr.xmaspay.common.TradeStatusEnum;
import com.srnpr.xmaspay.config.ApplePayConfig;
import com.srnpr.xmaspay.request.ApplePayCallBackRequest;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.response.ApplePayCallBackResponse;
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
 * applePay回调相关实现
 * @author pang_jhui
 *
 */
public class ApplePayCallBackAspectImpl extends BaseClass implements IApplePayCallBackAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
		
//		ApplePayCallBackRequest callBackRequest = (ApplePayCallBackRequest) joinPoint.getArgs()[0];
//		
//		try {
//
//			MDataMap mDataMap = initOcpaymentMap(callBackRequest);
//			/* 保存交易记录 */
//			PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(mDataMap);
//
//			/* 若订单支付成功 */
//			if (StringUtils.equals(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())) {
//
//				/* 更新订单信息表已付金额 */
//				PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(
//						callBackRequest.getNo_order(), new BigDecimal(callBackRequest.getMoney_order()));
//
//				List<MDataMap> orderInfos = PayServiceFactory.getInstance().getOrderPayService()
//						.getOrderInfoList(callBackRequest.getNo_order());
//
//				for (MDataMap orderInfoMap : orderInfos) {
//
//					saveOrderPayInfo(orderInfoMap, callBackRequest);
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
		
//		ApplePayCallBackRequest callBackRequest = (ApplePayCallBackRequest) joinPoint.getArgs()[0];
//	
//		if(StringUtils.equals(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())){
//			
//			/*获取相关处理类*/
//			String className = DictManager.getPayGateChildInfo(PayGateEnum.APPLEPAY.getCode(), Constants.ZW_DEFINE_PAY_CALLBACK_FUNC);
//			
//			Class<?> payClass = ClassUtils.getClass(className);
//			
//			IPayGateWayCallBackFunc payGateWayCallBackFunc = (IPayGateWayCallBackFunc) payClass.newInstance();
//			
//			PayGateWayCallBackRequest payGateWayCallBackRequest = new PayGateWayCallBackRequest();
//			
//			payGateWayCallBackRequest.setC_order(callBackRequest.getNo_order());
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
	public boolean doCheckSign(String targetSign,String sourceSign,ApplePayCallBackResponse callBackResponse){
		
		boolean flag = true;
		
		/*签名校验未通过*/
		if(!StringUtils.equals(targetSign, sourceSign)){
			
			flag = false;
			
			callBackResponse.setRet_code(ApplePayEnum.FAILURE.getCode());
			
			callBackResponse.setRet_msg(bInfo(964101002));
			
		}
		
		return flag;
		
	}

	@Override
	public ApplePayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] args = joinPoint.getArgs();
		
		ApplePayCallBackRequest callBackRequest = (ApplePayCallBackRequest) args[0];
		
		ApplePayConfig applePayConfig = (ApplePayConfig) args[1];
		
		MDataMap mDataMap = BeanComponent.getInstance().objectToMap(callBackRequest,null,false);
		
		String sign = mDataMap.get("sign");
		
		String sourceSign = applePayConfig.getSign(mDataMap);		
		
		ApplePayCallBackResponse callBackResponse = new ApplePayCallBackResponse();
		
		// 先记录一条回调日志，设置状态为失败。后面的代码再签名验证成功后再保存一条成功的记录
		MDataMap dataMap = initOcpaymentMap(callBackRequest);
		dataMap.put("trade_status", TradeStatusEnum.TRADE_FAILURE.name());
		dataMap.put("flag_success", TradeStatusEnum.TRADE_FAILURE.getFlag_success());
		PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(dataMap);
		
		/*签名校验*/
		if(!doCheckSign(sign, sourceSign, callBackResponse)){
			return callBackResponse;
		}	
		
		// 忽略未支付成功的回调信息
		if(!StringUtils.equals(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())){
			return callBackResponse;
		}
			
		// 订单校验
		String orderCode = callBackRequest.getNo_order();
		MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		if(!isExistOrder(orderCode, orderMap, callBackResponse)){
			return callBackResponse;
		}
		
		// 忽略已支付的订单
		if(orderMap.get("payed_money") != null && new BigDecimal(orderMap.get("payed_money")).compareTo(new BigDecimal(orderMap.get("due_money"))) >= 0){
			callBackResponse.setRet_code("9999");
			callBackResponse.setRet_msg("重复请求");
			return callBackResponse;
		}
		
		// 金额校验
		BigDecimal dueMoney = new BigDecimal(orderMap.get("due_money"));	
		if(!doCheckPayMoney(dueMoney, new BigDecimal(callBackRequest.getMoney_order()), callBackResponse)){
			return callBackResponse;
		}
		
		/* 保存交易成功记录 */
		dataMap.put("trade_status", TradeStatusEnum.TRADE_SUCCESS.name());
		dataMap.put("flag_success", TradeStatusEnum.TRADE_SUCCESS.getFlag_success());
		PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(dataMap);
		
		// 签名验证成功后再执行后续操作
		// 保存支付信息
		savePayinfo(callBackRequest);
		// 更新订单状态
		updateOrderStatus(callBackRequest);
		
		callBackResponse = (ApplePayCallBackResponse) joinPoint.proceed();
			
		/*同步支付信息*/
		PayServiceFactory.getInstance().getOrderPayService().createExecPayInfo(callBackRequest.getNo_order());
			
		/*支付单号报关信息*/
		PayServiceFactory.getInstance().getOrderPayService().createExecOrderCustoms(callBackRequest.getNo_order());
		
		// 处理完成，返回成功信息
		callBackResponse.setRet_code("0000");
		callBackResponse.setRet_msg("交易成功");
		return callBackResponse;
		
	}
	
	/**
	 * 根据applePay返回的请求信息保存支付信息
	 * @param callBackRequest
	 * 		applePay回调请求信息
	 * @return 支付信息
	 * @throws ParseException 
	 */
	public MDataMap initOcpaymentMap(ApplePayCallBackRequest callBackRequest) throws ParseException {

		MDataMap mdDataMap = new MDataMap();

		mdDataMap.put("out_trade_no", callBackRequest.getNo_order());
		/*支付交易成功*/
		if (StringUtils.equalsIgnoreCase(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())) {
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_SUCCESS.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_SUCCESS.getFlag_success());

		/*支付交易失败*/
		}else{
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_FAILURE.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_FAILURE.getFlag_success());
			
		}
		
		mdDataMap.put("sign_type", Constants.SIGN_TYPE_MD5);
		
		mdDataMap.put("sign", callBackRequest.getSign());
		
		mdDataMap.put("trade_no", callBackRequest.getOid_paybill());
		
		mdDataMap.put("total_fee", callBackRequest.getMoney_order());
		
		Date dealDate = new Date();
		if(org.apache.commons.lang3.StringUtils.isNotBlank(callBackRequest.getSettle_date())){
			dealDate = DateUtil.sdfDateOnlyNoSp.parse(callBackRequest.getSettle_date());
		}
		
		mdDataMap.put("gmt_payment", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		mdDataMap.put("gmt_create", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		JsonHelper<ApplePayCallBackRequest> requestHelper = new JsonHelper<ApplePayCallBackRequest>();
		
		mdDataMap.put("param_value", requestHelper.ObjToString(callBackRequest));
		
		mdDataMap.put("mark", PayGateEnum.APPLEPAY.getCategoryCode());
		
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
	public boolean isExistOrder(String orderCode,MDataMap mDataMap,ApplePayCallBackResponse callBackResponse){
		
		boolean flag = true;
		
		if(mDataMap == null){
			
			flag = false;
			
			callBackResponse.setRet_code(ApplePayEnum.FAILURE.getCode());
			
			callBackResponse.setRet_msg(bInfo(964101004,orderCode));
			
		}
		
		return flag;
		
	}
	
	/**
	 * 校验支付金额是否与订单金额一致
	 * @param orderMoney
	 * 		订单金额
	 * @param payMoney
	 * 		支付金额
	 * @param ApplePayCallBackResponse
	 * 		支付网关响应信息
	 * @return true|金额一致 false|金额不一致
	 */
	public boolean doCheckPayMoney(BigDecimal orderMoney, BigDecimal payMoney,ApplePayCallBackResponse callBackResponse){
		
		boolean flag = true;
		
		if(orderMoney.compareTo(payMoney) != 0){
			
			flag = false;
			
			callBackResponse.setRet_code(ApplePayEnum.FAILURE.getCode());
			
			callBackResponse.setRet_msg(bInfo(964101003));
			
		}
		
		return flag;
		
	}
	
	
	public void saveOrderPayInfo(MDataMap orderInfo, ApplePayCallBackRequest request) {

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
	public MDataMap initOrderPayInfo(MDataMap orderInfo, ApplePayCallBackRequest request){
		
		MDataMap orderPayInfo = new MDataMap();
		
		orderPayInfo.put("order_code", orderInfo.get("order_code"));
		
		orderPayInfo.put("pay_sequenceid", request.getOid_paybill());
		
		orderPayInfo.put("payed_money", orderInfo.get("due_money"));
		
		orderPayInfo.put("create_time", FormatHelper.upDateTime());
		
		orderPayInfo.put("pay_type", PayGateEnum.APPLEPAY.getCategoryCode());
		
		if(orderInfo != null){
			
			orderPayInfo.put("merchant_id", orderInfo.get("buyer_code"));
			
		}
		
		orderPayInfo.put("payed_all_fee", orderInfo.get("due_money"));
		
		orderPayInfo.put("payed_fee", BigDecimal.ZERO.toString());
		
		orderPayInfo.put("status", Constants.OC_ORDER_PAY_STATUS_0);
		
		return orderPayInfo;
		
		
	}
	
	// 保存支付信息
	public void savePayinfo(ApplePayCallBackRequest callBackRequest) {
		
		/* 若订单支付成功 */
		if (StringUtils.equals(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())) {

			/* 更新订单信息表已付金额 */
			PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(
					callBackRequest.getNo_order(), new BigDecimal(callBackRequest.getMoney_order()));

			List<MDataMap> orderInfos = PayServiceFactory.getInstance().getOrderPayService()
					.getOrderInfoList(callBackRequest.getNo_order());

			for (MDataMap orderInfoMap : orderInfos) {

				saveOrderPayInfo(orderInfoMap, callBackRequest);

			}

		}
		
	}

	// 更新订单状态
	public void updateOrderStatus(ApplePayCallBackRequest callBackRequest) throws Exception {		
		
		if(StringUtils.equals(ApplePayEnum.SUCCESS.name(),callBackRequest.getResult_pay())){
			
			/*获取相关处理类*/
			String className = DictManager.getPayGateChildInfo(PayGateEnum.APPLEPAY.getCode(), Constants.ZW_DEFINE_PAY_CALLBACK_FUNC);
			
			Class<?> payClass = ClassUtils.getClass(className);
			
			IPayGateWayCallBackFunc payGateWayCallBackFunc = (IPayGateWayCallBackFunc) payClass.newInstance();
			
			PayGateWayCallBackRequest payGateWayCallBackRequest = new PayGateWayCallBackRequest();
			
			payGateWayCallBackRequest.setC_order(callBackRequest.getNo_order());
			
			payGateWayCallBackFunc.doAfter(payGateWayCallBackRequest);
			
		}
		
		
	}
}
