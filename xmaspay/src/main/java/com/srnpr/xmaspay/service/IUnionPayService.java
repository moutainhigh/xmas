package com.srnpr.xmaspay.service;

import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.face.IPayService;
import com.srnpr.xmaspay.request.UnionPayRequest;
import com.srnpr.xmaspay.response.UnionPayResponse;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 银联支付业务实现类
 * @author pang_jhui
 *
 */
public interface IUnionPayService extends IPayService {
	
	/**
	 * 银联支付相关业务处理
	 * @param unionPayRequest
	 * 		支付网关请求信息
	 * @param payConfig
	 * 		支付相关配置信息
	 * @return UnionPayResponse
	 */
	public UnionPayResponse doProcess(UnionPayRequest unionPayRequest,IUnionPayConfig payConfig);
	
	/**
	 * 针对请求参数参数进行签名
	 * @param signStr
	 * 		请求参数
	 * @param encoding
	 * 		编码
	 * @return 签名字符串
	 */
	public String sign(String signStr,String encoding);
	
	/**
	 * 按照key=value&。。。形式组装字符串
	 * @param mDataMap
	 * 		请求参数
	 * @return 组装后的字符串
	 */
	public String convertMapToStr(MDataMap mDataMap);
	
	/**
	 * 校验回调数据的有效性
	 * @param mDataMap
	 * @param encoding
	 * @return
	 * @throws Exception 
	 */
	public boolean validate(MDataMap mDataMap, String encoding) throws Exception;

}
