package com.srnpr.xmaspay.util;

import java.util.Random;

public class WechatUtil {
	
	public static String getNonceStr() {
		
		Random random = new Random();
		
		return MessageDigestUtil.Md5EncodeUpper(String.valueOf(random.nextInt(10000)), null);
		
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
