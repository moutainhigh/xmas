package com.srnpr.xmassystem.very;

import org.apache.commons.lang.StringUtils;

public class ImageCompressRun implements Runnable {
	
	private Integer width ;
	private String sImage;
	private String sFormat;
	private String baseKey;
	
	public ImageCompressRun(String baseKey,String sImage, Integer iWidth,
			String sFormat) {
		this.width = iWidth;
		this.sImage = sImage;
		this.sFormat = sFormat;
		this.baseKey = baseKey;
	}
	
	@Override
	public void run() {
		new PlusVeryImage().upImageZoom(baseKey, StringUtils.join(StringUtils.split(sImage, "|"), ",").replace(" ", ""), width, sFormat);
	}
		
	
}
