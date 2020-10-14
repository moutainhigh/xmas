package com.srnpr.xmassystem.duohuozhu.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.srnpr.xmassystem.duohuozhu.model.RequestModel;
import com.srnpr.xmassystem.duohuozhu.model.ResponseModel;
import com.srnpr.xmassystem.duohuozhu.utils.MD5Util;
import com.srnpr.xmassystem.duohuozhu.utils.XmlUtil;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.HttpClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

public class RsyncDuohuozhuSupport {

	private static String DHZ_MD5KEY = TopUp.upConfig("xmassystem.dhz_md5key");
	private static String DHZ_GATEWAY = TopUp.upConfig("xmassystem.dhz_gateway");
	private static String DHZ_DATA_TYPE = TopUp.upConfig("xmassystem.dhz_data_type");
	private static String DHZ_VERSION = TopUp.upConfig("xmassystem.dhz_version");
	private static String DHZ_LOG_SWITCH = TopUp.upConfig("xmassystem.dhz_log_switch");
	private static String DHZ_CP_ID = TopUp.upConfig("xmassystem.dhz_cp_id");
	
	@SuppressWarnings({ "unchecked" })
	public ResponseModel callGateway(RequestModel model) {
		ResponseModel result = null;
		String request = "";
		MDataMap logParams = new MDataMap();
		String requestTime = DateUtil.getSysDateTimeString();
		
		Map<String, Object> root = new LinkedHashMap<String, Object>();
		Map<String, Object> header = new LinkedHashMap<String, Object>();
		header.put("trade_id", UUID.randomUUID().toString().replaceAll("-", ""));
		header.put("function_id", model.getHead().getFunction_id());
		header.put("version", DHZ_VERSION);
		header.put("cp_id", DHZ_CP_ID);
		header.put("charset", model.getHead().getCharset());
		header.put("sign_type", model.getHead().getSign_type());
		header.put("data_type", DHZ_DATA_TYPE);
		header.put("request_time", DateUtil.getSysDateTimeString1());
		root.put("header", header);
		root.put("body", model.getBody());
		request = XmlUtil.createXmlByMap(root, "root", false);
		String signed = MD5Util.MD5Encode(request+DHZ_MD5KEY, "utf-8").toUpperCase();
		header.put("signed", signed);
		root.remove("header");
		root.remove("body");
		root.put("header", header);
		root.put("body", model.getBody());
		request = XmlUtil.createXmlByMap(root, "root", true);
		
		String resultStr = "";
		try {
			resultStr = HttpClientSupport.doPostDhz(DHZ_GATEWAY, request);
		
			Map<String, Object> resultMap = XmlUtil.createMapByXml(resultStr);
			result = new ResponseModel();
			Map<String, Object> respheader = (Map<String, Object>) resultMap.get("header");
			if(null != respheader && respheader.size() > 0) {
				result.getHeader().setTrade_id(respheader.get("trade_id")+"");
				result.getHeader().setFunction_id(respheader.get("function_id")+"");
				result.getHeader().setCp_id(respheader.get("cp_id")+"");
				result.getHeader().setSigned(respheader.get("signed")+"");
				result.getHeader().setResp_time(respheader.get("resp_time")+"");
				result.getHeader().setRequest_time(respheader.get("request_time")+"");
				result.getHeader().setResp_code(respheader.get("resp_code")+"");
				result.getHeader().setResp_msg(respheader.get("resp_msg")+"");
			}
			Map<String, Object> respbody = (Map<String, Object>) resultMap.get("body");
			if(null != respbody && respbody.size() > 0) {
				result.getBody().putAll(respbody);
			}
				
			logParams.put("response_time", DateUtil.getSysDateTimeString());
		}catch(Exception e) {
			logParams.put("exception_data", String.valueOf(e));
			e.printStackTrace();
		}finally {
			logParams.put("rsync_target", model.getHead().getFunction_id());
			logParams.put("request_url", DHZ_GATEWAY);
			logParams.put("request_data", request);
			logParams.put("request_time", requestTime);
			logParams.put("response_data", resultStr);
			logParams.put("create_time", DateUtil.getSysDateTimeString());
			addDuohuozhuLog(logParams);
		}
		
		return result;
	}
	
	private static void addDuohuozhuLog(MDataMap logParams) {
		if("on".equals(DHZ_LOG_SWITCH)) {
			DbUp.upTable("lc_rsync_duohz_log").dataInsert(logParams);
		}
	}
}
