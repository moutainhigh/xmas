package com.srnpr.xmaspay.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.common.UnionPayConstant;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 银联支付工具类
 * @author pang_jhui
 *
 */
public class UnionPayUtil extends BaseClass {
	
	/**银联支付工具类实例*/
	private volatile static UnionPayUtil INSTANCE = null;
	
	/**
	 * 银联支付工具类实例
	 * @return
	 */
	public static UnionPayUtil getInstance(){
		
		if(INSTANCE == null){
			
			synchronized (UnionPayUtil.class) {
				
				if(INSTANCE == null){
					
					INSTANCE = new UnionPayUtil();
					
				}
				
			}
			
		}
		
		return INSTANCE;
		
	}
	
	/**
	 * 生成签名值(SHA1摘要算法)
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @return 签名是否成功
	 */
	public String sign(String signStr, String encoding) {
		if (StringUtils.isNotBlank(encoding)) {
			encoding = "UTF-8";
		}
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA1进行摘要并转16进制
			byte[] signDigest = UnionPaySecureUtil.getInstance().sha1X16(signStr, encoding);
			byteSign = UnionPaySecureUtil.getInstance().base64Encode(UnionPaySecureUtil.getInstance()
					.signBySoft(UnionPayCertUtil.getInstance().getSignCertPrivateKey(), signDigest));
			stringSign = new String(byteSign);
			
			return stringSign;
			
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 通过传入的证书绝对路径和证书密码读取签名证书进行签名并返回签名值<br>
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @param certPath
	 *            证书绝对路径
	 * @param certPwd
	 *            证书密码
	 * @return 签名值
	 */
	public boolean signByCertInfo(Map<String, String> data,
			 String certPath, String certPwd,String encoding) {
		if (StringUtils.isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		if (StringUtils.isEmpty(certPath) || StringUtils.isEmpty(certPwd)) {
			bLogError(0,"Invalid Parameter:CertPath=[" + certPath
					+ "],CertPwd=[" + certPwd + "]");
			return false;
		}
		// 设置签名证书序列号
		data.put(UnionPayConstant.PARAM_CERTID,
				UnionPayCertUtil.getInstance().getCertIdByKeyStoreMap(certPath, certPwd));
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			byte[] signDigest = UnionPaySecureUtil.getInstance().sha1X16(stringData, encoding);
			byteSign = UnionPaySecureUtil.getInstance().base64Encode(UnionPaySecureUtil.getInstance().signBySoft(
					UnionPayCertUtil.getInstance().getSignCertPrivateKeyByStoreMap(certPath, certPwd),
					signDigest));
			stringSign = new String(byteSign);
			// 设置签名域值
			data.put(UnionPayConstant.PARAM_SIGNATURE, stringSign);
			return true;
		} catch (Exception e) {
			bLogError(0, "签名异常:"+e.getMessage());
			return false;
		}
	}


	/**
	 * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (UnionPayConstant.PARAM_SIGNATURE.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + UnionPayConstant.EQUAL + en.getValue()
					+ UnionPayConstant.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}


	/**
	 * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public Map<String, String> coverResultString2Map(String result) {
		return convertResultStringToMap(result);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map =null;
		try {
			
			if(StringUtils.isNotBlank(result)){
				if(result.startsWith("{") && result.endsWith("}")){
					System.out.println(result.length());
					result = result.substring(1, result.length()-1);
				}
				 map = parseQString(result);
			}
			
		} catch (UnsupportedEncodingException e) {
			bLogError(0, e.getMessage());
		}
		return map;
	}

	
	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public Map<String, String> parseQString(String str)
			throws UnsupportedEncodingException {

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;//值里有嵌套
		char openName = 0;
		if(len>0){
			for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey) {// 如果当前生成的是key
					
					if (curChar == '=') {// 如果读取到=分隔符 
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else  {// 如果当前生成的是value
					if(isOpen){
						if(curChar == openName){
							isOpen = false;
						}
						
					}else{//如果没开启嵌套
						if(curChar == '{'){//如果碰到，就开启嵌套
							isOpen = true;
							openName ='}';
						}
						if(curChar == '['){
							isOpen = true;
							openName =']';
						}
					}
					if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					}else{
						temp.append(curChar);
					}
				}
				
			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
			String key, Map<String, String> map)
			throws UnsupportedEncodingException {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * @param contentData
	 * @return
	 */
	public Map<String, String> filterBlank(Map<String, String> contentData){
		
		Map<String, String> submitFromData = new HashMap<String, String>();
		Set<String> keyset = contentData.keySet();
		
		for(String key:keyset){
			String value = contentData.get(key);
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(key, value.trim());
			}
		}
		return submitFromData;
	}


}
