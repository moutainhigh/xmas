package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;


/** 
* @ClassName: PlusModelTemplateAeraCode 
* @Description: 城市编号
* @author 张海生
* @date 2015-12-22 下午3:09:43 
*  
*/
public class PlusModelTemplateAeraCode implements IPlusModel {

	List<String> areaCodes = new ArrayList<String>();

	public List<String> getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(List<String> areaCodes) {
		this.areaCodes = areaCodes;
	}
	
}
