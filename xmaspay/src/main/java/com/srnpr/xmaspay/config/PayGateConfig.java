package com.srnpr.xmaspay.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关接口配置
 */
public class PayGateConfig {

	private static Map<Type,Integer> payGateValMap = new HashMap<Type, Integer>();
	private static Map<Integer,Type> payGateEnumMap = new HashMap<Integer,Type>();
	
	/** 支付宝APP(新版参数) */
	public static String PAY_GATE_ALIPAY_APP = "663";
	/** 支付宝PC(新版参数) */
	public static String PAY_GATE_ALIPAY_WEB = "66";
	/** 支付宝H5(新版参数) */
	public static String PAY_GATE_ALIPAY_H5 = "664";
	
	public static enum Type {
		/** 支付宝WEB */
		ALIPAY_WEB,
		/** 支付宝WAP（H5） */
		ALIPAY_WAP,
		/** 支付宝移动支付（APP） */
		ALIPAY_APP,
		/** 支付宝H5版移动支付（H5） */
		ALIPAY_H5,
		/** 微信原生（二维码） */
		WECHAT_BARCODE,
		/** 微信WAP支付 */
		WECHAT_WAP,
		/** 微信JSAPI（微信客户端内） */
		WECHAT_JSAPI,
		/** 微信APP支付 （自身的APP内） */
		WECHAT_APP,
		/** 银联支付 (PC) */
		UNION_PAY_WEB,
		/** 银联支付 (移动应用) */
		UNION_PAY_APP,
		/** 银联支付(手机网站) */
		UNION_PAY_H5,
		/** 微匠支付 */
		VJMOBI_PAY_H5,
		/** 银联支付-分期(H5) */
		UNION_PAY_FENQI_H5,
		/** 银联支付-分期(APP) */
		UNION_PAY_FENQI_APP,
	}
	
	static {
		payGateValMap.put(Type.ALIPAY_WEB, 65);
		payGateEnumMap.put(65, Type.ALIPAY_WEB);
		
		// 以下类型通过支付宝WEB间接支持
		payGateEnumMap.put(77, Type.ALIPAY_WEB);  // 银联
		payGateEnumMap.put(73, Type.ALIPAY_WEB); // 财付通
		payGateEnumMap.put(1, Type.ALIPAY_WEB); // 招商银行
		payGateEnumMap.put(2, Type.ALIPAY_WEB); // 建设银行
		payGateEnumMap.put(3, Type.ALIPAY_WEB); // 工商银行
		payGateEnumMap.put(42, Type.ALIPAY_WEB); // 兴业银行
		payGateEnumMap.put(47, Type.ALIPAY_WEB); // 交通银行
		payGateEnumMap.put(48, Type.ALIPAY_WEB); // 光大银行
		payGateEnumMap.put(49, Type.ALIPAY_WEB); // 北京农村商业银行
		payGateEnumMap.put(69, Type.ALIPAY_WEB); // 中国银行
		payGateEnumMap.put(86, Type.ALIPAY_WEB); // 农业银行
		payGateEnumMap.put(70, Type.ALIPAY_WEB); // 中信银行
		payGateEnumMap.put(94, Type.ALIPAY_WEB); // 平安银行
		payGateEnumMap.put(901, Type.ALIPAY_WEB); // 民生银行
		// 支付宝间接支持
		
		payGateValMap.put(Type.ALIPAY_WAP, 651);
		payGateEnumMap.put(651, Type.ALIPAY_WAP);
		
		payGateValMap.put(Type.ALIPAY_APP, 653);
		payGateEnumMap.put(653, Type.ALIPAY_APP);
		
		payGateValMap.put(Type.ALIPAY_H5, 654);
		payGateEnumMap.put(654, Type.ALIPAY_H5);
		
		payGateValMap.put(Type.WECHAT_BARCODE, 76);
		payGateEnumMap.put(76, Type.WECHAT_BARCODE);
		
		payGateValMap.put(Type.WECHAT_WAP, 761);
		payGateEnumMap.put(761, Type.WECHAT_WAP);
		
		payGateValMap.put(Type.WECHAT_JSAPI, 762);
		payGateEnumMap.put(762, Type.WECHAT_JSAPI);
		
		payGateValMap.put(Type.WECHAT_APP, 763);
		payGateEnumMap.put(763, Type.WECHAT_APP);
		
		payGateValMap.put(Type.UNION_PAY_WEB, 62);
		payGateEnumMap.put(62, Type.UNION_PAY_WEB);
		
		payGateValMap.put(Type.UNION_PAY_APP, 623);
		payGateEnumMap.put(623, Type.UNION_PAY_APP);
		
		payGateValMap.put(Type.UNION_PAY_H5, 624);
		payGateEnumMap.put(624, Type.UNION_PAY_H5);
		
		payGateValMap.put(Type.UNION_PAY_FENQI_H5, 631);
		payGateEnumMap.put(631, Type.UNION_PAY_FENQI_H5);
		
		payGateValMap.put(Type.UNION_PAY_FENQI_APP, 633);
		payGateEnumMap.put(633, Type.UNION_PAY_FENQI_APP);
		
		/** 平安银行H5（微匠） */
		payGateEnumMap.put(942, Type.VJMOBI_PAY_H5);
		/** 浦发银行H5（微匠） */
		payGateEnumMap.put(431, Type.VJMOBI_PAY_H5);
	}
	
	/**
	 * 获取网关的支付方式
	 * @param type
	 * @return
	 */
	public static int getPayGateVal(Type type){
		Integer v = payGateValMap.get(type);
		return v == null ? 0 : v;
	}
	
	/**
	 * 获取网关的支付方式枚举
	 * @param type
	 * @return
	 */
	public static Type getPayGateEnum(int gate){
		return payGateEnumMap.get(gate);
	}
	
}
