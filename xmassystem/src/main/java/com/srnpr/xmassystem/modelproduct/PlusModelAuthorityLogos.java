package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 权威标识列表
 * @author ligj
 * @version
 *
 */
public class PlusModelAuthorityLogos implements IPlusModel {

	@ZapcomApi(value = "权威标志")
	private List<PlusModelAuthorityLogo> authorityLogos = new ArrayList<PlusModelAuthorityLogo>();

	public List<PlusModelAuthorityLogo> getAuthorityLogos() {
		return authorityLogos;
	}

	public void setAuthorityLogos(List<PlusModelAuthorityLogo> authorityLogos) {
		this.authorityLogos = authorityLogos;
	}
	
}