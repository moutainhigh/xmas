package com.srnpr.xmaspay.util;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * xml解析工具类
 * @author pang_jhui
 *
 */
public class XmlUtil extends BaseClass {
	
	private volatile static XmlUtil INSTANCE = null;
	
	/**
	 * 获取xml工具类
	 * @return 工具类实例
	 */
	public synchronized static XmlUtil getInstance(){
		
		if(INSTANCE == null){
			
			synchronized (XmlUtil.class) {
				
				if(INSTANCE == null){
					
					INSTANCE = new XmlUtil();
					
				}
				
			}
			
		}
		
		return INSTANCE;
		
		
	}
	
	/**
	 * 将xml字符串转换为Map
	 * @param xmlStr
	 * 		待转换xml字符串
	 * @return 转换后map集合
	 */
	public  MDataMap xmlToMDataMap(String xmlStr){
		
		MDataMap mDataMap = null;
		
		try {
			
			Document document = DocumentHelper.parseText(xmlStr);
			
			Element rootElement = document.getRootElement();
			
			Iterator<?> elements = rootElement.elementIterator(); 
			
			if(elements.hasNext()){
				
				mDataMap = new MDataMap();
				
				while (elements.hasNext()) {
					
					Element element = (Element) elements.next();
					
					if(element.elements().size() > 0){
						
						String childXmlStr = convertXmlStr(element.elementIterator());
						
						mDataMap.put(element.getName(), childXmlStr);
						
					}else{
						
						mDataMap.put(element.getName(), element.getTextTrim());
						
					}
					
								
					
				}
				
			}
			 
			 
			
		} catch (DocumentException e) {
			
			bLogError(0,e.getMessage());
			
		}
		
		return mDataMap;
		
	}
	
	/**
	 * 将数据集合转换为xml字符串
	 * @param mDataMap
	 * 		数据集合
	 * @return xml字符串
	 */
	public String mDataMapToXml(MDataMap mDataMap){
		
		StringBuffer xmlBuffer = new StringBuffer("<xml>");
		
		Iterator<String> keys = mDataMap.keySet().iterator();
		
		while (keys.hasNext()) {
			
			String key = keys.next();
			
			String value = mDataMap.get(key);
			
			if(StringUtils.isNotBlank(value)){
				
				xmlBuffer.append("<"+key+">").append(value).append("</"+key+">");
				
			}
			
		}
		
		xmlBuffer.append("</xml>");
		
		return xmlBuffer.toString();
		
	}
	
	/**
	 * 将子节点保存在为字符串
	 * @param elments
	 * 		子节点集合
	 * @return 子节点字符串
	 */
	public String convertXmlStr(Iterator<?> elments){
		
		StringBuffer buffer = new StringBuffer();
		
		while (elments.hasNext()) {
			
			Element element = (Element) elments.next();
			
			buffer.append(element.asXML());
			
		}
		
		return buffer.toString();
		
	}

}
