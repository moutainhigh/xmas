package com.srnpr.xmassystem.homehas;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmassystem.load.LoadJDToken;
import com.srnpr.xmassystem.modelevent.PlusModelJDToken;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.baseface.IBaseCreate;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 调用京东接口公共方法
 * @remark 
 * @author 任宏斌
 * @date 2019年5月6日
 */
public class RsyncJingdongSupport extends BaseClass implements IBaseCreate {
	
	private static String JINGDONG_APPKEY = TopUp.upConfig("xmassystem.jingdong_app_key");
	private static String JINGDONG_GATEWAY = TopUp.upConfig("xmassystem.jingdong_gateway");
	private static String JINGDONG_FORMAT = TopUp.upConfig("xmassystem.jingdong_format");
	private static String JINGDONG_VERSION = TopUp.upConfig("xmassystem.jingdong_version");
	private static String JINGDONG_LOG_SWITCH = TopUp.upConfig("xmassystem.jingdong_log_switch");
	
	public static String callGateway(String target,Map<String, Object> params) {
		String result = "";
		MDataMap logParams = new MDataMap();
		MDataMap reqParams = new MDataMap();
		String paramJson = null == params ? "{}" : JSON.toJSONString(params);
		String requestTime = DateUtil.getSysDateTimeString();
		
		// 不记录日志的参数，有任意非null值则不记录此次请求日志
		Object logIgnore = params.remove("logIgnore");
		
		try {
			reqParams.put("method", target);
			reqParams.put("app_key", JINGDONG_APPKEY);
			reqParams.put("access_token", getToken());
			reqParams.put("timestamp", DateUtil.getSysDateTimeString());
			reqParams.put("format", JINGDONG_FORMAT);
			reqParams.put("v", JINGDONG_VERSION);
			reqParams.put("param_json", paramJson);
			result = WebClientSupport.upPost(JINGDONG_GATEWAY, reqParams);
			//避免中文乱码
			//result = new String(result.getBytes("ISO8859-1"),"UTF-8");
		
			
			logParams.put("response_time", DateUtil.getSysDateTimeString());
			logParams.put("response_data", result);
		}catch(Exception e) {
			logParams.put("exception_data", String.valueOf(e));
			e.printStackTrace();
		}finally {
			logParams.put("rsync_target", target);
			logParams.put("request_url", JINGDONG_GATEWAY);
			logParams.put("request_data", paramJson);
			logParams.put("request_time", requestTime);
			logParams.put("create_time", DateUtil.getSysDateTimeString());
			
			if(logIgnore == null) {
				addJingDongLog(logParams);
			}
		}
		
		return result;
	}
	
	private static String getToken() {
		LoadJDToken loadJDToken = new LoadJDToken();
		PlusModelJDToken plusModelJDToken = loadJDToken.upInfoByCode(new PlusModelQuery("jd"));
		String access_token = plusModelJDToken.getAccess_token();
		return access_token;
	}
	
	private static void addJingDongLog(MDataMap logParams) {
		if("on".equals(JINGDONG_LOG_SWITCH)) {
			String[] targets = {"biz.message.get","biz.message.del"};
			// 排除部分不重要的日志信息
			if(!ArrayUtils.contains(targets, logParams.get("rsync_target"))) {
				DbUp.upTable("lc_rsync_jingdong_log").dataInsert(logParams);
			}
		}
	}
	
}
