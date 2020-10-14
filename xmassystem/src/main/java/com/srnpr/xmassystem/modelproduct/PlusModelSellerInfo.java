package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商户信息
 * @author jlin
 *
 */
public class PlusModelSellerInfo implements IPlusModel {

	@ZapcomApi(value = "商户类型",remark="普通4497478100050001,跨境4497478100050002")
	private String uc_seller_type = "4497478100050001";
	
	@ZapcomApi(value = "商户公司名称")
	private String sellerCompanyName = "";
	
	@ZapcomApi(value = "法人身份证正面照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/274a0/xxx.jpg")
	private String legalPersonIDCardPic = "";
	
	@ZapcomApi(value = "法人身份证反面照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/yyy.jpg")
	private String legalPersonIDCardOppPic = "";
	
	@ZapcomApi(value = "营业执照照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/27507/bbb.jpg")
	private String bizLicensePic = "";
	
	@ZapcomApi(value = "组织机构代码照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/27507/ccc.jpg")
	private String orgCodePic = "";
	
	@ZapcomApi(value = "税务登记证副本照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/rrr.jpg")
	private String taxRegCertCopy = "";
	
	@ZapcomApi(value = "开户行影印证件",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/ttt.jpg")
	private String bankAccountCertPhotoCopy = "";

	public String getUc_seller_type() {
		return uc_seller_type;
	}

	public void setUc_seller_type(String uc_seller_type) {
		this.uc_seller_type = uc_seller_type;
	}

	public String getLegalPersonIDCardPic() {
		return legalPersonIDCardPic;
	}

	public void setLegalPersonIDCardPic(String legalPersonIDCardPic) {
		this.legalPersonIDCardPic = legalPersonIDCardPic;
	}

	public String getLegalPersonIDCardOppPic() {
		return legalPersonIDCardOppPic;
	}

	public void setLegalPersonIDCardOppPic(String legalPersonIDCardOppPic) {
		this.legalPersonIDCardOppPic = legalPersonIDCardOppPic;
	}

	public String getBizLicensePic() {
		return bizLicensePic;
	}

	public void setBizLicensePic(String bizLicensePic) {
		this.bizLicensePic = bizLicensePic;
	}

	public String getOrgCodePic() {
		return orgCodePic;
	}

	public void setOrgCodePic(String orgCodePic) {
		this.orgCodePic = orgCodePic;
	}

	public String getTaxRegCertCopy() {
		return taxRegCertCopy;
	}

	public void setTaxRegCertCopy(String taxRegCertCopy) {
		this.taxRegCertCopy = taxRegCertCopy;
	}

	public String getBankAccountCertPhotoCopy() {
		return bankAccountCertPhotoCopy;
	}

	public void setBankAccountCertPhotoCopy(String bankAccountCertPhotoCopy) {
		this.bankAccountCertPhotoCopy = bankAccountCertPhotoCopy;
	}

	public String getSellerCompanyName() {
		return sellerCompanyName;
	}

	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}
}
