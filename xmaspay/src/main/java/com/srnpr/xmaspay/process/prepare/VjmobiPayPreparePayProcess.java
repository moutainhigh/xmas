package com.srnpr.xmaspay.process.prepare;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess.PaymentInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 微匠支付
 * @author zhaojunling
 */
public class VjmobiPayPreparePayProcess extends PayGatePreparePayProcess<VjmobiPayPreparePayProcess.PaymentInput, VjmobiPayPreparePayProcess.PaymentResult> {

	@Override
	protected Map<String, String> createPayGateParam(PaymentInput input) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("c_paygate", input.paygate);
		map.put("c_openid", input.openid);
		map.put("c_paygate_type", "8");
		map.put("c_paygate_account", "4");

		// 微匠渠道需要提供收货地址字段
		if(StringUtils.isNotBlank(input.bigOrderCode)){
			String sql = "SELECT auth_address,area_code FROM oc_orderadress WHERE order_code IN(SELECT order_code FROM oc_orderinfo WHERE big_order_code = :big_order_code) LIMIT 1";
			Map<String, Object> address = DbUp.upTable("oc_orderadress").upTemplate().queryForMap(sql, new MDataMap("big_order_code", input.bigOrderCode));
			map.put("c_address", String.valueOf(address.get("auth_address")));
			map.put("c_bankarea", String.valueOf(address.get("area_code")));
		}
		
		return map;
	}

	@Override
	protected void prepareResult(PaymentInput input, PaymentResult result,String responseText) {
	}

	/**
	 * 获取支付请求输入对象
	 */
	public static class PaymentInput extends PayGatePreparePayProcess.PaymentInput {
		// 用户选择的银行
		public String paygate;
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
