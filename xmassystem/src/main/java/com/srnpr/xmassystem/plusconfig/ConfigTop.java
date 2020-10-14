package com.srnpr.xmassystem.plusconfig;

import com.srnpr.xmassystem.face.IPlusConfig;

public abstract class ConfigTop implements IPlusConfig {

	
	/* (non-Javadoc)
	 * @see com.srnpr.xmassystem.face.IPlusConfig#getExpireSecond()
	 */
	public int getExpireSecond()
	{
		//默认设置30天过期时间
		return 2592000;
	}
	
	/**
	 * 
	 * 默认值：  true 启用
	 * @return 
	 * @see com.srnpr.xmassystem.face.IPlusConfig#enabledLocalCache()
	 */
	public boolean enabledLocalCache(){
		return true;
	}
}
