package com.srnpr.xmasproduct.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class CollagePersonInfo {
	@ZapcomApi(value="昵称")
	private String nickname = "";
	@ZapcomApi(value="头像")
	private String headPhoto = "";
	@ZapcomApi(value="拼团编码")
	private String collageCode = "";
	@ZapcomApi(value="拼团活动结束时间")
	private String collageEndTime = "";
	@ZapcomApi(value="还差几人成团")
	private String needPersonCount = "";
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadPhoto() {
		return headPhoto;
	}
	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
	public String getCollageCode() {
		return collageCode;
	}
	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}
	public String getCollageEndTime() {
		return collageEndTime;
	}
	public void setCollageEndTime(String collageEndTime) {
		this.collageEndTime = collageEndTime;
	}
	public String getNeedPersonCount() {
		return needPersonCount;
	}
	public void setNeedPersonCount(String needPersonCount) {
		this.needPersonCount = needPersonCount;
	}
	
}
