package com.srnpr.xmaspay.common;

import org.apache.commons.lang.StringUtils;
/**
 * 支付网关
 * @author pang_jhui
 *
 */
public enum PayGateEnum {

	/**支付宝WEB*/
	ALIPAY_WEB("449747730001","449746280003"),
	
	/**支付宝WAP（H5）*/
	ALIPAY_WAP("449747730002","449746280003"),
	
	/**微信原生（二维码）*/
	WECHAT_QRCODE("449747730004","449746280005"),
	
	/**微信JSAPI（微信客户端内) */
 	WECHAT_JSONAPI("449747730005","449746280005"),
 	
 	/**微信支付*/
 	WECHAT_APP("449747730003","449746280005"),
 	
 	/**招商银行*/
 	BANK_CMB("CMB","449746280003"),
 	
 	/**建设银行*/
 	BANK_CCB("CCB","449746280003"),
 	
 	/**工商银行*/
 	BANK_ICBC("ICBC","449746280003"),
 	
 	/**兴业银行*/
 	BANK_CIB("CIB","449746280003"),
 	
 	/**交通银行*/
 	BANK_COMM("COMM","449746280003"),
 	
 	/**光大银行*/
 	BANK_CEB("CEB","449746280003"),
 	
 	/**北京农村商业银行*/
 	BANK_BJRCB("BJRCB","449746280003"),
 	
 	/**中国银行*/
 	BANK_BOC("BOC","449746280003"),
 	
 	/**农业银行*/
 	BANK_ABC("ABC","449746280003"),
 	
 	/**中信银行*/
 	BANK_ECITIC("ECITIC","449746280003"),
 	
 	/**平安银行*/
 	BANK_PINGAN("PINGAN","449746280003"),
 	
 	/**民生银行*/
 	BANK_CMBC("CMBC","449746280003"),
	
	/**applePay支付*/
	APPLEPAY("APPLEPAY","449746280013"),
	
	/**银联支付*/
	UNIONPAY("UNIONPAY","449746280014");
	
	/*支付方式编码*/
	private String code = "";
	
	/*支付信息分类*/
	private String categoryCode = "";
	
	PayGateEnum(String... args){
		
		if(args.length == 1){
			
			this.code = args[0];
			
		}
		
		if(args.length == 2){
			
			this.code = args[0];
			
			this.categoryCode = args[1];
			
		}
		
	}

	/**
	 * 获取支付方式编码
	 * @return 支付编码
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 获取支付信息分类编码
	 * @return 分类编码
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * 根据支付类型获取枚举名称
	 * @param payTypeId
	 * 		支付类型编码
	 * @return 枚举名称
	 */
	public static String getEnumNameByTypeCode(String payTypeId){
		
		PayGateEnum[] payGateEnums = PayGateEnum.values();
		
		String name = "";
		
		for (PayGateEnum payGateEnum : payGateEnums) {
			
			if(StringUtils.equals(payGateEnum.getCode(), payTypeId)){
				
				name = payGateEnum.name();
				
				break;
				
			}
			
		}
		
		return name;
		
	}
	
	/**
	 * 根据枚举类型获取分类代码
	 * @param enumName
	 * 		枚举类型
	 * @return 分类代码
	 */
	public static String getCategoryCodeByName(String enumName) {

		PayGateEnum[] payGateEnums = PayGateEnum.values();

		String categoryCode = "";

		for (PayGateEnum payGateEnum : payGateEnums) {

			if (StringUtils.equals(payGateEnum.name(), enumName)) {

				categoryCode = payGateEnum.getCategoryCode();

				break;

			}

		}

		return categoryCode;

	}

}
