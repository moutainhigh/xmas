package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商品活动标签
 */
public class TagInfo {
	
	public static enum Style {
		
		Normal("0"),
		VipCard("1");

		private final String val; 
		
		private Style(String v) {
			this.val = v;
		}
		
		@Override
		public String toString() {
			return val;
		}
	}
	

	@ZapcomApi(value = "标签名称", remark = "秒杀、闪购、会员日")
	private String name = "";
	@ZapcomApi(value = "标签样式", remark = "0: 默认样式、1: 橙意会员卡样式")
	private String style = Style.Normal.toString();
	
	public TagInfo() {
		super();
	}
	
	public TagInfo(String name, String styleVal) {
		super();
		this.name = name;
		this.style = styleVal;
	}
	
	public TagInfo(String name, Style style) {
		super();
		this.name = name;
		this.style = style.val;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	
}
