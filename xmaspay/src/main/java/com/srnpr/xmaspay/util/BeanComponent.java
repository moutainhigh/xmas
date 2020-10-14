package com.srnpr.xmaspay.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 实体bean处理
 * @author pangjh
 *
 */
public class BeanComponent {
	
	/*实体bean处理处理实例*/
	private volatile static BeanComponent INSTANCE = null;
	
	/**
	 * 获取实体bean处理实例
	 * @return BeanComponent
	 */
	public static BeanComponent getInstance(){
		
		if(INSTANCE == null){
			
			synchronized (BeanComponent.class) {
				
				if(INSTANCE == null){
					
					INSTANCE = new BeanComponent();
					
				}
				
			}
			
		}
		
		return INSTANCE;
		
	}
	
	/**
	 * 反射相关实体
	 * @param clazz
	 * 		实体class
	 * @param mDataMap
	 * 		实体数据信息
	 * @return Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T invoke(Class clazz,MDataMap mDataMap,boolean isContainBase) throws Exception{
		
		Field[] currFields = clazz.getDeclaredFields();
		
		Field[] superFields = clazz.getSuperclass().getDeclaredFields();
		
		List<Field> fields = new ArrayList<Field>();
		
		Collections.addAll(fields, currFields);
		
		Collections.addAll(fields, superFields);
		
		if(isContainBase){
			
			Field[] baseFields = clazz.getSuperclass().getSuperclass().getDeclaredFields();
			
			Collections.addAll(fields, baseFields);
			
		}
		
		Object object = clazz.newInstance();

		for (Field field : fields) {
			
			String name = field.getName();
			
			String typeName = field.getType().getName();
			
			String prex = name.substring(0, 1);
			
			String methodName = "set"+prex.toUpperCase()+name.substring(1);
			
			Method method = clazz.getMethod(methodName, field.getType());
			
			if(mDataMap.containsKey(name)){
				
				String valueStr = mDataMap.get(name);
				
				if(StringUtils.isNotBlank(valueStr)){
					
					if(BigDecimal.class.getName().equals(typeName)){
						
						BigDecimal value = new BigDecimal(valueStr);
						
						method.invoke(object, value);
						
					}else if(Integer.class.getName().equals(typeName) || "int".equals(typeName)){
						
						int value = Integer.parseInt(valueStr);
						
						method.invoke(object, value);
						
					}else{
						
						method.invoke(object, valueStr);
						
					}	
					
				}				
				
			}
			
		}
		
		return (T)object;	
		
	}
	
	/**
	 * 将对象属性字段转换为Map集合
	 * @param object
	 * 		待转换对象
	 * @param baseClass
	 * 		基类
	 * @param isContainBase
	 * 		是否包含基类
	 * @return MDataMap
	 * 		字段集合
	 * @throws Exception
	 */
	public MDataMap objectToMap(Object object,Class<?> baseClass,boolean isContainBase) throws Exception{
		
		Class<?> clazz = object.getClass();
		
		List<Field> fields = new ArrayList<Field>();
		
		if(baseClass != null){
			
			for(;clazz != baseClass;clazz=clazz.getSuperclass()){
				
				Collections.addAll(fields, clazz.getDeclaredFields());
				
			}
			
			if(isContainBase){
				
				Collections.addAll(fields, baseClass.getDeclaredFields());
				
			}
			
		}else{
			
			Collections.addAll(fields, clazz.getDeclaredFields());
			
		}
		
		MDataMap mDataMap = new MDataMap();
		
		for (Field field : fields) {
			
			String name = field.getName();
			
			String prex = name.substring(0, 1);
			
			String methodName = "get"+prex.toUpperCase()+name.substring(1);
			
			Method method = object.getClass().getMethod(methodName);
			
			Object obj = method.invoke(object);
			
			if(obj != null){
				mDataMap.put(name, obj.toString());
			}
		}
		
		return mDataMap;
		
	}
	
	
	/**
	 * 解析api
	 * @param className
	 * 		类名称
	 * @param methodName
	 * 		要执行方法名称
	 * @param parameterTypes
	 * 		参数类型集合
	 * @param args
	 * 		参数值集合
	 * @return 返回对象
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public Object processApi(String className,String methodName,Class[] parameterTypes,Object[] args) throws Exception{
		
		Class object = Class.forName(className);
		
		Method method =  object.getMethod(methodName, parameterTypes);
		
		return method.invoke(object.newInstance(), args);
		
	}
	
	/**
	 * 赋值map数据集合
	 * @param sourceDataMap
	 * 		提供复制的集合
	 * @param targetDataMap
	 * 		待复制的集合
	 * @param ignorekeys
	 * 		复制的key
	 */
	public void copyDataMap(MDataMap sourceDataMap,MDataMap targetDataMap,String... keys){
		
		if(sourceDataMap != null && targetDataMap != null ){
			
			List<String> ignoreList = (keys != null) ? Arrays.asList(keys) : null;
			
			Iterator<String> it = sourceDataMap.keySet().iterator();
			
			/*是否全部复制*/
			boolean flag = (ignoreList != null && ignoreList.size() > 0) ? true : false;
			
			while (it.hasNext()) {
				
				String key = (String) it.next();
				/*需要复制key的集合非空，则只根据集合中的key复制*/
				if(flag){
					
					if(ignoreList.contains(key)){
						
						targetDataMap.put(key, sourceDataMap.get(key));
						
					}
					
				}else{
					/*需要复制key集合为空，则全部复制*/
					targetDataMap.put(key, sourceDataMap.get(key));
					
				}
				
			}
			
		}		
		
	}
	
	/**
	 * 将map中value中的对象转换为Object
	 * @param paramsMap
	 * 		参数集合
	 * @return Map<String ,String>
	 */
	public MDataMap convertTodataMap(Map<String ,Object> paramsMap){
		
		MDataMap maps = null;
		
		if(paramsMap != null){
			
			maps = new MDataMap();
			
			Iterator<String> keys = paramsMap.keySet().iterator();
			
			while (keys.hasNext()) {
				
				String key = keys.next();
				
				Object value = paramsMap.get(key);
				
				if (value instanceof String) {
					
					String new_val = (String) value;
					
					maps.put(key, new_val);
					
					
				}
				
				
			}
			
			
		}
		
		return maps;
		
		
	}
	
	/**
	 * 针对签名参数按照ASCII码进行排序
	 * @param split
	 * 		分隔符
	 * @param mDataMap
	 * 		待处理的数据集合
	 * @return 排序后的参数
	 */
	public String sortParam(String split, MDataMap mDataMap){
		
		String paramStr = "";
		
		StringBuffer digestBuffer = new StringBuffer();
		
		if(mDataMap != null){
			
			List<String> keyList = new ArrayList<String>(mDataMap.keySet());
			
			Collections.sort(keyList);
			
			for (String fileName : keyList) {

				String filedValue = mDataMap.get(fileName);

				if (StringUtils.isNotBlank(filedValue)) {

					digestBuffer.append(split).append(fileName).append("=").append(filedValue);

				}

			}
			
			if(digestBuffer.length() > 0){
				
				paramStr = digestBuffer.substring(1).toString();
				
			}
			
			
		}
		
		return paramStr;
		
		
	}
	
	public static JSONObject dataMapToJSONObject(MDataMap mDataMap){
		Map<String,Object> map = new HashMap<String, Object>((int)(mDataMap.size()/0.75+1));
		map.putAll(mDataMap);
		return new JSONObject(map);
	}

}
