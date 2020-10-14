package com.srnpr.xmassystem.top;

import com.srnpr.xmassystem.face.ILoad;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.face.IPlusExpireTime;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.topcache.SimpleCache;

/**
 * 加载内容的顶级类 该类用于封装部分通用方法
 * 
 * @author srnpr
 *
 * @param <TModel>
 * @param <TQuery>
 */
public abstract class LoadTop<TModel extends IPlusModel, TQuery extends IPlusQuery>
		extends BaseClass implements ILoad {

	// 本地缓存，只缓存5秒
	private static SimpleCache localCache = new SimpleCache(new SimpleCache.Config(5, 5, "LoadTop", false));
	
	public abstract TModel topInitInfo(TQuery tQuery);

	public abstract IPlusConfig topConfig();
	
	/**
	 * 根据编号获取信息 首先判断缓存系统中是否有 如果有则返回 否则执行初始化
	 * 
	 * @param sCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TModel upInfoByCode(TQuery tQuery) {

		TModel t = null;

		String sCode = tQuery.getCode();
		String v = null;
		String cacheKey = topConfig().getSchema().toString() + " - " +sCode;
		
		// 先取本地缓存
		if(topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
			v = localCache.get(cacheKey);
		}
		
		// 再取redis缓存
		if(v == null){
			v = XmasKv.upFactory(topConfig().getSchema()).get(sCode);
			
			// 此处检查一下缓存的版本
			if(v != null){
				try {
					t = new GsonHelper().fromJson(v, (TModel) topConfig().getPlusClass().newInstance());
					
					if(IPlusModelRefresh.class.isInstance(t)){
						// 如果实现了刷新的接口则检查是否需要刷新
						if(((IPlusModelRefresh)t).isRefresh()){
							v = null;
							t = null;
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			// 放入本地缓存
			if(v != null && topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
				localCache.put(cacheKey, v);
			}
		}
		
		// 最后查询数据库
		if(v == null){
			t = topInitInfo(tQuery);
			if(t != null) {
				v = GsonHelper.toJson(t);
				
				int expireSecond = topConfig().getExpireSecond();
				// 如果设置了自定义的过期时间则覆盖默认的过期时间
				if(IPlusExpireTime.class.isInstance(t)){
					if(((IPlusExpireTime)t).getExpireSecond() > 0){
						expireSecond = ((IPlusExpireTime)t).getExpireSecond();
					}
				}
				
				XmasKv.upFactory(topConfig().getSchema()).setex(sCode,expireSecond, v);
				
				// 放入本地缓存
				if(v != null && topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
					localCache.put(cacheKey, v);
				}
			}
		}
		
		if(t == null && v != null){
			try {
				t = new GsonHelper().fromJson(v, (TModel) topConfig().getPlusClass().newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return t;
	}

	/**
	 * 删除指定的Code
	 * 
	 * @param sCode
	 * @return
	 */
	public boolean deleteInfoByCode(String sCode) {
		XmasKv.upFactory(topConfig().getSchema()).del(sCode);
		
		if(topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
			localCache.remove(topConfig().getSchema().toString() + " - " +sCode);
		}
		
		return true;
	}
	
	/**
	 * 刷新缓存内容
	 * @param tQuery
	 */
	public void refresh(TQuery tQuery) {
		deleteInfoByCode(tQuery.getCode());
		upInfoByCode(tQuery);
	}

}