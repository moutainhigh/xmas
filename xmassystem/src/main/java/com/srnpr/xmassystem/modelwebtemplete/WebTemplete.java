package com.srnpr.xmassystem.modelwebtemplete;

import java.util.List;
import java.util.ArrayList;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * web专题模板model
 * @author fq
 *
 */
public class WebTemplete {

	
	
	@ZapcomApi(value="多栏数量")
	private String column_num = "";

	@ZapcomApi(value="标题颜色")
	private String template_title_color = "";
	
	@ZapcomApi(value="标题选中色")
	private String template_title_color_selected = "";
	
	@ZapcomApi(value="模板选中色")
	private String template_backcolor_selected = "";
	
	@ZapcomApi(value = "模版类型")
	private String template_type = "";
	
	@ZapcomApi(value = "模版编号")
	private String template_number = "";
	
	@ZapcomApi(value = "模版背景色")
	private String template_backcolor = "";
	
	@ZapcomApi(value = "商品购买方式的图片")
	private String commodity_buy_picture = "";
	
	@ZapcomApi(value = "商品文本背景类型")
	private String commodity_text_picture = "";
	
	@ZapcomApi(value = "是否展示最低折扣")
	private String commodity_min_dis = "";
	
	@ZapcomApi(value = "模版图")
	private String commodity_picture = "";
	
	@ZapcomApi(value = "商品文本背景值")
	private String commodity_text_value = "";
	
	@ZapcomApi(value = "商品文本底色")
	private String commodity_text_color = "";
	
	@ZapcomApi(value = "商品文本背景图")
	private String commodity_text_pic = "";
	
	@ZapcomApi(value = "标题名称")
	private String template_title_name = "";
	
	@ZapcomApi(value="关联内容")
	private List<WebCommodity> commodList = new ArrayList<WebCommodity>();
	
	@ZapcomApi(value="售价价格颜色")
	private String sell_price_color = "";
	
	@ZapcomApi(value="拼团活动编号")
	private String event_code = "";
	
	@ZapcomApi(value="分隔条",remark="449748410001:不显示；449748410002:显示")
	private String split_bar = "";
	
	public String getColumn_num() {
		return column_num;
	}

	public void setColumn_num(String column_num) {
		this.column_num = column_num;
	}
	
	public String getTemplate_title_color() {
		return template_title_color;
	}

	public void setTemplate_title_color(String template_title_color) {
		this.template_title_color = template_title_color;
	}

	public String getTemplate_title_color_selected() {
		return template_title_color_selected;
	}

	public void setTemplate_title_color_selected(String template_title_color_selected) {
		this.template_title_color_selected = template_title_color_selected;
	}

	public String getTemplate_backcolor_selected() {
		return template_backcolor_selected;
	}

	public void setTemplate_backcolor_selected(String template_backcolor_selected) {
		this.template_backcolor_selected = template_backcolor_selected;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	public String getTemplate_number() {
		return template_number;
	}

	public void setTemplate_number(String template_number) {
		this.template_number = template_number;
	}

	public String getTemplate_backcolor() {
		return template_backcolor;
	}

	public void setTemplate_backcolor(String template_backcolor) {
		this.template_backcolor = template_backcolor;
	}

	public String getCommodity_buy_picture() {
		return commodity_buy_picture;
	}

	public void setCommodity_buy_picture(String commodity_buy_picture) {
		this.commodity_buy_picture = commodity_buy_picture;
	}

	public String getCommodity_text_picture() {
		return commodity_text_picture;
	}

	public void setCommodity_text_picture(String commodity_text_picture) {
		this.commodity_text_picture = commodity_text_picture;
	}

	public String getCommodity_min_dis() {
		return commodity_min_dis;
	}

	public void setCommodity_min_dis(String commodity_min_dis) {
		this.commodity_min_dis = commodity_min_dis;
	}

	public String getCommodity_picture() {
		return commodity_picture;
	}

	public void setCommodity_picture(String commodity_picture) {
		this.commodity_picture = commodity_picture;
	}

	public String getCommodity_text_value() {
		return commodity_text_value;
	}

	public void setCommodity_text_value(String commodity_text_value) {
		this.commodity_text_value = commodity_text_value;
	}

	public String getCommodity_text_color() {
		return commodity_text_color;
	}

	public void setCommodity_text_color(String commodity_text_color) {
		this.commodity_text_color = commodity_text_color;
	}

	public String getCommodity_text_pic() {
		return commodity_text_pic;
	}

	public void setCommodity_text_pic(String commodity_text_pic) {
		this.commodity_text_pic = commodity_text_pic;
	}

	public String getTemplate_title_name() {
		return template_title_name;
	}

	public void setTemplate_title_name(String template_title_name) {
		this.template_title_name = template_title_name;
	}

	public List<WebCommodity> getCommodList() {
		return commodList;
	}

	public void setCommodList(List<WebCommodity> commodList) {
		this.commodList = commodList;
	}

	public String getSell_price_color() {
		return sell_price_color;
	}

	public void setSell_price_color(String sell_price_color) {
		this.sell_price_color = sell_price_color;
	}

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getSplit_bar() {
		return split_bar;
	}

	public void setSplit_bar(String split_bar) {
		this.split_bar = split_bar;
	}
	
}
