package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 运费模板信息
 * @author jlin
 *
 */
public class PlusModelFreight implements IPlusModel {

	@ZapcomApi(value = "模板唯一标示")
	private String uid = "";
	
	@ZapcomApi(value = "店铺id")
	private String storeId = "";
	
	@ZapcomApi(value = "模板名称")
	private String tplName = "";
	
	@ZapcomApi(value = "省份")
	private String province = "";
	
	@ZapcomApi(value = "城市")
	private String city = "";
	
	@ZapcomApi(value = "区")
	private String area = "";
	
	@ZapcomApi(value = "发货时间，精确到分钟")
	private String consignmentTime = "";
	
	@ZapcomApi(value = "是否包邮")
	private String isFree = "";
	
	@ZapcomApi(value = "计价方式",remark="449746290001  按件数, 449746290003  按体积,449746290002  按重量")
	private String valuationType = "";

	@ZapcomApi(value = "运送方式",remark="1：快递  2：EMS  4：平邮  三种方式的和表示支持的方式")
	private String freightMode = "";
	
	@ZapcomApi(value = "是否禁用",remark="449746250001 禁用  449746250002 不禁用")
	private String isDisable = "";
	
	private List<PlusModelFreightDetail> freightDetails = new ArrayList<PlusModelFreightDetail>();

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getConsignmentTime() {
		return consignmentTime;
	}

	public void setConsignmentTime(String consignmentTime) {
		this.consignmentTime = consignmentTime;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public String getValuationType() {
		return valuationType;
	}

	public void setValuationType(String valuationType) {
		this.valuationType = valuationType;
	}

	public String getFreightMode() {
		return freightMode;
	}

	public void setFreightMode(String freightMode) {
		this.freightMode = freightMode;
	}

	public String getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(String isDisable) {
		this.isDisable = isDisable;
	}

	public List<PlusModelFreightDetail> getFreightDetails() {
		return freightDetails;
	}

	public void setFreightDetails(List<PlusModelFreightDetail> freightDetails) {
		this.freightDetails = freightDetails;
	}
}
