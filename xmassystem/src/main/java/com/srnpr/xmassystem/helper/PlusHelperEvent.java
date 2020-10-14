package com.srnpr.xmassystem.helper;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.top.XmasSystemConst;

public class PlusHelperEvent {

	/**
	 * 返回是否IC编号
	 * 
	 * @param sCode
	 * @return
	 */
	public static boolean checkEventItem(String sCode) {
		return StringUtils.startsWith(sCode, XmasSystemConst.EVENT_ITEM_START);
	}

	/**
	 * 返回是否扫码购或型录
	 * 
	 * @param sCode  
	 * @return
	 */
	public static boolean checkSmgOrDm(String sCode) {
		
		return  StringUtils.equals(sCode, XmasSystemConst.APPSMG_CODE) || StringUtils.equals(sCode, XmasSystemConst.SMG_CODE) || StringUtils.equals(sCode, XmasSystemConst.QD_CODE)  || StringUtils.equals(sCode, XmasSystemConst.DM_CODE) || StringUtils.equals(sCode, XmasSystemConst.BJTV_CODE);
	}

	/**
	 * 返回是否特定子活动
	 * 
	 * @param sCode
	 * @return
	 */
	public static String upSubEvent(String sCode) {
		String sReturn = "";

		if (StringUtils.contains(sCode, XmasSystemConst.EVENT_SPLIT_LINE)) {
			sReturn = StringUtils.substringBetween(sCode,
					XmasSystemConst.EVENT_SPLIT_LINE);
		}

		return sReturn;
	}

	/**
	 * 获取子活动下的SKU编号
	 * 
	 * @param sCode
	 * @return
	 */
	public static String upSubSkuCode(String sCode) {

		return StringUtils.substringAfterLast(sCode,
				XmasSystemConst.EVENT_SPLIT_LINE);

	}

	/**
	 * 返回状态
	 * 
	 * @param sStatusCode
	 * @return
	 */
	public static boolean checkEventStatus(String sStatusCode) {
		return sStatusCode.equals(XmasSystemConst.ACTIVE_EVENT_STATUS) || sStatusCode.equals(XmasSystemConst.ACTIVE_EVENT_STATUS_STOP);
	}

}
