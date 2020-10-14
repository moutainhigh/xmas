package com.srnpr.xmassystem.spider;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.homehas.RsyncJingdongSupport;
import com.srnpr.zapcom.basehelper.ThreadTestHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class YijiAddressSpider /*extends ThreadTestHelper*/{
//	public static void main(String[] args) throws Exception{
//		String callGateway = RsyncJingdongSupport.callGateway("biz.address.allProvinces.query", new HashMap<String,Object>());
//		JSONObject parseObject = JSONObject.parseObject(callGateway);
//		JSONObject jsonObject = parseObject.getJSONObject("biz_address_allProvinces_query_response");
//		if("0".equals(jsonObject.get("code").toString()) && "0000".equals(jsonObject.get("resultCode").toString())){
//			JSONObject jsonObject2 = jsonObject.getJSONObject("result");
//			for(String name : jsonObject2.keySet()) {
//				String code = jsonObject2.getString(name);
//				MDataMap dataMap = new MDataMap();
//				dataMap.put("code", code);
//				dataMap.put("name", name);
//				dataMap.put("code_lvl", "1");
//				DbUp.upTable("sc_jingdong_address").dataInsert(dataMap);
//			}
//		}
//		
//		
//		System.out.println(callGateway);
//	}
//	public static void main(String[] args) throws Exception{
//		MDataMap mDataMap = new MDataMap();
//		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_jingdong_address").dataSqlList("select * from sc_jingdong_address where code_lvl = 1", mDataMap);
//		for(Map<String, Object> map : dataSqlList) {
//			HashMap<String,Object> mDataMapparam = new HashMap<String,Object>();
//			String codefu = map.get("code").toString();
//			String namefu = map.get("name").toString();
//			System.out.println("开始爬	"+namefu+"	这个地址的子集");
//			mDataMapparam.put("id", codefu);
//			String callGateway = RsyncJingdongSupport.callGateway("biz.address.citysByProvinceId.query", mDataMapparam);
//			JSONObject parseObject = JSONObject.parseObject(callGateway);
//			JSONObject jsonObject = parseObject.getJSONObject("biz_address_citysByProvinceId_query_response");
//			String success = jsonObject.get("success").toString();
//			String recode = jsonObject.get("code").toString();
//			String resultCode = jsonObject.get("resultCode").toString();
//			if("true".equals(success) && "0".equals(recode) && "0000".equals(resultCode)){
//				JSONObject jsonObject2 = jsonObject.getJSONObject("result");
//				for(String name : jsonObject2.keySet()) {
//					String code = jsonObject2.getString(name);
//					MDataMap dataMap = new MDataMap();
//					dataMap.put("code", code);
//					dataMap.put("p_code", codefu);
//					dataMap.put("name", name);
//					dataMap.put("code_lvl", "2");
//					DbUp.upTable("sc_jingdong_address").dataInsert(dataMap);
//				}
//				System.out.println("开始爬	"+namefu+"	这个地址的子集成功==========");
//			}else {
//				System.out.println("开始爬	"+namefu+"	这个地址的子集失败**********");
//			}
//			
//		}
//	}
//	public static void main(String[] args) throws Exception{
//		MDataMap mDataMap = new MDataMap();
//		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_jingdong_address").dataSqlList("select * from sc_jingdong_address where code_lvl = 2", mDataMap);
//		for(Map<String, Object> map : dataSqlList) {
//			HashMap<String,Object> mDataMapparam = new HashMap<String,Object>();
//			String codefu = map.get("code").toString();
//			String namefu = map.get("name").toString();
//			System.out.println("开始爬	"+namefu+"	这个地址的子集");
//			mDataMapparam.put("id", codefu);
//			String callGateway = RsyncJingdongSupport.callGateway("biz.address.countysByCityId.query", mDataMapparam);
//			JSONObject parseObject = JSONObject.parseObject(callGateway);
//			JSONObject jsonObject = parseObject.getJSONObject("biz_address_countysByCityId_query_response");
//			String success = jsonObject.get("success").toString();
//			String recode = jsonObject.get("code").toString();
//			String resultCode = jsonObject.get("resultCode").toString();
//			if("true".equals(success) && "0".equals(recode) && "0000".equals(resultCode)){
//				JSONObject jsonObject2 = jsonObject.getJSONObject("result");
//				for(String name : jsonObject2.keySet()) {
//					String code = jsonObject2.getString(name);
//					MDataMap dataMap = new MDataMap();
//					dataMap.put("code", code);
//					dataMap.put("p_code", codefu);
//					dataMap.put("name", name);
//					dataMap.put("code_lvl", "3");
//					DbUp.upTable("sc_jingdong_address").dataInsert(dataMap);
//				}
//				System.out.println("开始爬	"+namefu+"	这个地址的子集成功==========");
//			}else {
//				System.out.println("开始爬	"+namefu+"	这个地址的子集失败**********");
//			}
//			
//		}
//	}
//	public static void main(String[] args) {
//		MDataMap mDataMap = new MDataMap();
//		String sql = "SELECT\r\n" + 
//				"	sc.*\r\n" + 
//				"FROM\r\n" + 
//				"	(\r\n" + 
//				"		SELECT\r\n" + 
//				"			*\r\n" + 
//				"		FROM\r\n" + 
//				"			sc_jingdong_address\r\n" + 
//				"		WHERE\r\n" + 
//				"			code_lvl = 3\r\n" + 
//				"	) sc\r\n" + 
//				"LEFT OUTER JOIN (\r\n" + 
//				"	SELECT\r\n" + 
//				"		*\r\n" + 
//				"	FROM\r\n" + 
//				"		sc_jingdong_address\r\n" + 
//				"	WHERE\r\n" + 
//				"		code_lvl = 4\r\n" + 
//				") scc ON sc. CODE = scc.p_code WHERE scc.name is null";
//		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_jingdong_address").dataSqlList(sql, mDataMap);
//		for(Map<String, Object> map : dataSqlList) {
//			mDataMap.clear();
//			String codefu = map.get("code").toString();
//			String namefu = map.get("name").toString();
//			System.out.println("开始爬	"+namefu+"	这个地址的子集");
//			mDataMap.put("id", codefu);
//			String callGateway = RsyncJingdongSupport.callGateway("biz.address.townsByCountyId.query", mDataMap);
//			JSONObject parseObject = JSONObject.parseObject(callGateway);
//			JSONObject jsonObject = parseObject.getJSONObject("biz_address_townsByCountyId_query_response");
//			String success = jsonObject.get("success").toString();
//			String recode = jsonObject.get("code").toString();
//			String resultCode = jsonObject.get("resultCode").toString();
//			if("true".equals(success) && "0".equals(recode) && "0000".equals(resultCode)){
//				JSONObject jsonObject2 = jsonObject.getJSONObject("result");
//				for(String name : jsonObject2.keySet()) {
//					String code = jsonObject2.getString(name);
//					MDataMap dataMap = new MDataMap();
//					dataMap.put("code", code);
//					dataMap.put("p_code", codefu);
//					dataMap.put("name", name);
//					dataMap.put("code_lvl", "567");
//					DbUp.upTable("sc_jingdong_address").dataInsert(dataMap);
//				}
//				System.out.println("开始爬	"+namefu+"	这个地址的子集成功==========");
//			}else {
//				System.out.println("开始爬	"+namefu+"	这个地址的子集失败**********");
//			}
//		
//	}
	public static void main(String[] args) throws Exception{
		MDataMap mDataMap = new MDataMap();
		String sql = "select sc.p_code as p_code from  sc_jingdong_address sc WHERE sc.code_lvl = 3 GROUP BY sc.p_code ORDER BY p_code LIMIT %s,%s";
		for(int i = 0;1 == 1;i+=40) {
			String sqlExe = String.format(sql, i,40);
			List<Map<String,Object>> dataSqlList = DbUp.upTable("sc_jingdong_address").dataSqlList(sqlExe, mDataMap);
			if(dataSqlList.size() > 0) {
				System.out.println(i);
				new Thread(new ThreadSpiderSiji(dataSqlList)).start();
			}else {
				break;
			}
		}
		
	}
}
