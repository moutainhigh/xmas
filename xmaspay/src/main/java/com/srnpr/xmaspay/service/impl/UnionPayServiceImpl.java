package com.srnpr.xmaspay.service.impl;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.common.UnionPayConstant;
import com.srnpr.xmaspay.config.face.IUnionPayConfig;
import com.srnpr.xmaspay.request.UnionPayBaseRequest;
import com.srnpr.xmaspay.request.UnionPayRequest;
import com.srnpr.xmaspay.response.UnionPayResponse;
import com.srnpr.xmaspay.service.IUnionPayService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.UnionPayCertUtil;
import com.srnpr.xmaspay.util.UnionPaySecureUtil;
import com.srnpr.xmaspay.util.UnionPayUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;

/**
 * 银联支付业务处理类
 * @author pang_jhui
 *
 */
public class UnionPayServiceImpl extends BaseClass implements IUnionPayService {

	@Override
	public String sign(String signStr, String encoding) {
		
		return UnionPayUtil.getInstance().sign(signStr, encoding);
		
	}

	@Override
	public String convertMapToStr(MDataMap mDataMap) {

		String signStr = "";

		if (mDataMap.containsKey(UnionPayConstant.PARAM_SIGNATURE)) {

			mDataMap.remove(UnionPayConstant.PARAM_SIGNATURE);

		}
		
		TreeMap<String, String> treeMap = new TreeMap<String, String>(mDataMap);

		StringBuffer buffer = new StringBuffer();

		Iterator<String> keys = treeMap.keySet().iterator();

		while (keys.hasNext()) {

			String key = keys.next();

			String value = mDataMap.get(key);

			if (StringUtils.isNotBlank(value)) {

				buffer.append(key).append(UnionPayConstant.AMPERSAND).append(UnionPayConstant.EQUAL).append(value);

			}

		}

		if (buffer.length() > 0) {

			signStr = buffer.substring(1);

		}

		return signStr;

	}

	@Override
	public UnionPayResponse doProcess(UnionPayRequest unionPayRequest, IUnionPayConfig payConfig) {
		
		String returnStr = "";
		
		UnionPayResponse response = new UnionPayResponse();
		
		
		try {
			
			MDataMap mDataMap = BeanComponent.getInstance().objectToMap(unionPayRequest, UnionPayBaseRequest.class,false);
			
			returnStr = WebClientSupport.upPost(payConfig.getRequestUrl(),mDataMap);
			
		} catch (Exception e) {
			
			response.setResultcode(-1);
			
			response.setResultmsg(e.getMessage());
			
			bLogError(0, e.getMessage());
			
		}
		
		JsonHelper<UnionPayResponse> responseHelper = new JsonHelper<UnionPayResponse>();		
		
		try {
			
			response = responseHelper.StringToObjExp(returnStr, response);
			
		} catch (IOException e) {
			
			response.setResultcode(-1);
			
			response.setResultmsg(returnStr);
			
		}
		
		
		
		return response;
	}

	@Override
	public boolean validate(MDataMap mDataMap, String encoding) throws Exception {
		
		boolean flag = false;
		
		String signSource = mDataMap.get(UnionPayConstant.PARAM_SIGNATURE);
		
		String sourceCertId = mDataMap.get(UnionPayConstant.PARAM_CERTID);
		
		String signData = convertMapToStr(mDataMap);
		
		PublicKey publicKey = UnionPayCertUtil.getInstance().getValidateKey(sourceCertId);
		
		byte[] signDataByte = UnionPaySecureUtil.getInstance().base64Encode(signSource.getBytes(encoding));
		
		byte[] srcData = UnionPaySecureUtil.getInstance().sha1X16(signData, encoding);
		
		/*验证公钥及签名是否正确*/
		flag = UnionPaySecureUtil.getInstance().validateSignBySoft(publicKey, signDataByte, srcData);
		
		return flag;
	}
	

}
