package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
* @ClassName: PlusModelTemplateAreaQuery 
* @Description: 根据模板编号获取相关城市
* @author 张海生
* @date 2015-12-22 下午3:13:19 
*  
*/
public class PlusModelTemplateAreaQuery extends QueryTop {

	@ZapcomApi(value = "模板编号", remark = "模板编号", require = 1)
	private String code = "";

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
