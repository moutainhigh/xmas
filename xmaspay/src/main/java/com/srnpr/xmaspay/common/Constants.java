package com.srnpr.xmaspay.common;

/**
 * 支付常量类
 * @author pang_jhui
 *
 */
public class Constants {	
	
	/**参数定义：支付接口*/
	public final static String ZW_DEFINE_CODE_PAYGATE = "46992324";
	
	/**支付网关回调缓存key*/
	public final static String CACHE_KEY_PAYGATEWAY_CALLBACK = "com.srnpr.xmaspay.service.PayGateWayCallBack";
	
	/**商户已经成功接受网关通知*/
	public final static String PAYGATEWAY_CALLBACK_RESULT_SUCESS = "1";
	
	/**商户已经成功接受网关通知*/
	public final static String PAYGATEWAY_CALLBACK_RESULT_FAILURE = "0";
	
	/**支付网关回调返回url*/
	public final static String ZW_DEFINE_RETURN_URL = "RETURN_URL";
	
	/**支付网关回调业务处理*/
	public final static String ZW_DEFINE_PAY_CALLBACK_FUNC = "PAY_CALLBACK_FUNC";
	
	/**订单支付状态：1已成功*/
	public final static String ORDER_PAY_STATUS_SUCCESS = "1";
	
	/**订单支付状态*/
	public final static String KEY_ORDER_PAY_STATUS = "ORDER_PAY_STATUS";
	
	/**小订单前缀*/
	public final static String ORDER_START_WITH_DD = "DD";
	
	/**大订单前缀*/
	public final static String ORDER_START_WITH_OS = "OS";
	
	/**支付网关是否将支付结果通知标识*/
	public final static String SERVER_RETURN_FLAG = "1";
	
	/**服务器路径*/
	public final static String SERVER_CONTEXT_PATH = "SERVER_CONTEXT_PATH";
	
	/**服务器通知方式*/
	public final static String PAYGATEWAY_NOTIFICE_TYPE = "1";
	
	/**订单语言*/
	public final static int ORDER_LANGUAGE = 0;
	
	/**签名类型：MD5*/
	public final static String SIGN_TYPE_MD5 = "MD5";
	
	/**订单支付类型：支付宝*/
	public final static String ORDER_PAY_TYPE_ALIPAY = "449746280003";
	
	/**订单支付类型：微信*/
	public final static String ORDER_PAY_TYPE_WECHAT = "449746280005";
	
	/**订单支付类型：ApplePay*/
	public final static String ORDER_PAY_TYPE_APPLEPAY = "449746280013";
	
	/**未读过*/
	public final static String OC_ORDER_PAY_STATUS_0 = "0";
	
	/**读过*/
	public final static String OC_ORDER_PAY_STATUS_1 = "1";
	
	/**执行类型：将支付信息同步至ld*/
	public final static String ZA_EXEC_TYPE_SYNC_PAYINFO_LD = "449746990001";
	
	/**执行类型：跨境通订单同步*/
	public final static String ZA_EXEC_TYPE_SYNC_KJT = "449746990003";
	
	/**执行类型：订单支付单号报关*/
	public final static String ZA_EXEC_TYPE_SYNC_CUSTOMS = "449746990005";
	
	/**支付宝响应*/
	public final static String ALIPAY_KEY_RESPONSE = "response";
	
	/**支付宝异常响应*/
	public final static String ALIPAY_KEY_ERROR_RESPONSE = "error_response";
	
	/**签名计算：参数分隔符*/
	public final static String SIGN_PARAM_SPLIT_AND = "&";
	
	/**是否成功标识：1成功*/
	public final static String FLAG_SUCCESS = "1";
	
	/**是否成功标识：0失败*/
	public final static String FLAG_FAILURE = "0";
	

}
