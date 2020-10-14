package com.srnpr.xmassystem.modelproduct;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**   
 * 	常见问题
*    ligj
*/
public class PlusModelCommonProblem {
	@ZapcomApi(value = "标题")
	private String title = "";
	@ZapcomApi(value = "内容")
	private String content = "";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}

