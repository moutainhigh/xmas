package com.srnpr.xmassystem.load;


import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelCcVideoLink;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * CC视频播放地址缓存
 */
public class LoadCcVideoLink extends LoadTop<PlusModelCcVideoLink, PlusModelQuery> {

	static String apiKey = "2PTwX2QNHSghSm8UoagS32s5bQG7I1hR";
	static String userId = "7408202D39A123FF";
	
	@Override
	public PlusModelCcVideoLink topInitInfo(PlusModelQuery tQuery) {
		PlusModelCcVideoLink plusModel = new PlusModelCcVideoLink();
		
		String url = getCcVideoLink(StringUtils.trimToEmpty(tQuery.getCode()));
		if(url != null) {
			plusModel.setVideoUrl(url);
		}
		
		return plusModel;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	private String getCcVideoLink(String ccvid) {
		String queryString = "format=json&userid=" + userId + "&videoid=" + ccvid + "&time=" + new Date().getTime();
		String hash = DigestUtils.md5Hex(queryString + "&salt=" + apiKey);
		String url = "http://union.bokecc.com/api/mobile?" + queryString + "&hash=" + hash;
		
		String responseText = "";
		try {
			responseText = WebClientSupport.create().doGet(url);
			if(responseText != null && (!responseText.startsWith("{") || responseText.contains("\"error\""))) {
				LogFactory.getLog(getClass()).warn("LoadCcVideoLink#getCcVideoLink Error!: "+responseText+"("+url+")");
				responseText = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if(StringUtils.isBlank(responseText)) {
			return null;
		}
		
		String playurl = null;
		try {
			JSONObject obj = JSON.parseObject(responseText);
			playurl = obj.getJSONObject("video").getJSONArray("copy").getJSONObject(0).getString("playurl");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return playurl;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.CcVideoLink;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelCcVideoLink.class;
		}
		
		// 缓存时间随机避免数据同时过期，时间太长可能会导致播放链接过期
		public int getExpireSecond() {
			return 1800 + RandomUtils.nextInt(1800);
		}

		// 数据量比较小不用存本地缓存
		@Override
		public boolean enabledLocalCache() {
			return false;
		}
		
	};


}
