package com.srnpr.xmassystem.support;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class PlusSupportSeller extends BaseClass {

	private static LoadSellerInfo sellerLoader = new LoadSellerInfo();
	
	public PlusModelSellerInfo initSellerInfoFromDB(String small_seller_code) {
		PlusModelSellerInfo info=new PlusModelSellerInfo();
		MDataMap dataMap = DbUp.upTable("uc_seller_info_extend").one("small_seller_code",small_seller_code);
		if(dataMap != null) {
			info.setSellerCompanyName(StringUtils.isBlank(dataMap.get("seller_company_name")) ? "" : dataMap.get("seller_company_name"));
			info.setUc_seller_type(StringUtils.isBlank(dataMap.get("uc_seller_type")) ? "" : dataMap.get("uc_seller_type"));
			info.setLegalPersonIDCardPic(StringUtils.isBlank(dataMap.get("legal_person_ID")) ? "" : dataMap.get("legal_person_ID"));
			info.setLegalPersonIDCardOppPic(StringUtils.isBlank(dataMap.get("legal_person_ID_opposite")) ? "" : dataMap.get("legal_person_ID_opposite"));
			info.setBizLicensePic(StringUtils.isBlank(dataMap.get("upload_business_license")) ? "" : dataMap.get("upload_business_license"));
			info.setOrgCodePic(StringUtils.isBlank(dataMap.get("organization_code")) ? "" : dataMap.get("organization_code"));
			info.setTaxRegCertCopy(StringUtils.isBlank(dataMap.get("tax_registration_certificate_copy")) ? "" : dataMap.get("tax_registration_certificate_copy"));
			info.setBankAccountCertPhotoCopy(StringUtils.isBlank(dataMap.get("open_bank_photocopy_certificate")) ? "" : dataMap.get("open_bank_photocopy_certificate"));
		}
		return info;
	}
	
	/**
	 * 根据商户的smallSellerCode返回该商户的类型.
	 * @param smallSellerCode
	 * @author zht
	 * @return
	 */
	public String getSellerType(String smallSellerCode) {
		String sellerType = "";
		PlusModelSellerQuery query = new PlusModelSellerQuery(smallSellerCode);
		PlusModelSellerInfo info = sellerLoader.upInfoByCode(query);
		if(null != info) {
			sellerType = info.getUc_seller_type();
		}
		return sellerType;
	}

}
