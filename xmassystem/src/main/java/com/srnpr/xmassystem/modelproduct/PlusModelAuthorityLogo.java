package com.srnpr.xmassystem.modelproduct;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 权威标识
 * @author ligj
 * @version
 * 2015-11-27 16:43
 *
 */
public class PlusModelAuthorityLogo{
	
	@ZapcomApi(value = "uuid")
    private String uid;

	@ZapcomApi(value = "权威标志说明")
    private String logoContent;
	
	@ZapcomApi(value = "权威标志图标")
    private String logoPic;

	@ZapcomApi(value = "位置")
    private Integer logoLocation;

	@ZapcomApi(value = "是否全场",remark="44974 7110002是449747110001否")
    private String allFlag;
	
	@ZapcomApi(value = "应用编号",remark="SI2003")
    private String manageCode;
	
	@ZapcomApi(value = "展示商品",remark="是否全场为否时此字段有效，4497471600150001：LD商品，4497471600150003：跨境商品，4497471600150002：商户商品，4497471600150004：沙皮狗商品")
    private String showProductSource;
	
	public String getLogoContent() {
		return logoContent;
	}
	public void setLogoContent(String logoContent) {
		this.logoContent = logoContent;
	}
	public String getLogoPic() {
		return logoPic;
	}
	public void setLogoPic(String logoPic) {
		this.logoPic = logoPic;
	}
	public Integer getLogoLocation() {
		return logoLocation;
	}
	public void setLogoLocation(Integer logoLocation) {
		this.logoLocation = logoLocation;
	}
	public String getAllFlag() {
		return allFlag;
	}
	public void setAllFlag(String allFlag) {
		this.allFlag = allFlag;
	}
	public String getManageCode() {
		return manageCode;
	}
	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	public String getShowProductSource() {
		return showProductSource;
	}
	public void setShowProductSource(String showProductSource) {
		this.showProductSource = showProductSource;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
}