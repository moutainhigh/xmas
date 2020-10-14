package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * @descriptions 商品标签。此类作为LoadProductLabelInfo.java的返回结果类。
 * @author ligj
 * @version 1.0.0 
 * 
 * @refactor 根据3.9.6需求，增加infoActivityPic/startTime/endTime 三个字段
 * 					 这三个字段在数据库表 pc_product_labels 中也有增加
 * @author Yangcl
 * @date 2016-5-6-下午2:43:10
 * @version 1.0.1
 */
public class PlusModelProductLabel implements IPlusModel{

	@ZapcomApi(value="标签编号")
    private String labelCode = "";

	@ZapcomApi(value="标签名称")
    private String labelName = "";

	@ZapcomApi(value="商品列表图片")
    private String listPic = "";

	@ZapcomApi(value="商品详情图片")
    private String infoPic = "";

	@ZapcomApi(value="是否可用",remark="1：可用，0：不可用")
    private Integer flagEnable=1;
    
	@ZapcomApi(value="商品详情活动标签" , remark="接口返回eventLabelPic  396需求 活动图片，比如618活动的横条") 
	private String infoActivityPic="";
	
	@ZapcomApi(value="开始时间")
	private String startTime=""; 
	
	@ZapcomApi(value="结束时间")
	private String endTime="";
	
	@ZapcomApi(value="活动图片跳转链接" , remark="396需求 活动图片跳转链接")
	private String eventLabelPicSkip = "";
	
	@ZapcomApi(value="标签自定义位置" , remark="449748430001.左上 449748430005.通栏下  ")
	private String labelPosition = "";
	
	@ZapcomApi(value="排除直播品" , remark="449747110002 是 449747110001 否  ")
	private String excludeLive = "";
	
	@ZapcomApi(value="更新时间")
	private String updateTime=""; 

	@ZapcomApi(value="商品列表图片宽")
    private long width;
	
	@ZapcomApi(value="商品列表图片高")
    private long height;

	public String getExcludeLive() {
		return excludeLive;
	}

	public void setExcludeLive(String excludeLive) {
		this.excludeLive = excludeLive;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getEventLabelPicSkip() {
		return eventLabelPicSkip;
	}

	public void setEventLabelPicSkip(String eventLabelPicSkip) {
		this.eventLabelPicSkip = eventLabelPicSkip;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getListPic() {
		return listPic;
	}

	public void setListPic(String listPic) {
		this.listPic = listPic;
	}

	public String getInfoPic() {
		return infoPic;
	}

	public void setInfoPic(String infoPic) {
		this.infoPic = infoPic;
	}

	public Integer getFlagEnable() {
		return flagEnable;
	}

	public void setFlagEnable(Integer flagEnable) {
		this.flagEnable = flagEnable;
	}

	public String getInfoActivityPic() {
		return infoActivityPic;
	}

	public void setInfoActivityPic(String infoActivityPic) {
		this.infoActivityPic = infoActivityPic;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
}