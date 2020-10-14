package com.srnpr.xmassystem.face;

/**
 * 自定义过期时间
 */
public interface IPlusExpireTime {

	/**
	 * 为了解决同一类KEY无法设置不同的过期时间的问题
	 * @return 0 以默认设置为准， 大于 0 以此方法返回的过期时间为准
	 */
	public int getExpireSecond();
	
}
