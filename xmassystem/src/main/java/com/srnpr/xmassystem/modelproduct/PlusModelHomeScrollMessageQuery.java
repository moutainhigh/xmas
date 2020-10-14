package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 用于查询首页滚动消息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月22日
 */
public class PlusModelHomeScrollMessageQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "写死一个")
	private String code = "home";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
