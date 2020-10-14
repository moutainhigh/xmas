package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelCouponTypeInfo implements IPlusModel, IPlusModelRefresh {
	
	/** 增加这个属性的值可在加载缓存时自动刷新旧版缓存数据 */
	public static final int _VERSION = 4;
	
	public PlusModelCouponTypeInfo() {
		super();
		this.v = _VERSION;
	}

	@Override
	public boolean isRefresh() {
		return v < _VERSION;
	}
	
	/** 版本号 */
	@ZapcomApi(value = "数据版本号")
	private int v = 0;

	@ZapcomApi(value = "是否有效", remark = "0无效  1有效")
	private int flag = -1;

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public boolean invalid() {
		return flag==0;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
