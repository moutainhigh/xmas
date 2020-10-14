package com.srnpr.xmaspay.process.prepare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.config.PayGateConfig;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.util.PayGateUtils;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 请求支付网关获取支付信息基类
 * @author zhaojunling
 */
public abstract class PayGatePreparePayProcess<I extends PayGatePreparePayProcess.PaymentInput, R extends PayGatePreparePayProcess.PaymentResult> extends PreparePayProcess<I, R>{

	@Override
	protected R doProcess(I input) {
		R result = getResult();
		String bigOrderCode = input.bigOrderCode;
		
		MDataMap bigOrderInfo = payService.getOrderInfoSupper(bigOrderCode);
		
		Map<String,String> param = createPayGateParam(input);
		param.put("c_mid", XmasPayConfig.getPayGateMid());
		param.put("c_order", bigOrderCode);
		param.put("c_orderamount", new BigDecimal(bigOrderInfo.get("due_money")).toString());
		param.put("c_ymd", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		param.put("c_moneytype", "1");
		param.put("c_retflag", "1");
		param.put("c_returl", XmasPayConfig.getPayGateReturnUrl());
		param.put("notifytype", "1");
		param.put("c_version", "v2.0");
		
		if(input.memo1 != null){
			param.put("c_memo1", input.memo1);
		}
		if(input.memo2 != null){
			param.put("c_memo2", input.memo2);
		}
		
		// 如果设置了reurl则表示需要前台同步支付结果
		if(input.reurl != null){
			param.put("c_reurl", input.reurl);
			param.put("c_retflag", "2");
		}
		
		// 支付宝H5的需要设置c_reurl为失败会跳地址
		if(PayGateConfig.PAY_GATE_ALIPAY_H5.equals(param.get("c_paygate"))) {
			param.put("c_reurl", input.errurl);
		}
		
		// 兼容一下测试环境模拟支付未走请求时c_reurl直接取的异步通知输出的reURL情况
		if("761".equals(param.get("c_paygate"))) {
			param.put("c_memo1", input.reurl);
		}
		
		param.put("c_signstr", PayGateUtils.createSign(param, XmasPayConfig.getPayGatePass()));
		
		// 网页支付直接构造提交表单，非网页支付则用接口请求支付参数
		if(PaymentChannel.APP == input.payChannel 
				|| PaymentChannel.BARCODE == input.payChannel
				|| PaymentChannel.JSAPI_WXSS == input.payChannel){
			String responseText = null;
			
			MDataMap logMap = new MDataMap();
			logMap.put("action", "submitPayGate");
			logMap.put("target", bigOrderCode);
			logMap.put("method", "com.srnpr.xmaspay.process.prepare.PayGatePreparePayProcess#doProcess");
			logMap.put("request", JSON.toJSONString(param));
			logMap.put("request_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("exception", "");
			
			try {
				Map<String,String> headerMap = new HashMap<String, String>();
				if(StringUtils.isNotBlank(input.userAgent)) {
					headerMap.put("User-Agent", input.userAgent);
				}
				responseText = PayGateUtils.createOrder(param, headerMap, XmasPayConfig.getPayGateURL());
				
				logMap.put("response", responseText);
				logMap.put("response_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				result.setResultCode(0);
				result.setResultMessage("请求支付网关异常:"+e);
				return result;
			}
			
			// 解析网关返回参数
			prepareResult(input, result, responseText);
			
			// 记录请求支付网关的返回结果日志
			logMap.put("flag_success", result.getResultCode()+"");
			logMap.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			payService.saveXmasPayLog(logMap);
			
			if(result.getResultCode() != PaymentResult.SUCCESS){
				return result;
			}
		}else{
			result.payUrl = PayGateUtils.createPayGateUrl(param, XmasPayConfig.getPayGateURL());
		}
		
		return result;
	}
	
	/**
	 * 创建支付网关的请求参数,根据支付类型设置不同的支付网管参数
	 * @param input
	 * @return 设置好的参数集合map <br>
	 * 可能需要设置的参数: <br>
	 * c_paygate 支付方式 <br>
	 * c_paygate_type 支付接口类型 <br>
	 * c_paygate_account 接口类型账号<br>
	 * c_openid JSAPI支付时微信用户的OpenId<br>
	 * c_reurl js前台同步支付结果返回地址<br>
	 */
	protected abstract Map<String,String> createPayGateParam(I input); 
	
	/**
	 * 解析支付网关返回的参数,提取调起支付所需的参数
	 * @param responseText
	 * @return
	 */
	protected abstract void prepareResult(I input,R result,String responseText);
	
	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends PreparePayProcess.PaymentInput {
		/** 需要同步返回支付结果的地址 */
		public String reurl;
		/** 失败时返回的地址 */
		public String errurl;
		// 微信JSAPI支付时的用户openid
		public String openid;
		// 扩展参数一
		public String memo1;
		// 扩展参数二
		public String memo2;
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends PreparePayProcess.PaymentResult {
		// WEB页面跳转到支付网关支付
		public String payUrl;

	}
}
