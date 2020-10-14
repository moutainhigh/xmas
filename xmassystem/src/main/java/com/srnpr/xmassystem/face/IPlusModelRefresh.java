package com.srnpr.xmassystem.face;

/**
 * 对比缓存中和本地的数据结构版本是否兼容<br>
 * 如果不兼容则刷新缓存中数据
 */
public interface IPlusModelRefresh {

	/**
	 * 子类实现此方法对缓存的数据进行对比
	 * @return true 刷新缓存中数据，false 不刷新
	 */
	public boolean isRefresh();
}
