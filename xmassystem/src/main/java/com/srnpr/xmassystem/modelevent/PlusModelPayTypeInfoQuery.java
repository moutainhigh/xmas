package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusQuery;

/**
 * 用于查询支付信息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月12日
 */
public class PlusModelPayTypeInfoQuery implements IPlusQuery {

	private String code = "payInfo";
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
