package com.srnpr.xmassystem.support;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.modelproduct.PlusModelBlackList;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class PlusSupportBlacklist extends BaseClass {

	/**
	 * 根据manageCode查询各系统黑名单信息
	 * 当前版本先不做系统平台区分 默认all
	 * @param manageCode
	 * @return
	 */
	public PlusModelBlackList initBlackListFromDb(String manageCode) {
		PlusModelBlackList blackList = new PlusModelBlackList();
		List<MDataMap> li = DbUp.upTable("mc_blacklist").queryAll("mobile", "", "status=:status", new MDataMap("status","1"));
		if (li!=null&&!li.isEmpty()) {
			for (int i = 0; i < li.size(); i++) {
				MDataMap map = li.get(i);
				if(map!=null&&!map.isEmpty()&&map.containsKey("mobile")&&StringUtils.isNotBlank(map.get("mobile"))){
					blackList.getMobiles().add(map.get("mobile"));
				}
			}
		}
		
		return blackList;
	}
}
