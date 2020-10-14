package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PlusModelGitfSkuInfoList implements IPlusModel {
	
	@ZapcomApi(value="赠品List")
	List<PlusModelGiftSkuinfo> giftSkuinfos = new ArrayList<PlusModelGiftSkuinfo>();

	public List<PlusModelGiftSkuinfo> getGiftSkuinfos() {
		return giftSkuinfos;
	}

	public void setGiftSkuinfos(List<PlusModelGiftSkuinfo> giftSkuinfos) {
		this.giftSkuinfos = giftSkuinfos;
	}
	
}
