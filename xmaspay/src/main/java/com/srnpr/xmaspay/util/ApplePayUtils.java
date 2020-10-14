package com.srnpr.xmaspay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 苹果支付工具类
 */
public class ApplePayUtils {

	/**
	 * 创建签名参数
	 * @param param
	 * @param pass
	 */
	public static String createSign(Map<String,String> param, String keyMd5){
		List<String> list = new ArrayList<String>();
		
		Set<Entry<String, String>> entryList = param.entrySet();
		for(Entry<String, String> entry : entryList){
			if(entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())){
				list.add(entry.getKey()+"="+entry.getValue());
			}
		}
		
		Collections.sort(list);
		
		list.add("key="+keyMd5);
		String text = StringUtils.join(list,"&");
		
		return DigestUtils.md5Hex(text);
	}
	
	/**
	 * 验证苹果支付的回调签名是否正确
	 * @param param
	 * @param keyMd5
	 * @return
	 */
	public static boolean verifySign(Map<String,String> param, String keyMd5){
		Map<String,String> signMap = new HashMap<String, String>(param);
		String sign = (String)signMap.remove("sign");
		String expectSign = createSign(signMap, keyMd5);
		return expectSign.equalsIgnoreCase(sign);
	}
	
}
