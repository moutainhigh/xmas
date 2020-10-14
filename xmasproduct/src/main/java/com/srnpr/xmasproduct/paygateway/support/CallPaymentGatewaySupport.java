package com.srnpr.xmasproduct.paygateway.support;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmasproduct.paygateway.model.RequestModel;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;

public class CallPaymentGatewaySupport {

	private static String PAYGATEWAY_ADDRESS = TopUp.upConfig("xmasproduct.paygateway_address");
	private static String PAYGATEWAY_APP_ID = TopUp.upConfig("xmasproduct.paygateway_app_id");
	private static String PAYGATEWAY_MD5_KEY = TopUp.upConfig("xmasproduct.paygateway_md5_key");
	private static String PAYGATEWAY_VERSION = TopUp.upConfig("xmasproduct.paygateway__version");
	
	public static String callGateway(RequestModel params) {
		String result = "";
		try {
			params.setApp_id(PAYGATEWAY_APP_ID);
			params.setCharset("UTF-8");
			params.setSign_type("MD5");
			params.setTimestamp(DateUtil.getSysDateTimeString());
			params.setVersion(PAYGATEWAY_VERSION);
			String sign = createMd5Sign(getSignChar(params.getBody()), PAYGATEWAY_MD5_KEY);
			params.setSign(sign);
			result = WebClientSupport.upPostJson(PAYGATEWAY_ADDRESS, JSON.toJSONString(params));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String getSignChar(Map<String, Object> body) {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> jsonMapEntry = body.entrySet();
		for(Entry<String, Object> entry : jsonMapEntry) {
			if(entry.getValue() == null) {
				continue;
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String createMd5Sign(String originChar, String md5Char) {
		String finalOrigin = "";
		if("".equals(originChar)) {
			finalOrigin = "md5key=" + md5Char;
		}else {
			finalOrigin = originChar + "&md5key=" + md5Char;
		}
		return DigestUtils.md5Hex(finalOrigin);
	}
}
