package com.srnpr.xmaspay.process.notify;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmaspay.PaymentProcess;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.util.PayGateUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topcache.SimpleCache;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 支付网关的支付完成通知处理
 * @author zhaojunling
 */
public class PayGateNotifyPayProcess extends NotifyPayProcess<PayGateNotifyPayProcess.PaymentInput, PayGateNotifyPayProcess.PaymentResult> implements PaymentProcess<PayGateNotifyPayProcess.PaymentInput, PayGateNotifyPayProcess.PaymentResult>{

	private static SimpleCache cache = new SimpleCache(new SimpleCache.Config(900,900,"paygate",false));
	
	@Override
	protected boolean verifyInput(PaymentInput input, PaymentResult result) {
		if(input.dataParam == null) input.dataParam = new HashMap<String, String>();
		
		if("v1.2".equals(input.dataParam.get("c_version"))){
			// 1.2版本签名固定参数
			StringBuilder build = new StringBuilder();
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_mid")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_order")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_orderamount")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_ymd")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_transnum")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_succmark")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_moneytype")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("dealtime")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_memo1")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_memo2")));
			build.append(StringUtils.trimToEmpty(input.dataParam.get("c_version")));
			build.append(XmasPayConfig.getPayGatePass());
			
			String signVal = DigestUtils.md5Hex(build.toString());
			if(!signVal.equalsIgnoreCase(input.dataParam.get("c_signstr"))){
				result.setResultMessage("签名错误V1:"+DigestUtils.md5Hex(StringUtils.trimToEmpty(XmasPayConfig.getPayGatePass()))+"["+PayGateUtils.getLocalAddress()+"]");
				return false;
			}
		}else{
			// 2.0版本签名使用全部参数
			if(!PayGateUtils.verifySign(input.dataParam, XmasPayConfig.getPayGatePass())){
				result.setResultMessage("签名错误V2:"+DigestUtils.md5Hex(StringUtils.trimToEmpty(XmasPayConfig.getPayGatePass()))+"["+PayGateUtils.getLocalAddress()+"]");
				return false;
			}
		}
		
		return true;
	}

	@Override
	protected NotifyPay getNotifyPay(PaymentInput input) {
		final String paygate = StringUtils.trimToEmpty(input.dataParam.get("c_paygate"));
		MDataMap dataMap = new MDataMap();
		
		if(StringUtils.isNotBlank(paygate)){
			dataMap = cache.get(paygate, new SimpleCache.Loader<MDataMap>() {
				@Override
				public MDataMap load() {
					return DbUp.upTable("oc_paygate").one("paygate", paygate);
				}
			});
			
			if(dataMap == null){
				dataMap = new MDataMap();
			}
		}
		
		NotifyPay item = new NotifyPay();
		item.success = "Y".equalsIgnoreCase(input.dataParam.get("c_succmark"));
		item.bigOrderCode = input.dataParam.get("c_order");
		item.orderPayType = StringUtils.trimToEmpty(dataMap.get("paytype"));
		item.orderPayBank = StringUtils.trimToEmpty(dataMap.get("paybank"));
		item.tradeNo = input.dataParam.get("bankorderid");
		item.payedMoney = input.dataParam.get("c_orderamount");
		return item;
	}

	@Override
	protected PaymentResult warpResult(PaymentInput input,PaymentResult result) {
		result.reURL = XmasPayConfig.getPayGateDefaultReURL();
		if(input != null && input.dataParam != null){
			if(StringUtils.isNotBlank(input.dataParam.get("c_memo1"))){
				result.reURL = input.dataParam.get("c_memo1");
			}
		}
		return result;
	}
	
	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
	
	@Override
	protected String getFromType() {
		return "paygate";
	}

	@Override
	protected void savePayment(PaymentInput input, NotifyPay notify) {
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("c_mid", StringUtils.trimToEmpty(input.dataParam.get("c_mid")));
		mDataMap.put("c_order", StringUtils.trimToEmpty(input.dataParam.get("c_order")));
		mDataMap.put("c_orderamount", StringUtils.trimToEmpty(input.dataParam.get("c_orderamount")));
		mDataMap.put("c_ymd", StringUtils.trimToEmpty(input.dataParam.get("c_ymd")));
		mDataMap.put("c_moneytype", StringUtils.trimToEmpty(input.dataParam.get("c_moneytype")));
		mDataMap.put("dealtime", StringUtils.trimToEmpty(input.dataParam.get("dealtime")));
		mDataMap.put("c_transnum", StringUtils.trimToEmpty(input.dataParam.get("c_transnum")));
		mDataMap.put("c_succmark", StringUtils.trimToEmpty(input.dataParam.get("c_succmark")));
		mDataMap.put("c_cause", StringUtils.trimToEmpty(input.dataParam.get("c_cause")));
		mDataMap.put("c_memo1", StringUtils.trimToEmpty(input.dataParam.get("c_memo1")));
		mDataMap.put("c_memo2", StringUtils.trimToEmpty(input.dataParam.get("c_memo2")));
		mDataMap.put("c_signstr", StringUtils.trimToEmpty(input.dataParam.get("c_signstr")));
		mDataMap.put("c_paygate", StringUtils.trimToEmpty(input.dataParam.get("c_paygate")));
		mDataMap.put("c_version", StringUtils.trimToEmpty(input.dataParam.get("c_version")));
		mDataMap.put("bankorderid", StringUtils.trimToEmpty(input.dataParam.get("bankorderid")));
		mDataMap.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		payService.savePayGatePayment(mDataMap);
	}
	
	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends NotifyPayProcess.PaymentInput {
		/** 支付网关通知参数 */
		public Map<String,String> dataParam;

		@Override
		public String getBigOrderCode() {
			return dataParam == null ? null : StringUtils.trimToEmpty(dataParam.get("c_order"));
		}
	}

	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends NotifyPayProcess.PaymentResult {
		// 处理结果页面
		public String reURL;
	}
}
