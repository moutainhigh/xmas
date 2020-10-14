package com.srnpr.xmassystem.modelproduct;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**   
*    
* 项目名称：   
* 类名称：PropertyinfoForSku   
* 类描述：   
* 创建人：李国杰
*
* @version    
*    
*/
public class PlusModelPropertyInfo  {
    
	@ZapcomApi(value = "属性名称")
    private String propertykey  = ""  ;

	@ZapcomApi(value = "属性值")
    private String propertyValue  = ""  ;
	
	@ZapcomApi(value = "内联赠品展示的开始时间")
	private String startDate = "";
	
	@ZapcomApi(value = "内联赠品展示的结束时间")
	private String endDate = "";



	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPropertykey() {
		return propertykey;
	}

	public void setPropertykey(String propertykey) {
		this.propertykey = propertykey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}

