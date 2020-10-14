package com.srnpr.xmassystem.homehas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 同步家有接口的基类
 * 
 * @author srnpr
 * 
 * @param <TConfig>
 * @param <TRequest>
 * @param <TResponse>
 */
public abstract class XmasRsyncHomeHas<TRequest extends XmasRsyncHomeHas.IRsyncRequest, TResponse extends XmasRsyncHomeHas.IRsyncResponse>
		extends BaseClass {

	private TResponse processResult = null;

	/**
	 * 调用处理逻辑 返回操作
	 * 
	 * @param sRequestString
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 */
	private String getHttps(String sUrl, String sRequestString)
			throws ParseException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}

		webClientRequest.setFilePath(sDir);
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity = new StringEntity(sRequestString,
				TopConst.CONST_BASE_ENCODING);

		webClientRequest.setConentType("application/json");

		webClientRequest.setPassword(bConfig("groupcenter.rsync_homehas_password"));

		webClientRequest.setHttpEntity(httpEntity);

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream instream = new FileInputStream(new File(webClientRequest.getFilePath()));

		trustStore.load(instream, webClientRequest.getPassword().toCharArray());

		HttpClientBuilder cb = HttpClientBuilder.create();
		SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		cb.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
		
		// 所有的都是1秒，避免阻塞主逻辑
		RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectionRequestTimeout(5000)  
                .setConnectTimeout(5000)  
                .setSocketTimeout(5000)
                .build(); 
		
		cb.setDefaultRequestConfig(requestConfig);
		CloseableHttpClient httpClient = cb.build();

		HttpPost httppost = new HttpPost(webClientRequest.getUrl());

		httppost.setEntity(webClientRequest.getHttpEntity());

		if (StringUtils.isNotEmpty(webClientRequest.getConentType())) {

			httppost.setHeader("Content-Type", webClientRequest.getConentType());
		}

		// 设置成短链接模式 关闭keep-alve
		httppost.setHeader("Connection", "close");

		String sResponseString = null;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httppost);

			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				sResponseString = EntityUtils.toString(resEntity, TopConst.CONST_BASE_ENCODING);

			}
		} catch (Exception e) {
			e.printStackTrace();
			sResponseString = e.getMessage()+"";
		} finally {
			if(response != null){
				response.close();
			}
			httpClient.close();
		}
		
		return sResponseString;
	}

	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	public String upRequestUrl(){
		return bConfig("groupcenter.rsync_homehas_url") + getRsyncTarget();
	}
	
	public abstract String getRsyncTarget();
	
	public abstract TRequest upRsyncRequest();
	
	public abstract TResponse upResponseObject();
	
	public boolean doRsync() {

		String sCode = WebHelper.upCode("LCRL");

		boolean result = false;
		MDataMap mInsertMap = new MDataMap();
		try {

			String sUrl = upRequestUrl();

			TRequest tRequest = upRsyncRequest();

			JsonHelper<IRsyncRequest> requestJsonHelper = new JsonHelper<IRsyncRequest>();
			String sRequest = requestJsonHelper.ObjToString(tRequest);

			
			// 插入日志表调用的日志记录
			mInsertMap.inAllValues("code", sCode, "rsync_target", getRsyncTarget(), 
					"rsync_url", sUrl, "request_data", sRequest, "request_time", FormatHelper.upDateTime());
			
			String sResponseString = getHttps(sUrl, sRequest);
			
			mInsertMap.inAllValues("response_time", FormatHelper.upDateTime(), "response_data", sResponseString);
			
			TResponse tResponse = upResponseObject();
			JsonHelper<TResponse> responseJsonHelper = new JsonHelper<TResponse>();
			tResponse = responseJsonHelper.GsonFromJson(sResponseString.trim(),tResponse);

			processResult = tResponse;
			
			// 更新处理完成时间
			mInsertMap.inAllValues("process_time", FormatHelper.upDateTime(),
					"process_data", "{}", 
					"status_data", "",
					"flag_success", "1",
					"process_num", "-1",
					"success_num", "-1");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			mInsertMap.inAllValues("flag_success", "0", "process_time", FormatHelper.upDateTime(), "error_expection", String.valueOf(e));
			
			// 只记录异常日志
			DbUp.upTable("lc_rsync_log").dataInsert(mInsertMap);
		}

		return result;
	}

	/**
	 * 获取调用接口之后的结果
	 * 
	 * @return
	 */
	public TResponse upProcessResult() {
		return processResult;
	}
	
	public static interface IRsyncRequest {

	}
	
	public static interface IRsyncResponse {

	}
}
