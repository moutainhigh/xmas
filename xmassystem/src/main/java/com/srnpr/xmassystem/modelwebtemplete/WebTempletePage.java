package com.srnpr.xmassystem.modelwebtemplete;

import java.util.List;
import java.util.ArrayList;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * web专题模板页model
 * @author fq
 *
 */
public class WebTempletePage implements IPlusModel{

	@ZapcomApi(value = "页面标题")
	private String title = "";

	@ZapcomApi(value = "页面编号")
	private String page_number = "";

	@ZapcomApi(value = "页面类型")
	private String dropdown_type = "";
	
	@ZapcomApi(value = "创建时间")
	private String create_time = "";
	
	@ZapcomApi(value = "是否分享")
	private String app_is_share = "";
	
	@ZapcomApi(value = "分享标题")
	private String share_title = "";
	
	@ZapcomApi(value = "分享内容")
	private String share_content = "";
	
	@ZapcomApi(value = "分享图片")
	private String share_img = "";
	
	@ZapcomApi(value = "分享链接")
	private String share_link = "";
	
	@ZapcomApi(value = "是否关联直播")
	private String is_rel_onlive = "";
	
	@ZapcomApi(value = "专题模板所属项目")
	private String project_ad = "";
	
	@ZapcomApi(value = "专题模板")
	private List<WebTemplete> templeteList = new ArrayList<WebTemplete>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

	public String getDropdown_type() {
		return dropdown_type;
	}

	public void setDropdown_type(String dropdown_type) {
		this.dropdown_type = dropdown_type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getApp_is_share() {
		return app_is_share;
	}

	public void setApp_is_share(String app_is_share) {
		this.app_is_share = app_is_share;
	}

	public String getShare_title() {
		return share_title;
	}

	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getShare_img() {
		return share_img;
	}

	public void setShare_img(String share_img) {
		this.share_img = share_img;
	}

	public String getShare_link() {
		return share_link;
	}

	public void setShare_link(String share_link) {
		this.share_link = share_link;
	}

	public String getIs_rel_onlive() {
		return is_rel_onlive;
	}

	public void setIs_rel_onlive(String is_rel_onlive) {
		this.is_rel_onlive = is_rel_onlive;
	}

	public String getProject_ad() {
		return project_ad;
	}

	public void setProject_ad(String project_ad) {
		this.project_ad = project_ad;
	}

	public List<WebTemplete> getTempleteList() {
		return templeteList;
	}

	public void setTempleteList(List<WebTemplete> templeteList) {
		this.templeteList = templeteList;
	}
	
	

}
