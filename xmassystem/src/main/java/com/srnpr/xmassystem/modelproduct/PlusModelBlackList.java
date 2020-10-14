package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 黑名单手机号
 * @author xiegj
 *
 */
public class PlusModelBlackList implements IPlusModel {

	@ZapcomApi(value = "黑名单手机号")
	private List<String> mobiles = new ArrayList<String>();

	public List<String> getMobiles() {
		return mobiles;
	}

	public void setMobiles(List<String> mobiles) {
		this.mobiles = mobiles;
	}
	
}
