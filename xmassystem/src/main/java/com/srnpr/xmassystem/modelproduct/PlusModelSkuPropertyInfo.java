package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**   
*    
* 项目名称   
* 类名称：PropertyInfo   
* 类描述：   
* 创建人：李国杰
* 
*/
public class PlusModelSkuPropertyInfo  {
    
	@ZapcomApi(value = "属性Key编号")
    private String propertyKeyCode  = ""  ;

	@ZapcomApi(value = "属性Key名称")
    private String propertyKeyName  = ""  ;
	
	@ZapcomApi(value = "属性Value信息列表")
	private List<PlusModelSkuPropertyValueInfo> propertyValueList = new ArrayList<PlusModelSkuPropertyValueInfo>();
	
	public String getPropertyKeyCode() {
		return propertyKeyCode;
	}
	public void setPropertyKeyCode(String propertyKeyCode) {
		this.propertyKeyCode = propertyKeyCode;
	}
	public String getPropertyKeyName() {
		return propertyKeyName;
	}
	public void setPropertyKeyName(String propertyKeyName) {
		this.propertyKeyName = propertyKeyName;
	}
	public List<PlusModelSkuPropertyValueInfo> getPropertyValueList() {
		return propertyValueList;
	}
	public void setPropertyValueList(List<PlusModelSkuPropertyValueInfo> propertyValueList) {
		this.propertyValueList = propertyValueList;
	}

}

