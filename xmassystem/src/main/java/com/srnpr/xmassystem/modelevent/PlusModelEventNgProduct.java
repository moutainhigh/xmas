package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;

/**
 * 内购活动调过滤参数实体类
 * @author zhouguohui
 *
 */
public class PlusModelEventNgProduct {
	/**用户编号**/
	private String memberCode="";
	/**product成本价**/
	private BigDecimal proCostPrice=BigDecimal.ZERO;
	/**sku集合**/
	private List<PlusModelProductSkuInfo> listSku = new ArrayList<PlusModelProductSkuInfo>();
	/**内购购物车对象**/
	List<PlusModelNgProduct>  ngPro = new ArrayList<PlusModelNgProduct>();
	/**以下字段为过滤活动后的价格**/
	Map<String,PlusModelNgProduct> map = new HashMap<String,PlusModelNgProduct>();
	
	
	
	/**
	 * @return the proCostPrice
	 */
	public BigDecimal getProCostPrice() {
		return proCostPrice;
	}
	/**
	 * @param proCostPrice the proCostPrice to set
	 */
	public void setProCostPrice(BigDecimal proCostPrice) {
		this.proCostPrice = proCostPrice;
	}
	/**
	 * @return the memberCode
	 */
	public String getMemberCode() {
		return memberCode;
	}
	/**
	 * @param memberCode the memberCode to set
	 */
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	/**
	 * @return the ngPro
	 */
	public List<PlusModelNgProduct> getNgPro() {
		return ngPro;
	}
	/**
	 * @param ngPro the ngPro to set
	 */
	public void setNgPro(List<PlusModelNgProduct> ngPro) {
		this.ngPro = ngPro;
	}
	/**
	 * @return the map
	 */
	public Map<String, PlusModelNgProduct> getMap() {
		return map;
	}
	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, PlusModelNgProduct> map) {
		this.map = map;
	}
	/**
	 * @return the listSku
	 */
	public List<PlusModelProductSkuInfo> getListSku() {
		return listSku;
	}
	/**
	 * @param listSku the listSku to set
	 */
	public void setListSku(List<PlusModelProductSkuInfo> listSku) {
		this.listSku = listSku;
	}
	
	
	
}
