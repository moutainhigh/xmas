package com.srnpr.xmassystem.support;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 获取用户相关缓存
 * @author zhouguohui
 *
 */
public class PlusSupportUser {

	/****
	 * 获取用户是不是内购会员
	 * @param memberCode   用户编号
	 * @return
	 */
	public boolean upVipType(String memberCode) {
		
//		boolean isTrue=true;
		/**用户信息不存在  就创建**/
//		if(!XmasKv.upFactory(EKvSchema.UserMemberCode).exists(memberCode)) {
//			// MDataMap mData = DbUp.upTable("mc_extend_info_homehas").oneWhere("vip_type", null, null, "member_code", memberCode,"vip_type","4497469400050001");
//			MDataMap mData = new PlusServiceAccm().getCustInfo(memberCode);
//			
//			if(mData == null || !"4497469400050001".equals(mData.get("vip_type"))){
//				isTrue=false;
//				XmasKv.upFactory(EKvSchema.UserMemberCode).set(memberCode, "0");
//			}else{
//				isTrue=true;
//				XmasKv.upFactory(EKvSchema.UserMemberCode).set(memberCode, "1");
//			}
//			
//			XmasKv.upFactory(EKvSchema.UserMemberCode).expire(memberCode, 3600*24);
//			
//		}else{
//			
//			if(XmasKv.upFactory(EKvSchema.UserMemberCode).get(memberCode).equals("1")){
//				isTrue=true;
//			}else{
//				isTrue=false;
//			}
//			
//		}
		
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		
		return "70".equals(levelInfo.getLevel());
	}
}
