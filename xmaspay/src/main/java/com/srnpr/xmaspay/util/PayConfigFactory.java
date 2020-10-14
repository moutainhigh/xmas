package com.srnpr.xmaspay.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.srnpr.xmaspay.face.IPayConfig;
import com.srnpr.zapcom.baseface.IBaseInstance;

/**
 * 支付配置项工厂类
 * @author zhaojulning
 *
 */
public class PayConfigFactory implements IBaseInstance{

	/**
	 * 工程类实例
	 */
	private volatile static PayConfigFactory instance = null;
	
	private Map<String,IPayConfig> configMap = new ConcurrentHashMap<String, IPayConfig>();
	
	/**
	 * 获取支付相关业务处理实例
	 * @return
	 */
	public static PayConfigFactory getInstance(){
		if(instance == null){
			synchronized (PayConfigFactory.class) {
				if(instance == null){
					instance = new PayConfigFactory();
				}
			}
		}
		return instance;		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IPayConfig> T getPayConfig(Class<T> cls){
		String key = cls.getName();
		if(!configMap.containsKey(key)){
			try {
				configMap.put(key, cls.newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (T)configMap.get(key);
	}
	
}
