package com.srnpr.xmassystem.face;

import com.srnpr.xmassystem.enumer.EKvSchema;

public interface IPlusConfig {

	/**
	 * 定义KV结构中的枚举类型
	 * 
	 * @return
	 */
	public EKvSchema getSchema();

	/**
	 * 返回实体对象的class
	 * 
	 * @return
	 */
	public Class<?> getPlusClass();
	
	
	/**
	 * 返回过期时间  如果大于0 则将过期时间设置为该时间
	 * @return
	 */
	public int getExpireSecond();
	
	/**
	 * 是否启用redis缓存之上的本地缓存<br>
	 * 总启用开关配置 xmassystem.enabledLocalCache<br>
	 * @return 
	 * 		true 启用本地缓存，需要同时设置 xmassystem.enabledLocalCache值为1才会生效<br>
	 * 		false 不启用本地缓存，任何情况下都不会使用本地缓存
	 */
	public boolean enabledLocalCache();
}
