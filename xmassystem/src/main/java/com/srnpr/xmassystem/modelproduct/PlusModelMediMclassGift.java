package com.srnpr.xmassystem.modelproduct;

import java.io.Serializable;

/**
 * LD赠品媒体中分类
 * @author Administrator
 *
 */

public class PlusModelMediMclassGift implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 *分类ID
	 */
	private String MEDI_MCLSS_ID = "";
	/**
	 * 分类名称
	 */
	private String MEDI_MCLSS_NM="";
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMEDI_MCLSS_ID() {
		return MEDI_MCLSS_ID;
	}

	public void setMEDI_MCLSS_ID(String mEDI_MCLSS_ID) {
		MEDI_MCLSS_ID = mEDI_MCLSS_ID;
	}

	public String getMEDI_MCLSS_NM() {
		return MEDI_MCLSS_NM;
	}

	public void setMEDI_MCLSS_NM(String mEDI_MCLSS_NM) {
		MEDI_MCLSS_NM = mEDI_MCLSS_NM;
	}
	
	
}
