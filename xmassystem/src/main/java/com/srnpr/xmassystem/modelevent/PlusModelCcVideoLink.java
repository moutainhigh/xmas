package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/***
 * 视频播放地址
 */
public class PlusModelCcVideoLink extends IPlusAbstractModel implements IPlusModel {

	@Override
	protected int getCurrentVersion() {
		return 0;
	}
	
	private String videoUrl = "";

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
}
