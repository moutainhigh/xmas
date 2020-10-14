package com.srnpr.xmaspay.process.prepare;

import java.util.HashMap;
import java.util.Map;

import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.config.PayGateConfig;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess.PaymentInput;

/**
 * 银联支付
 * @author zhaojunling
 */
public class UnionPreparePayProcess extends PayGatePreparePayProcess<UnionPreparePayProcess.PaymentInput, UnionPreparePayProcess.PaymentResult> {

	@Override
	protected Map<String, String> createPayGateParam(PaymentInput input) {
		Map<String, String> map = new HashMap<String, String>();
		if(PaymentChannel.APP == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.UNION_PAY_APP)+"");
			// 重新定义支付渠道设置为WAP， 否则无法正常返回支付链接
			input.payChannel = PaymentChannel.WAP;  
		}else if(PaymentChannel.WEB == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.UNION_PAY_WEB)+"");
		}else if(PaymentChannel.WAP == input.payChannel){
			map.put("c_paygate", PayGateConfig.getPayGateVal(PayGateConfig.Type.UNION_PAY_H5)+"");
		}else if(PaymentChannel.WAPAPP == input.payChannel) {
			map.put("c_paygate", "623");
		}
		
		if(input.reurl != null){
			map.put("c_memo1", input.reurl);
		}
		
		return map;
	}

	@Override
	protected void prepareResult(PaymentInput input, PaymentResult result,String responseText) {}

	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends PayGatePreparePayProcess.PaymentInput {
	}
	
	/**
	 * 获取支付参数的输出对象
	 */
	public static class PaymentResult extends PayGatePreparePayProcess.PaymentResult {
	}

	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
}
