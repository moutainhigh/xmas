package com.srnpr.xmassystem.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 京东地址相关的封装方法
 */
public class PlusSupportJdAddress extends BaseClass {

	/**
	 * 根据京东的地址ID查询相关的完整的地址信息<br>
	 * 传入一个4级地址编号，查询对应前三级的编号
	 * @param jdAreaId
	 * @return
	 */
	public JdAddress getJdAddress(String jdAddressId) {
		JdAddress address = new JdAddress();
		List<MDataMap> list = new ArrayList<MDataMap>();
		
		// 递归获取本级以及父级地址
		getJdAddressAndParent(list, jdAddressId);
		
		int lvl;
		String code;
		String name;
		for(MDataMap map : list) {
			lvl = NumberUtils.toInt(map.get("code_lvl"));
			code = map.get("code");
			name = map.get("name");
			
			// 根据地址ID判断提供的地址是哪一级
			if(map.get("code").equals(jdAddressId)) {
				address.setLevel(lvl);
			}
			
			if(lvl == 1) {
				address.setProvinceId(code);
				address.setProvinceName(name);
			} else if(lvl == 2) {
				address.setCityId(code);
				address.setCityName(name);
			} else if(lvl == 3) {
				address.setCountyId(code);
				address.setCountyName(name);
			} else if(lvl == 4) {
				address.setVillageId(code);
				address.setVillageName(name);
			}
		}
		
		return address;
	}
	
	/**
	 * 根据惠家有区域编号查询对应的京东地址ID
	 * @param jdAreaId
	 * @return
	 */
	public String getJdAddressId(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		MDataMap relMap = DbUp.upTable("sc_jingdong_address_rel").one("hjy_code", areaCode);
		return relMap == null ? null : relMap.get("jd_code");
	}
	
	/**
	 * 根据提供的地址ID递归获取本级以及父级地址
	 * @param list
	 * @param jdAddressId
	 */
	public void getJdAddressAndParent(List<MDataMap> list, String jdAddressId) {
		// 如果地址编码已经获取过了则直接返回
		for(MDataMap map : list) {
			if(map.get("code").equals(jdAddressId)) {
				return;
			}
		}
		
		List<MDataMap> res = DbUp.upTable("sc_jingdong_address").queryAll("code,name,p_code,code_lvl", "", "", new MDataMap("code", jdAddressId));
		if(!res.isEmpty()) {
			list.add(res.get(0));
			
			// 如果有父级则递归获取
			if(StringUtils.isNotBlank(res.get(0).get("p_code"))) {
				getJdAddressAndParent(list,res.get(0).get("p_code"));
			}
		}
	}
	
	public static class JdAddress {
		private String provinceId = "";// 省
		private String provinceName = "";// 省
		private String cityId = "";    // 市
		private String cityName = "";    // 市
		private String countyId = "";  // 县
		private String countyName = "";  // 县
		private String villageId = ""; // 乡镇
		private String villageName = ""; // 乡镇
		
		// 提供的地址ID属于哪一级
		private int level; // 层级

		public String getProvinceId() {
			return provinceId;
		}

		public void setProvinceId(String provinceId) {
			this.provinceId = provinceId;
		}

		public String getProvinceName() {
			return provinceName;
		}

		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getCountyId() {
			return countyId;
		}

		public void setCountyId(String countyId) {
			this.countyId = countyId;
		}

		public String getCountyName() {
			return countyName;
		}

		public void setCountyName(String countyName) {
			this.countyName = countyName;
		}

		public String getVillageId() {
			return villageId;
		}

		public void setVillageId(String villageId) {
			this.villageId = villageId;
		}

		public String getVillageName() {
			return villageName;
		}

		public void setVillageName(String villageName) {
			this.villageName = villageName;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}
		
	}
	
	/**
	 * 根据惠家有areaCode查客服维护的地址对应表
	 * @param areaCode
	 * @return
	 */
	public JdAddress getJdAddressName(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		
		List<MDataMap> list = new ArrayList<MDataMap>();
		// 递归获取本级以及父级地址
		getHjyAddressAndParent(list, areaCode);
		
		//按地址级别排序
		Collections.sort(list, new Comparator<MDataMap>() {
			@Override
			public int compare(MDataMap o1, MDataMap o2) {
				if(NumberUtils.toInt(o1.get("code_lvl")) - NumberUtils.toInt(o2.get("code_lvl"))>0) return 1;
				else return -1;
			}
		});
		
		String hjyAddressName = "";
		for (int i = 0; i < list.size(); i ++) {
			hjyAddressName +=list.get(i).get("name");
		}
		MDataMap jdAddr = DbUp.upTable("sc_jingdong_address_rel_sp").one("hjy_code_name",hjyAddressName);
		if(null != jdAddr) {
			JdAddress jdAddress = new JdAddress();
			jdAddress.setProvinceId(jdAddr.get("jd_code1"));
			jdAddress.setCityId(jdAddr.get("jd_code2"));
			jdAddress.setCountyId(jdAddr.get("jd_code3"));
			jdAddress.setVillageId(jdAddr.get("jd_code4"));
			return jdAddress;
		}
		
		return null;
	}
	
	public void getHjyAddressAndParent(List<MDataMap> list, String areaCode) {
		// 如果地址编码已经获取过了则直接返回
		for(MDataMap map : list) {
			if(map.get("code").equals(areaCode)) {
				return;
			}
		}
		
		List<MDataMap> res = DbUp.upTable("sc_tmp").queryAll("code,name,p_code,code_lvl", "", "", new MDataMap("code", areaCode));
		if(!res.isEmpty()) {
			list.add(res.get(0));
			
			// 如果有父级则递归获取
			if(StringUtils.isNotBlank(res.get(0).get("p_code"))) {
				getHjyAddressAndParent(list,res.get(0).get("p_code"));
			}
		}
	}

	/**
	 * 根据惠家有地址编号分级匹配京东地址编号
	 * @param areaCode
	 * @return
	 */
	public JdAddress getJdAddressAntitone(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		
		List<MDataMap> list = new ArrayList<MDataMap>();
		// 递归获取本级以及父级地址
		getHjyAddressAndParent(list, areaCode);
		
		String hjyCodes = "";
		for (MDataMap mDataMap : list) {
			hjyCodes += "'"+mDataMap.get("code")+"',";
		}
		
		if(StringUtils.isNotEmpty(hjyCodes)) {
			hjyCodes = hjyCodes.substring(0,hjyCodes.length()-1);
			String sSql = "SELECT jd_code,jd_code_lvl FROM systemcenter.sc_jingdong_address_rel WHERE hjy_code in ("+hjyCodes+")";
			List<Map<String, Object>> sqlList = DbUp.upTable("sc_jingdong_address_rel").dataSqlList(sSql, new MDataMap());
			if(null != sqlList && !sqlList.isEmpty()) {
				JdAddress address = new JdAddress();
				for (Map<String, Object> map : sqlList) {
					String jd_code = map.get("jd_code")+"";
					String jd_code_lvl = map.get("jd_code_lvl")+"";
					if("1".equals(jd_code_lvl)) {
						address.setProvinceId(jd_code);
					} else if("2".equals(jd_code_lvl)) {
						address.setCityId(jd_code);
					} else if("3".equals(jd_code_lvl)) {
						address.setCountyId(jd_code);
					} else if("4".equals(jd_code_lvl)) {
						address.setVillageId(jd_code);
					}
				}
				return address;
			}
		}
		
		return null;
	}
}
