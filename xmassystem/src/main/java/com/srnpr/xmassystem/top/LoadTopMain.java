package com.srnpr.xmassystem.top;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusQuery;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.topcache.SimpleCache;

/**
 * 加载内容的顶级类 该类用于封装部分通用方法
 * 
 *
 * @param <TModel>
 * @param <TQuery>
 */
public abstract class LoadTopMain<TModel extends IPlusModel, TQuery extends IPlusQuery> extends LoadTop<TModel, TQuery> {
	private static SimpleCache localCache = new SimpleCache(new SimpleCache.Config(5, 5, "LoadTopMain",false));
	
	public abstract TModel topInitInfoMain(TQuery tQuery);

	/**
	 * 根据编号获取信息 首先判断缓存系统中是否有 如果有则返回 否则执行初始化
	 * 
	 * @param sCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TModel upInfoByCodeMain(TQuery tQuery) {
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
			
			// 放入本地缓存
			if(v != null && topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
				localCache.put(cacheKey, v);
			}
		}
		
		// 最后查询数据库
		if(v == null){
			t = topInitInfoMain(tQuery);
			
			v = GsonHelper.toJson(t);
			XmasKv.upFactory(topConfig().getSchema()).setex(sCode,topConfig().getExpireSecond(), v);
			
			// 放入本地缓存
			if(v != null && topConfig().enabledLocalCache() && "1".equals(bConfig("xmassystem.enabledLocalCache"))){
				localCache.put(cacheKey, v);
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
}
