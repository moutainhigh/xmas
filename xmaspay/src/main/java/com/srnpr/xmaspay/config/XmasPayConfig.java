package com.srnpr.xmaspay.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topcache.SimpleCache;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 支付模块配置获取类
 * 
 * @author zhaojunling
 */
public class XmasPayConfig{

	private static SimpleCache cache = new SimpleCache(new SimpleCache.Config(300,300,"XmasPayConfig",false));
	
	/**
	 * 获取支付网关的请求地址
	 * @return
	 */
	public static String getPayGateURL(){
		return getConfigMap().get("469923240021");
	}
	
	/**
	 * 获取支付网关的退款申请地址
	 * @return
	 */
	public static String getPayGateRefundURL(){
		return getConfigMap().get("469923240027");
	}
	
	/**
	 * 退款通知地址
	 * @return
	 */
	public static String getPayGateRefundNotifyURL(){
		return getConfigMap().get("469923240028");
	}
	
	/**
	 * 获取支付网关的商户编号
	 * @return
	 */
	public static String getPayGateMid(){
		return getConfigMap().get("469923240022");
	}
	
	/**
	 * 获取支付网关的商户密钥
	 * @return
	 */
	public static String getPayGatePass(){
		return getConfigMap().get("469923240023");
	}
	
	/**
	 * 短信支付页面回显地址
	 * @return
	 */
	public static String getPayGateSmsReURL(){
		return getConfigMap().get("469923240031");
	}
	
	/**
	 * LD支付成功页面后续支付页面回显地址
	 * @return
	 */
	public static String getPayGateLdSmsReURL(){
		return getConfigMap().get("469923240032");
	}
	
	/**
	 * 获取提供给支付网关的支付通知回调配置
	 * @return
	 */
	public static String getPayGateReturnUrl(){
		return getConfigMap().get("469923240024");
	}
	
	/**
	 * 默认返回为支付网关的跳转地址
	 * @return
	 */
	public static String getPayGateDefaultReURL(){
		return getConfigMap().get("469923240025");
	}
	
	/**
	 * 默认微信商城的跳转地址
	 * @return
	 */
	public static String getPayGateWapDefaultReURL(){
		return getConfigMap().get("469923240026");
	}

	/**

	 * 默认微信商城的失败跳转地址
	 * @return
	 */
	public static String getPayGateWapDefaultErrURL(){
		return getConfigMap().get("469923240030");
	}
	
	/**
	 * 微信小程序提现地址
	 * @return
	 */
	public static String getWeXinCashReUrl(){
		return getConfigMap().get("469923240029");
	}
	
	/**
	 * 查询连连支付平台的商户编号
	 * @return
	 */
	public static String getApplePayOidPartner(){
		return TopConfig.Instance.bConfig("applepay.oid_partner");
	}
	
	/**
	 * 苹果支付的merchant_id
	 * @return
	 */
	public static String getApplePayApMerchantId(){
		
		return TopConfig.Instance.bConfig("applepay.ap_merchant_id");
	}
	
	/**
	 * MD5签名使用的key
	 * @return
	 */
	public static String getApplePayKeyMd5(){
		return TopConfig.Instance.bConfig("applepay.key_md5");
	}
	
	/**
	 * 连连支付异步通知地址
	 * @return
	 */
	public static String getApplePayNotifyUrl(){
		return TopConfig.Instance.bConfig("applepay.notify_url_v2");
	}
	
//	private static String getZWDefineValue(String dids){
//		MDataMap map = DbUp.upTable("zw_define").one("define_dids", dids);
//		return map == null ? null : StringUtils.trimToEmpty(map.get("define_name"));
//	}
	
	private static MDataMap getConfigMap(){
		MDataMap map = cache.get("XmasPayConfig", new SimpleCache.Loader<MDataMap>() {
			@Override
			public MDataMap load() {
				List<MDataMap> dataList = DbUp.upTable("zw_define").queryByWhere("parent_did","46992324");
				MDataMap dataMap = new MDataMap();
				for(MDataMap m : dataList){
					dataMap.put(StringUtils.trimToEmpty(m.get("define_dids")), StringUtils.trimToEmpty(m.get("define_name")));
				}
				return dataMap;
			}
		});
		
		return map;
	}
}
