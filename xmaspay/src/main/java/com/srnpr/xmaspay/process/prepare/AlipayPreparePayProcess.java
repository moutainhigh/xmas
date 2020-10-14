package com.srnpr.xmaspay.process.prepare;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.config.PayGateConfig;

/**
 * 支付宝支付
 * @author zhaojunling
 */
public class AlipayPreparePayProcess extends PayGatePreparePayProcess<AlipayPreparePayProcess.PaymentInput, AlipayPreparePayProcess.PaymentResult> {

	@Override
	protected Map<String, String> createPayGateParam(PaymentInput input) {
		Map<String, String> map = new HashMap<String, String>();
		if(PaymentChannel.APP == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.ALIPAY_APP)+"");
			if(input.v > 0) {
				map.put("c_paygate", PayGateConfig.PAY_GATE_ALIPAY_APP);
			}
		}else if(PaymentChannel.WEB == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.ALIPAY_WEB)+"");
			if(input.v > 0) {
				map.put("c_paygate", PayGateConfig.PAY_GATE_ALIPAY_WEB);
			}
		}else if(PaymentChannel.WAP == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.ALIPAY_H5)+"");
			map.put("c_memo1", input.reurl);
			
			if(input.v > 0) {
				map.put("c_paygate", PayGateConfig.PAY_GATE_ALIPAY_H5);
				map.put("c_reurl", input.errurl);
			}
		}else if(PaymentChannel.WAPAPP == input.payChannel){
			map.put("c_paygate", "653");
			map.put("c_memo1", input.reurl);
			
			if(input.v > 0) {
				map.put("c_paygate", PayGateConfig.PAY_GATE_ALIPAY_APP);
				map.put("c_reurl", input.errurl);
			}
		}
		
		return map;
	}

	//{"resultcode":0,"resultmsg":"","APPParam":"_input_charset=\"utf-8\"&body=\"购买商品\"&notify_url=\"http:\/\/test.pay.caiyb.cn\/Notify\/alipay\/653\/1000447\"&out_trade_no=\"ALIPA00001081\"&payment_type=\"1\"&seller_id=\"ucar_app@ichsy.com\"&service=\"mobile.securitypay.pay\"&sign=\"g1SxmCOWhZoC7VWUTMfKIyou2K%2fBte87GAtbMbwVlK%2bjG931EXvxCIp%2bJyR8zQdPBR9dVQh77TFZZAW23kM9CfzzSK%2fhuLyRylrhiR4C3NIQwPfAglocJbXuUTbvhXG6BpI02HnlvRF9waibibvU8%2bE47lpC3Xo9MQ%2bqHRKAtCU%3d\"&sign_type=\"RSA\"&subject=\"购买商品\"&total_fee=\"0.01\""}
	@Override
	protected void prepareResult(PaymentInput input, PaymentResult result,String responseText) {
		JSONObject resp = null;
		try {
			resp = JSON.parseObject(responseText);
			if(!"0".equals(resp.getString("resultcode"))){
				result.setResultCode(0);
				result.setResultMessage(resp.getString("resultmsg"));
			}else{
				result.payInfo = StringUtils.trimToEmpty(resp.getString("APPParam"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("解析网关结果异常");
		}
	}

	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends PayGatePreparePayProcess.PaymentInput {
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends PayGatePreparePayProcess.PaymentResult {
		public String payInfo;
	}

	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
}
