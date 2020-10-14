package com.srnpr.xmassystem.util;

public class XSSUtils {

	/**
	 * 检查内容是否包含执行脚本<br>
	 * 判断条件：内容包含src和=
	 * @param text
	 * @return
	 */
	public static boolean hasXSS(String text) {
		if(text == null) return false;
		text = text.toLowerCase();
		
		// 如果包含了src和=则默认为是包含了脚本代码
		return text.contains("src") && text.contains("=");
	}
}
