package com.srnpr.xmaspay.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日历计算工具类
 * @author pang_jhui
 *
 */
public class CalendarUtil {
	
	/**
	 * 日期计算
	 * @param date
	 * 		待计算日期
	 * @param diff
	 * 		差异值
	 * @param diffType
	 * 		字段类型（年、月、日、时、分、秒）
	 * @return 返回计算后日期
	 */
	public static Calendar calCalendar(Date date,int diff,int diffType){
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(diffType, diff);
		
		return calendar;
		
	}

}
