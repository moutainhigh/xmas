package com.srnpr.xmaspay.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webpage.RootProcess;

/**
 * 支付网关相关web控制器
 * @author pang_jhui
 *
 */

@Controller
@RequestMapping("payGate")
public class PayGateController extends BaseClass {
	
	/**
	 * 提供给支付网关回调
	 * @param request
	 * 		请求信息
	 * @return 响应信息
	 */
	@RequestMapping(value = "/payGateCallBack")
	public String payGateCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/html;charset=UTF-8");
		
		MDataMap mDataMap = new RootProcess().convertRequest(request);

		String returnStr = PayServiceFactory.getInstance().getPayGateWayCallBackProcess().process(mDataMap);
		
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = null;
		
		try {
			
			out = response.getWriter();
			out.print(returnStr);
			out.close();
			
		} catch (IOException e) {
			bLogError(0, e.getMessage());
		}
		
		return null;

	}
	
	/**
	 * 提供给银联支付回调回调
	 * @param request
	 * 		请求信息
	 * @return 响应信息
	 */
	@RequestMapping(value = "/unionPayCallBack")
	public String unionPayCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/html;charset=UTF-8");
		
		MDataMap mDataMap = new RootProcess().convertRequest(request);

		String returnStr = PayServiceFactory.getInstance().getPayGateWayCallBackProcess().process(mDataMap);
		
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = null;
		
		try {
			
			out = response.getWriter();
			out.print(returnStr);
			out.close();
			
		} catch (IOException e) {
			bLogError(0, e.getMessage());
		}
		
		return null;

	}
	
	/**
	 * 提供给applePay回调
	 * @param request
	 * 		请求信息
	 * @return 响应信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/applePayCallBack")
	public String applePayCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		Map<String,Object> param = new HashMap<String, Object>();
		ServletInputStream in = null;
		try {
			in = request.getInputStream();
			String jsonText = IOUtils.toString(in, "UTF-8");
			param = JSONObject.parseObject(jsonText, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		MDataMap mDataMap = new MDataMap(param);

		String returnStr = PayServiceFactory.getInstance().getApplePayCallBackProcess().process(mDataMap);
		
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = null;
		
		try {
			
			out = response.getWriter();
			out.print(returnStr);
			out.close();
			
		} catch (IOException e) {
			bLogError(0, e.getMessage());
		}
		
		return null;

	}

}
