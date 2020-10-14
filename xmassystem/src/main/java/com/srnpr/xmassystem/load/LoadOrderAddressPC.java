package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelOrderAddress;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigOrderAddress;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * @descriptions
 * 
 * @date 2016年6月7日下午4:37:59
 * @author Yangcl
 * @version 1.0.1
 */
public class LoadOrderAddressPC extends LoadOrderAddress {
	private final static PlusConfigOrderAddress PLUS_CONFIG = new PlusConfigOrderAddress();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	
	public PlusModelOrderAddress topInitInfo(PlusModelSkuQuery query) {
		String addressCode = query.getCode();
		PlusModelOrderAddress plusAddress = new PlusSupportProduct().initAdderssFrom(addressCode);
		return plusAddress;
	}
	
	/**
	 * @descriptions 对内购与非内购进行筛选
	 * 
	 * @param addressCode 如：NGDZ150402100001
	 * @param isPurchase
	 * @param query
	 * @refactor no
	 * @date 2016年6月7日下午4:44:46
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	public PlusModelOrderAddress checkForInner(Integer isPurchase, PlusModelSkuQuery query){
		PlusModelOrderAddress pmoa = new PlusModelOrderAddress();
		if(isPurchase == 1) {
			// 内购逻辑
			String sCode = query.getCode();
			if (!XmasKv.upFactory(topConfig().getSchema()).exists(sCode)) {
				String addressCode = query.getCode();
				pmoa = queryStaffAddress(addressCode);
				XmasKv.upFactory(topConfig().getSchema()).setex(sCode,topConfig().getExpireSecond(), GsonHelper.toJson(pmoa));
			} else {
				try {
					pmoa = new GsonHelper().fromJson(XmasKv.upFactory(topConfig().getSchema()).get(sCode),	(PlusModelOrderAddress) topConfig().getPlusClass().newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} else { 
			// 正常逻辑
			pmoa = upInfoByCode( query);
		}
		
		return pmoa;
	}
	
	/**
	 * @descriptions 筛选内购地址
	 * 
	 * @param addressCode
	 * @return
	 * @refactor no
	 * @date 2016年6月7日下午4:48:22
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	public PlusModelOrderAddress queryStaffAddress(String addressCode) {
		//db query
		PlusModelOrderAddress plusaAddress = new PlusModelOrderAddress();
		plusaAddress.setAddressCode(addressCode);
		MDataMap mAddressMap = DbUp.upTable("nc_staff_address").one("address_id", addressCode);
		//缓存地址信息
		if (mAddressMap!=null && !mAddressMap.isEmpty()) {
			plusaAddress.setAddress(mAddressMap.get("address_street"));
			plusaAddress.setAreaCode(mAddressMap.get("area_code"));
//			plusaAddress.setEmail(mAddressMap.get("email"));
//			plusaAddress.setMobilephone(mAddressMap.get("address_mobile"));TODO 手机
			plusaAddress.setPostCode(mAddressMap.get("address_postalcode"));
//			plusaAddress.setReceivePerson(mAddressMap.get("address_name")); TODO 收件人
		}
		return plusaAddress;
	}
}
















