package com.srnpr.xmassystem.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.homehas.RsyncJingdongSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class ThreadSpiderSiji implements Runnable {
	private List<Map<String,Object>> dataSqlList;
	public ThreadSpiderSiji(List<Map<String,Object>> dataSqlList) {
		this.dataSqlList = dataSqlList;
	}
	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		for(Map<String, Object> map : dataSqlList) {
			MDataMap mDataMap = new MDataMap();
			String string = map.get("p_code").toString();
			String sql = "select * from sc_jingdong_address where p_code = " + string;
			List<Map<String,Object>> dataSqlList2 = DbUp.upTable("sc_jingdong_address").dataSqlList(sql, mDataMap);
			for(Map<String,Object> map1 : dataSqlList2) {
				String codefu = map1.get("code").toString();
				String namefu = map1.get("name").toString();
				System.out.println(threadName + " 开始爬	"+namefu + ":" + codefu +"	这个地址的子集");
				HashMap<String, Object> mDataMapparam = new HashMap<String, Object>();
				mDataMapparam.put("id", codefu);
				String callGateway = RsyncJingdongSupport.callGateway("biz.address.townsByCountyId.query", mDataMapparam);
				JSONObject parseObject = JSONObject.parseObject(callGateway);
				JSONObject jsonObject = parseObject.getJSONObject("biz_address_townsByCountyId_query_response");
				String success = jsonObject.get("success").toString();
				String recode = jsonObject.get("code").toString();
				String resultCode = jsonObject.get("resultCode").toString();
				if("true".equals(success) && "0".equals(recode) && "0000".equals(resultCode)){
					JSONObject jsonObject2 = jsonObject.getJSONObject("result");
					for(String name : jsonObject2.keySet()) {
						String code = jsonObject2.getString(name);
						MDataMap dataMap = new MDataMap();
						dataMap.put("code", code);
						dataMap.put("p_code", codefu);
						dataMap.put("name", name);
						dataMap.put("code_lvl", "4");
						DbUp.upTable("sc_jingdong_address").dataInsert(dataMap);
					}
					System.out.println(threadName + " 开始爬	"+namefu + ":" + codefu + "	这个地址的子集成功==========");
				}else {
					System.out.println(threadName + " 开始爬	"+namefu + ":" + codefu + "	这个地址的子集失败********** 返回: " + callGateway);
				}
			}
		}
	}

}
