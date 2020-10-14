package com.srnpr.xmassystem.load;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.invoke.ref.GetCustLevelRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustLevelResult;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载用户在家有的客户等级
 */
public class LoadMemberLevel extends LoadTop<PlusModelMemberLevel,PlusModelMemberLevelQuery>{

	public PlusModelMemberLevel topInitInfo(PlusModelMemberLevelQuery tQuery) {
		String phone = "";
		
		if(StringUtils.isNotBlank(tQuery.getCode())){
			MDataMap mData = DbUp.upTable("mc_login_info").oneWhere("login_name", "", "", "member_code", tQuery.getCode());
			if(mData != null){
				phone = mData.get("login_name");
			}
		}
		
		PlusModelMemberLevel levelInfo = null;
		if(StringUtils.isNotBlank(phone)){
			levelInfo = getCustLevelInfo(phone);
		}
		
		if(levelInfo == null){
			// 查询不到客户信息的时候设置一个默认等级
			levelInfo = new PlusModelMemberLevel();
			levelInfo.setCustId("");
			levelInfo.setLevel(XmasSystemConst.CUST_LVL_CD);
		}
		
		levelInfo.setMemberCode(tQuery.getCode());
		levelInfo.setPhone(phone);
		
		// 如果获取到了客代号则缓存过期时间设置的长一些
		if(StringUtils.isNotBlank(levelInfo.getCustId())) {
			levelInfo.setExpireSecond(3600);
		}
		
		Date now = new Date();
		Date plusEndDate = null;
		
		// 如果已经过期则置空结束时间
		if(StringUtils.isNotBlank(levelInfo.getPlusEndDate())) {
			try {
				plusEndDate = DateUtils.parseDate(levelInfo.getPlusEndDate(), new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
			} catch (Exception e) {
				//e.printStackTrace();
				levelInfo.setPlusEndDate("");
			}
			
			if(plusEndDate == null || (!DateUtils.isSameDay(plusEndDate, now) && plusEndDate.compareTo(now) <= 0)) {
				plusEndDate = null;
				levelInfo.setPlusEndDate("");
			}
			
			if(plusEndDate != null) {
				levelInfo.setPlusEndDate(DateFormatUtils.format(plusEndDate, "yyyy-MM-dd"));
			}
		}
		
		
		return levelInfo;
	}
	
	public PlusModelMemberLevel getCustLevelInfo(String phone){
		GetCustLevelRef ref = (GetCustLevelRef)BeansHelper.upBean(GetCustLevelRef.NAME);
		
		PlusModelMemberLevel levelInfo = new PlusModelMemberLevel();
		
		GetCustLevelResult getCustLevelRes = ref.getCustLevel(phone);
		if(getCustLevelRes == null || getCustLevelRes.getResultCode() != 1 || StringUtils.isBlank(getCustLevelRes.getCust_id())){
			return null;
		}
		
		levelInfo.setCustId(getCustLevelRes.getCust_id());
		levelInfo.setLevel(getCustLevelRes.getCustlvl());
		levelInfo.setPlusEndDate(getCustLevelRes.getPlus_end_date());
		
		return levelInfo;
	}
	
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
    private final static ConfigTop PLUS_CONFIG = new ConfigTop(){

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.MemberLevel;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelMemberLevel.class;
		}
    	
		// 只缓存5分钟
		public int getExpireSecond() {
			return 300;
		};
    };

}
