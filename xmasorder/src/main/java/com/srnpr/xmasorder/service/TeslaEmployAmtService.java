package com.srnpr.xmasorder.service;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 暂存款、储息金使用
 * @author pang_jhui
 *
 */
public class TeslaEmployAmtService extends BaseClass {
	
	/**
	 * 接口解析
	 * @param flag
	 * 		操作标识（占用、取消、使用）
	 * @param mDataMap
	 * 		参数
	 * @return 是否操作成功
	 */
	public boolean doProcess(MDataMap mDataMap,TeslaXOrder teslaXOrder){
		
		boolean returnFlag = false;
		
		try {
			
			Class<?> employClass = Class.forName("com.cmall.groupcenter.homehas.RsyncEmployAccountInfo");
			
			Object object = employClass.newInstance();
			
			Method initMethod = employClass.getMethod("initRequest", MDataMap.class);
			
			Method initAmtMethod = employClass.getMethod("initAmtRequest", TeslaXOrder.class);
			
			Method employMethod = employClass.getMethod("doRsync");
			
			initMethod.invoke(object, mDataMap);
			
			initAmtMethod.invoke(object, teslaXOrder);
			
			Object returnArg = employMethod.invoke(object);
			
			if(returnArg instanceof Boolean){
				
				returnFlag = (Boolean) returnArg;
				
			}
			
		} catch (Exception e) {
			
			bLogError(0, e.getMessage());
			
		}		
		
		return returnFlag;
		
		
	}
	
	/**
	 * 根据订单编号查询家有会员信息
	 * @param order_code
	 * 		订单编号
	 * @return MDataMap
	 */
	public MDataMap getMemberInfoHas(String buyer_code){
		
		MDataMap extendMap = new MDataMap();
		
		if(StringUtils.isNotBlank(buyer_code)){
			
			extendMap = DbUp.upTable("mc_extend_info_homehas").one("member_code",buyer_code);
			
		}
		
		return extendMap;
		
	}

	
	//存在一定的风险，呵呵
	public MDataMap getMemberInfoHas1(String buyer_mobile){
		
		MDataMap extendMap = new MDataMap();
		String homehas_code="";
		String member_name="";
		//查询家有惠中用户
		Map<String, Object> map1 = DbUp.upTable("mc_login_info").dataSqlOne("SELECT e.old_code,e.member_name from mc_extend_info_homepool e LEFT JOIN  mc_login_info l on e.member_code=l.member_code WHERE l.login_name=:login_name and l.manage_code=:manage_code ", new MDataMap("login_name", buyer_mobile,"manage_code","SI2009"));
		if(map1!=null&&map1.size()>0){
			homehas_code=(String)map1.get("old_code");
			member_name=(String)map1.get("member_name");
		}
		
		
		if(StringUtils.isBlank(homehas_code)){
			//首先查询惠家有的用户
			Map<String, Object> map = DbUp.upTable("mc_login_info").dataSqlOne("SELECT e.homehas_code,e.member_name  from mc_extend_info_homehas e LEFT JOIN  mc_login_info l on e.member_code=l.member_code WHERE l.login_name=:login_name and l.manage_code=:manage_code ", new MDataMap("login_name", buyer_mobile,"manage_code","SI2003"));
			if(map!=null&&map.size()>0){
				homehas_code=(String)map.get("homehas_code");
				member_name=(String)map.get("member_name");
			}
		}
		
		extendMap.put("homehas_code", homehas_code);
		extendMap.put("member_name", member_name);
		
		return extendMap;
		
	}
}
