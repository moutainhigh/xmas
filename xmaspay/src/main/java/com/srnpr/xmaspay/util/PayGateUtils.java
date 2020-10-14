package com.srnpr.xmaspay.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 支付网关工具类
 */
public class PayGateUtils {

	/**
	 * 创建签名参数
	 * @param param
	 * @param pass
	 */
	public static String createSign(Map<String,String> param, String pass){
		List<String> list = new ArrayList<String>();
		
		Set<Entry<String, String>> entryList = param.entrySet();
		for(Entry<String, String> entry : entryList){
			if(StringUtils.isNotBlank(entry.getValue())){
				list.add(entry.getKey()+"="+entry.getValue());
			}
		}
		
		Collections.sort(list);
		
		list.add(pass);
		String text = StringUtils.join(list,"");
		
		return DigestUtils.md5Hex(text);
	}
	
	/**
	 * 验证签名
	 */
	public static boolean verifySign(Map<String,String> param, String pass){
		Map<String,String> signMap = new HashMap<String, String>(param);
		String signstr = (String)signMap.remove("c_signstr");
		String expectSign = createSign(signMap, pass);
		return expectSign.equalsIgnoreCase(signstr);
	}
	
	/**
	 * 创建支付网关的支付地址
	 * @param param
	 * @param gateUrl
	 * @return url 支付网关创建订单地址
	 */
	public static String createPayGateUrl(Map<String,String> param, String gateUrl){
		Set<Entry<String, String>> entryList = param.entrySet();
		
		List<String> paramList = new ArrayList<String>();
		for(Entry<String, String> entry : entryList){
			if(StringUtils.isNotBlank(entry.getValue())){
				try {
					paramList.add(entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		
		return gateUrl + "?" + StringUtils.join(paramList,"&");
	}
	
	/**
	 * 请求支付网管获取支付参数
	 * @param param
	 * @param gateUrl
	 * @return
	 * @throws Exception 
	 */
	public static String createOrder(Map<String,String> param, Map<String,String> headerMap,String gateUrl) throws Exception{
		MDataMap header = new MDataMap("Content-Type","application/x-www-form-urlencoded");
		
		if(headerMap != null && !headerMap.isEmpty()) {
			for(Entry<String, String> entry : headerMap.entrySet()){
				if(StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
					header.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return WebClientSupport.upPost(gateUrl, new MDataMap(new HashMap<String, Object>(param)), header);
	}
	
	/**
	 * 取本机IP地址和计算机名
	 * @return
	 */
	public static String getLocalAddress(){
		try {
			return InetAddress.getLocalHost().getHostName()+" "+InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "unknown";
		}
	}
}
