package com.github.yangwk.ydtutil.http;

import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	/**
	 * 默认编码
	 */
	public static final String CHARSET = "UTF-8";
	
	/**
	 * 默认配置
	 */
	public static final RequestConfig DEFAULT_CONFIG = RequestConfig.custom()
													.setConnectionRequestTimeout(20*1000)
													.setConnectTimeout(20*1000)
													.setSocketTimeout(20*1000)
													.build();	//设置超时，避免卡死
	
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * 获取文件
	 * @author yangwk
	 *
	 * @param url will request url, include or not parameters
	 * @param header will request header, it's &lt; name,value &gt;
	 * @param targetPath 保存的文件绝对路径
	 * @return
	 */
	public static void getFile(final String url,Map<String, String> header,String targetPath) {
	    CloseableHttpClient httpClient = null;
	    try {
	    	httpClient = createCloseableHttpClient();
	    	HttpGet httpget = httpGet(url);
	    	
	    	addHeader(httpget, header);
	    	
	    	log.info("Request >> url : {}" , httpget.getURI().toString() );
	    	
			CloseableHttpResponse response = null;
	        try{
	        	response = httpClient.execute(httpget);
	        	
	        	assertOk(response);
	        	
	        	HttpEntity entity = response.getEntity();
	            InputStream instream = entity.getContent();
	            
	            File targetFile = new File(targetPath);
	            log.info("copyToFile " + targetFile.getAbsolutePath());
	            
	            FileUtils.forceMkdirParent(targetFile);
	            FileUtils.copyToFile(instream, targetFile);
	            
	            EntityUtils.consume(entity);
	        }finally{
	        	IOUtils.closeQuietly(response);
	        }
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }finally{
	    	IOUtils.closeQuietly(httpClient);
	    }
	}


	/**
	 * 
	 * @author yangwk
	 *
	 * @param url will request url, include or not parameters
	 * @param header will request header, it's &lt; name,value &gt;
	 * @return
	 */
	public static String get(final String url,Map<String, String> header) {
	    String result = null;
	    CloseableHttpClient httpClient = null;
	    try {
	    	httpClient = createCloseableHttpClient();
	    	HttpGet httpget = httpGet(url);
	    	
	    	addHeader(httpget, header);
	    	
	    	log.info("Request >> url : {}" , httpget.getURI().toString() );
	    	
	        result = executeRequest(httpClient, httpget);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }finally{
	    	IOUtils.closeQuietly(httpClient);
	    }
	    
	    return result;
	}
	

	/**
	 * json格式的post请求
	 * @author yangwk
	 *
	 * @param url
	 * @param body
	 * @return
	 */
	public static String postJson(final String url,final String body){
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset="+CHARSET);
		return post(url, body, header);
	}

	/**
	 * post
	 * @author yangwk
	 *
	 * @param url will request url
	 * @param body will request body
	 * @param header will request header, it's &lt; name,value &gt;
	 * @return
	 */
	public static String post(final String url,final String body,Map<String, String> header) {
        String result = null;
        CloseableHttpClient httpClient = null;
        try {
        	httpClient = createCloseableHttpClient();
        	HttpPost httpost = httpPost(url);
        	
            StringEntity entity = new StringEntity(body, CHARSET);
            entity.setContentType(CHARSET);
            
            httpost.setEntity(entity);
            
            addHeader(httpost, header);
            
            log.info("Request >> url : {}, body : {}" , httpost.getURI().toString(),body);
            
            result = executeRequest(httpClient, httpost);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }finally{
        	IOUtils.closeQuietly(httpClient);
        }
        
        return result;
    }
	
	
	private static CloseableHttpClient createCloseableHttpClient() throws Exception{
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
        	
        	@Override
            public boolean isTrusted(X509Certificate[] chain,
                                     String authType) throws CertificateException {
                return true;	//信任所有
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//    	httpClient = HttpClients.createDefault();
        return httpClient;
    }
	
	
	private static String executeRequest(CloseableHttpClient httpClient, HttpUriRequest request) throws Exception{
		String result = null;
		CloseableHttpResponse response = null;
        try{
        	response = httpClient.execute(request);
        	
        	assertOk(response);
        	
        	HttpEntity entity = response.getEntity();
            
            result = entity != null ? EntityUtils.toString(entity, CHARSET) : null;
            
            EntityUtils.consume(entity);
            
            log.info("Response << {}" , result);
        }finally{
        	IOUtils.closeQuietly(response);
        }
        return result;
	}
	
	private static void assertOk(HttpResponse response){
		int status = response.getStatusLine().getStatusCode();
		boolean ok = (200 <= status && status < 300);
		if (! ok)
    		throw new RuntimeException("response StatusCode "+ status + " is not "+HttpStatus.SC_OK);
	}
	
	private static void addHeader(HttpMessage httpMessage, Map<String, String> header){
		if(header != null){
        	for(Map.Entry<String, String> entry : header.entrySet()){
        		httpMessage.addHeader( entry.getKey(), entry.getValue());
        	}
        }
	}
	
	private static HttpGet httpGet(String url){
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(DEFAULT_CONFIG);
		return httpget;
	}


	private static HttpPost httpPost(String url){
		HttpPost httpost = new HttpPost(url);
		httpost.setConfig(DEFAULT_CONFIG);
		return httpost;
	}
	
}
