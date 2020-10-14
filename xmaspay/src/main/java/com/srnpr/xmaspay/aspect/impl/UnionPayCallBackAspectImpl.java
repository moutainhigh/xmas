package com.srnpr.xmaspay.aspect.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.srnpr.xmaspay.aspect.IUnionPayCallBackAspect;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.PayGateEnum;
import com.srnpr.xmaspay.common.TradeStatusEnum;
import com.srnpr.xmaspay.common.UnionPayEnum;
import com.srnpr.xmaspay.request.PayGateWayCallBackRequest;
import com.srnpr.xmaspay.request.UnionPayBaseRequest;
import com.srnpr.xmaspay.request.UnionPayCallBackRequest;
import com.srnpr.xmaspay.response.UnionPayCallBackResponse;
import com.srnpr.xmaspay.service.IPayGateWayCallBackFunc;
import com.srnpr.xmaspay.service.impl.UnionPayServiceImpl;
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
public class UnionPayCallBackAspectImpl extends BaseClass implements IUnionPayCallBackAspect{

	@Override
	public void doBefore(JoinPoint joinPoint) {
		
		UnionPayCallBackRequest callBackRequest = (UnionPayCallBackRequest) joinPoint.getArgs()[0];
		
		try {

			MDataMap mDataMap = initOcpaymentMap(callBackRequest);
			/* 保存交易记录 */
			PayServiceFactory.getInstance().getOrderPayService().saveOcpaymentInfo(mDataMap);

			/* 若订单支付成功 */
			if (StringUtils.equals(UnionPayEnum.SUCCESS.getCode(),callBackRequest.getRespCode())) {

				/* 更新订单信息表已付金额 */
				PayServiceFactory.getInstance().getOrderPayService().updatePayedMoney(
						callBackRequest.getOrderId(), new BigDecimal(callBackRequest.getTxnAmt()));

				List<MDataMap> orderInfos = PayServiceFactory.getInstance().getOrderPayService()
						.getOrderInfoList(callBackRequest.getOrderId());

				for (MDataMap orderInfoMap : orderInfos) {

					saveOrderPayInfo(orderInfoMap, callBackRequest);

				}

			}

		} catch (ParseException e) {
			bLogError(0, e.getMessage());
		}
		

		
	}

	@Override
	public void doAfter(JoinPoint joinPoint) throws Exception {		
		
		UnionPayCallBackRequest callBackRequest = (UnionPayCallBackRequest) joinPoint.getArgs()[0];
	
		if(StringUtils.equals(UnionPayEnum.SUCCESS.getCode(), callBackRequest.getRespCode())){
			
			/*获取相关处理类*/
			String className = DictManager.getPayGateChildInfo(PayGateEnum.UNIONPAY.getCode(), Constants.ZW_DEFINE_PAY_CALLBACK_FUNC);
			
			Class<?> payClass = ClassUtils.getClass(className);
			
			IPayGateWayCallBackFunc payGateWayCallBackFunc = (IPayGateWayCallBackFunc) payClass.newInstance();
			
			PayGateWayCallBackRequest payGateWayCallBackRequest = new PayGateWayCallBackRequest();
			
			payGateWayCallBackRequest.setC_order(callBackRequest.getOrderId());
			
			payGateWayCallBackFunc.doAfter(payGateWayCallBackRequest);
			
		}
		
		
	}
	
	/**
	 * 校验签名
	 * @param mDataMap
	 * 		输入的参数
	 * @param sourceSign
	 * 		需要校验的签名
	 * @return 签名校验是否通过
	 */
	public boolean doCheckSign(MDataMap mDataMap,UnionPayCallBackResponse callBackResponse){
		
			
		boolean flag = false;
		try {
			
			flag = new UnionPayServiceImpl().validate(mDataMap, "UTF-8");
			
			/*签名校验未通过*/
			if(!flag){
				
				flag = false;
				
				callBackResponse.setResultcode(964101002);
				
				callBackResponse.setResultmsg(bInfo(964101002));
				
			}
			
		} catch (Exception e) {
			
			callBackResponse.setResultcode(-1);
			
			callBackResponse.setResultmsg("公钥及签名验证失败");
			
		}	
		
		return flag;
		
	}

