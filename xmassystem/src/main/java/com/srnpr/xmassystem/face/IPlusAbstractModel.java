package com.srnpr.xmassystem.face;

import com.alibaba.fastjson.annotation.JSONField;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 抽象类，提取公共版本号参数: v <br>
 * 子类需实现getCurrentVersion方法，提供一个最新版本号。
 */
public abstract class IPlusAbstractModel implements IPlusModelRefresh{
	
	/** 缓存版本号 */
	@ZapcomApi(value = "缓存数据版本号")
	private int v = getCurrentVersion();
	
	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	/**
	 * 返回当前数据结构的版本号，用于跟缓存数据的版本号做对比判断。
	 * 缓存中默认版本号是0
	 * @return
	 */
	protected abstract int getCurrentVersion();

	@JSONField(serialize = false)
	@Override
	public boolean isRefresh() {
		return v < getCurrentVersion();
	}
	
}
