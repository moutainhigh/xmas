package com.srnpr.xmassystem.helper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class NumberHelper {

	
	/** 
	* @Description:数字取整
	* @param number 数字
	* @author 张海生
	* @date 2015-10-23 下午3:25:36
	* @return Object 
	* @throws 
	*/
	public static Object formatNumToInteger(Object number) {
		if(number == null || StringUtils.isEmpty(number.toString())){
			return number;
		}
		BigDecimal bg = new BigDecimal(String.valueOf(number));
		BigDecimal bgNum = bg.setScale(0,BigDecimal.ROUND_DOWN);
		if(number instanceof String){
			return bgNum.toString();
		}else if(number instanceof BigDecimal){
			return bgNum;
		}else if(number instanceof Double){
			return bgNum.doubleValue();
		}else if(number instanceof Float){
			return bgNum.floatValue();
		}
		return bgNum;
	}
}