	@Override
	public UnionPayCallBackResponse doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] args = joinPoint.getArgs();
		
		UnionPayCallBackRequest callBackRequest = (UnionPayCallBackRequest) args[0];
		
		MDataMap mDataMap = BeanComponent.getInstance().objectToMap(callBackRequest,UnionPayBaseRequest.class,false);	
		
		UnionPayCallBackResponse callBackResponse = new UnionPayCallBackResponse();
		
		boolean flag = doCheckSign(mDataMap, callBackResponse);
		
		boolean validate_flag = true;
		
		/*签名校验通过*/
		if(flag){
			
			if(StringUtils.equals(UnionPayEnum.SUCCESS.getCode(), callBackRequest.getRespCode())){
				
				
				String orderCode = callBackRequest.getOrderId();
				
				MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
				
				if(isExistOrder(orderCode, orderMap, callBackResponse)){
					
					BigDecimal dueMoney = new BigDecimal(orderMap.get("due_money"));	
					
					if(!doCheckPayMoney(dueMoney, new BigDecimal(callBackRequest.getTxnAmt()), callBackResponse)){
						
						validate_flag = false;
						
					}
					
				}
				
			}
			
			
			if(validate_flag){
				
				callBackResponse = (UnionPayCallBackResponse) joinPoint.proceed();
				
				if(callBackResponse.upFlagTrue()){
					
					PayGateWayCallBackRequest payGateWayCallBackRequest = new PayGateWayCallBackRequest();
					
					payGateWayCallBackRequest.setC_order(callBackRequest.getOrderId());
					
					/*同步支付信息*/
					PayServiceFactory.getInstance().getOrderPayService().createExecPayInfo(payGateWayCallBackRequest.getC_order());
					
					/*支付单号报关信息*/
					PayServiceFactory.getInstance().getOrderPayService().createExecOrderCustoms(payGateWayCallBackRequest.getC_order());
				}
				
			}
				
			
			
			
		}
		
		return callBackResponse;
		
	}
	
	/**
	 * 根据支付网关返回的请求信息保存支付信息
	 * @param UnionPayCallBackRequest
	 * 		支付网关回调请求信息
	 * @return 支付信息
	 * @throws ParseException 
	 */
	public MDataMap initOcpaymentMap(UnionPayCallBackRequest callBackRequest) throws ParseException {

		MDataMap mdDataMap = new MDataMap();

		mdDataMap.put("out_trade_no", callBackRequest.getOrderId());
		/*支付交易成功*/
		if (StringUtils.equalsIgnoreCase(UnionPayEnum.SUCCESS.getCode(),
				callBackRequest.getRespCode())) {
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_SUCCESS.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_SUCCESS.getFlag_success());

		/*支付交易失败*/
		}else{
			
			mdDataMap.put("trade_status", TradeStatusEnum.TRADE_FAILURE.name());
			
			mdDataMap.put("flag_success", TradeStatusEnum.TRADE_FAILURE.getFlag_success());
			
		}
		
		mdDataMap.put("sign_type", Constants.SIGN_TYPE_MD5);
		
		mdDataMap.put("sign", callBackRequest.getSignature());
		
		mdDataMap.put("trade_no", callBackRequest.getQueryId());
		
		mdDataMap.put("total_fee", callBackRequest.getTxnAmt());
		
		Date dealDate = new SimpleDateFormat("MMddHHmmss").parse(callBackRequest.getTraceTime());
		
		mdDataMap.put("gmt_payment", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		mdDataMap.put("gmt_create", DateUtil.toString(dealDate, DateUtil.DATE_FORMAT_DATETIME));
		
		JsonHelper<UnionPayCallBackRequest> requestHelper = new JsonHelper<UnionPayCallBackRequest>();
		
		mdDataMap.put("param_value", requestHelper.ObjToString(callBackRequest));
		
		mdDataMap.put("mark", PayGateEnum.UNIONPAY.getCategoryCode());
		
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
	 * @param unionPayCallBackResponse
	 * 		回调响应信息
	 * @return true|存在 false|不存在
	 */
	public boolean isExistOrder(String orderCode,MDataMap mDataMap,UnionPayCallBackResponse unionPayCallBackResponse){
		
		boolean flag = true;
		
		if(mDataMap == null){
			
			flag = false;
			
			unionPayCallBackResponse.setResultcode(964101004);
			
			unionPayCallBackResponse.setResultmsg(bInfo(964101004,orderCode));
			
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
	public boolean doCheckPayMoney(BigDecimal orderMoney, BigDecimal payMoney,UnionPayCallBackResponse callBackResponse){
		
		boolean flag = true;
		
		if(orderMoney.compareTo(payMoney) != 0){
			
			flag = false;
			
			callBackResponse.setResultcode(964101003);
			
			callBackResponse.setResultmsg(bInfo(964101003));
			
		}
		
		return flag;
		
	}
	
	
	public void saveOrderPayInfo(MDataMap orderInfo, UnionPayCallBackRequest request) {

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
	public MDataMap initOrderPayInfo(MDataMap orderInfo, UnionPayCallBackRequest request){
		
		MDataMap orderPayInfo = new MDataMap();
		
		orderPayInfo.put("order_code", orderInfo.get("order_code"));
		
		orderPayInfo.put("pay_sequenceid", request.getTxnAmt());
		
		orderPayInfo.put("payed_money", orderInfo.get("due_money"));
		
		orderPayInfo.put("create_time", FormatHelper.upDateTime());
		
		orderPayInfo.put("pay_type", PayGateEnum.UNIONPAY.getCategoryCode());
		
		if(orderInfo != null){
			
			orderPayInfo.put("merchant_id", orderInfo.get("buyer_code"));
			
		}
		
		orderPayInfo.put("payed_all_fee", orderInfo.get("due_money"));
		
		orderPayInfo.put("payed_fee", BigDecimal.ZERO.toString());
		
		orderPayInfo.put("status", Constants.OC_ORDER_PAY_STATUS_0);
		
		return orderPayInfo;
		
		
	}
	

}
