package com.srnpr.xmaspay.config;

import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.xmaspay.util.MessageDigestUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付网关配置信息基类
 * @author pang_jhui
 *
 */
public class BasePayGateWayConfig extends BaseClass implements IPayConfig {
	
	/**
	 * 计算支付网关签名，采用MD5加密
	 * @param mDataMap
	 * 		计算参数
	 * @param fieldNames
	 * 		字段名称集合
	 * @return 签名
	 */
	public String calPayGateWaySign(MDataMap mDataMap, String[] fieldNames) {

		StringBuffer digestBuffer = new StringBuffer();

		for (String fileName : fieldNames) {

			String filedValue = mDataMap.get(fileName);

			if (StringUtils.isNotBlank(filedValue)) {

				digestBuffer.append(filedValue);

			}

		}

		return MessageDigestUtil.Md5Encode(digestBuffer.toString(), null);

	}

}
