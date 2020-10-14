package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;

/***
 * 一栏大图模版的商品销量
 */
public class PlusModelProductOrderNum extends IPlusAbstractModel implements IPlusModel {

	@Override
	protected int getCurrentVersion() {
		return 0;
	}
	
	private int num = 0;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
}
