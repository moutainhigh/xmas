package com.srnpr.xmassystem.util;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缓存省市区信息 类
 */
public class ProCityLoader {

	private static Cache cache;

	private static ProCityLoader instance = new ProCityLoader();

	private ProCityLoader() {
		CacheDefine cDefine = new CacheDefine();
		String sCacheName = this.getClass().getName();
		CacheConfiguration cacheConfiguration = new CacheConfiguration();

		cacheConfiguration.setName(sCacheName);

		// 设置最大数量
		cacheConfiguration.setMaxEntriesLocalHeap(9999999);
		// 设置最长存活时间
		cacheConfiguration.setTimeToIdleSeconds(900);
		cacheConfiguration.setTimeToLiveSeconds(3600);

		cacheConfiguration.setMemoryStoreEvictionPolicy("FIFO");

		cache = cDefine.inCustomCache(sCacheName, cacheConfiguration);
	}

	@SuppressWarnings("unchecked")
	private List<MDataMap> upValue(String k) {
		Object oReturnObject = null;
		Element eCachElement = cache.get(k);
		if (eCachElement != null) {
			oReturnObject = eCachElement.getObjectValue();
		}

		if (oReturnObject == null) {
			List<MDataMap> pcgovList = DbUp.upTable("sc_tmp").queryAll("code,name", "", "", new MDataMap());// 查出所有的省市区信息
			oReturnObject = pcgovList;

			// 把列表放入缓存
			if (!pcgovList.isEmpty()) {
				cache.put(new Element(k, oReturnObject));
			}

			// 把明细放入缓存
			for (MDataMap mp : pcgovList) {
				cache.put(new Element(mp.get("code"), mp.get("name")));
			}
		}

		return (List<MDataMap>) oReturnObject;
	}

	/**
	 * 查询区域编码的中文名称
	 * @param code
	 * @return
	 */
	public static String getName(String code) {
		String name = null;
		// 先查明细数据
		Element et = cache.get(code);
		if (et != null) {
			name = (String) et.getObjectValue();
		}

		// 如果明细数据不存在再循环列表数据
		if(name == null) {
			List<MDataMap> mapList = queryAll();
			for (MDataMap map : mapList) {
				if (map.get("code").equals(code)) {
					name = map.get("name");
					break;
				}
			}
		}

		return name;
	}

	public static List<MDataMap> queryAll() {
		return instance.upValue("all");
	}
}
