package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 运费模板特殊需求
 * @author jlin
 *
 */
public class PlusModelFreightDetail implements IPlusModel {

	@ZapcomApi(value = "运费模板id")
	private String tplUid = "";

	@ZapcomApi(value = "快递类型", remark = "1：快递  2：EMS  4：平邮")
	private String tplTypeId = "";

	@ZapcomApi(value = "是否可售", remark = "1：是 0：否")
	private String isEnable = "";

	@ZapcomApi(value = "区域名")
	private String area = "";

	@ZapcomApi(value = "区域码")
	private String areaCode = "";

	@ZapcomApi(value = "首（件数，重量，体积）")
	private BigDecimal expressStart = BigDecimal.ZERO;

	@ZapcomApi(value = "首费")
	private BigDecimal expressPostage = BigDecimal.ZERO;

	@ZapcomApi(value = "加重")
	private BigDecimal expressPlus = BigDecimal.ZERO;

	@ZapcomApi(value = "加费用")
	private BigDecimal expressPostageplus = BigDecimal.ZERO;

	@ZapcomApi(value = "排序")
	private int sequence = 0;

	public String getTplUid() {
		return tplUid;
	}

	public void setTplUid(String tplUid) {
		this.tplUid = tplUid;
	}

	public String getTplTypeId() {
		return tplTypeId;
	}

	public void setTplTypeId(String tplTypeId) {
		this.tplTypeId = tplTypeId;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public BigDecimal getExpressPostage() {
		return expressPostage;
	}

	public void setExpressPostage(BigDecimal expressPostage) {
		this.expressPostage = expressPostage;
	}

	public BigDecimal getExpressPlus() {
		return expressPlus;
	}

	public void setExpressPlus(BigDecimal expressPlus) {
		this.expressPlus = expressPlus;
	}

	public BigDecimal getExpressPostageplus() {
		return expressPostageplus;
	}

	public void setExpressPostageplus(BigDecimal expressPostageplus) {
		this.expressPostageplus = expressPostageplus;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public BigDecimal getExpressStart() {
		return expressStart;
	}

	public void setExpressStart(BigDecimal expressStart) {
		this.expressStart = expressStart;
	}
	
}
