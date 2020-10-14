package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 赠品信息
 * @author shiyz
 *
 */
public class PlusModelGiftSkuinfo {

	
	@ZapcomApi(value="赠品编号")
	private String good_id = "";
	
	@ZapcomApi(value="商品样式")
	private String style_id="";
	
	@ZapcomApi(value="商品颜色")
	private String color_id = "";
	
	@ZapcomApi(value="商品名称")
	private String good_nm = "";
	
	@ZapcomApi(value="活动编号")
	private String event_id = "";
	
	@ZapcomApi(value="赠品类别")
	private String gift_cd = "";
	
	@ZapcomApi(value="有效期开始时间")
	private String fr_date = "";
	
	@ZapcomApi(value="有效期结束时间")
	private String end_date = "";
	
	@ZapcomApi(value="媒体中分类(通路)，2：网站，34：APP通路，39：扫码购，42：微信商城")
	private List<PlusModelMediMclassGift> medi_mclss_nm = new ArrayList<PlusModelMediMclassGift>();
	
	public String getGood_id() {
		return good_id;
	}
	public void setGood_id(String good_id) {
		this.good_id = good_id;
	}
	public String getStyle_id() {
		return style_id;
	}
	public void setStyle_id(String style_id) {
		this.style_id = style_id;
	}
	public String getColor_id() {
		return color_id;
	}
	public void setColor_id(String color_id) {
		this.color_id = color_id;
	}
	public String getGood_nm() {
		return good_nm;
	}
	public void setGood_nm(String good_nm) {
		this.good_nm = good_nm;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getGift_cd() {
		return gift_cd;
	}
	public void setGift_cd(String gift_cd) {
		this.gift_cd = gift_cd;
	}
	public String getFr_date() {
		return fr_date;
	}
	public void setFr_date(String fr_date) {
		this.fr_date = fr_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public List<PlusModelMediMclassGift> getMedi_mclss_nm() {
		return medi_mclss_nm;
	}
	public void setMedi_mclss_nm(List<PlusModelMediMclassGift> medi_mclss_nm) {
		this.medi_mclss_nm = medi_mclss_nm;
	}
	
}
