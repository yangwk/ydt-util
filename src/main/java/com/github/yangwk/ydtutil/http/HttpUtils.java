package com.github.yangwk.ydtutil.http;

import java.io.Closeable;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http工具类
 * @author yangwk
 *
 */
public class HttpUtils {
	private HttpUtils(){}
	

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
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
	
	
	private static CloseableHttpClient _HttpClient = null;
	
	/**
	 * delete
	 * @author yangwk
	 *
	 */
	public static HttpResponseEntity delete(final String url,Map<String, String> header) {
		HttpDelete httpDelete = httpDelete(url);
		return simpleRequest(httpDelete, header);
	}

	/**
	 * get
	 * @author yangwk
	 *
	 */
	public static HttpResponseEntity get(final String url,Map<String, String> header) {
		HttpGet httpget = httpGet(url);
	    return simpleRequest(httpget, header);
	}
	

	/**
	 * 简单请求，没有body
	 * @author yangwk
	 */
	private static HttpResponseEntity simpleRequest(HttpRequestBase request, Map<String, String> header) {
		HttpResponseEntity result = null;
	    CloseableHttpClient httpClient = null;
	    try {
	    	httpClient = createCloseableHttpClient();
	    	
	    	addHeader(request, header);
	    	
	    	LOG.info("Request >> url : {}" , request.getURI().toString() );
	    	
	        result = executeRequest(httpClient, request);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }finally{
	    	closeHttpClient(httpClient);
	    }
	    
	    return result;
	}

	/**
	 * form表单post请求
	 * @author yangwk
	 */
	public static HttpResponseEntity postForm(final String url,final Map<String, String[]> parameterMap, Map<String, String> header){
		HttpResponseEntity result = null;
        CloseableHttpClient httpClient = null;
        try {
        	httpClient = createCloseableHttpClient();
        	HttpPost httpost = httpPost(url);
        	
        	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        	if(parameterMap != null) {
        		for(Entry<String, String[]> entry : parameterMap.entrySet()) {
        			String name = entry.getKey();
        			String[] values = entry.getValue();
        			for(int r=0; values != null && r < values.length; r ++) {
        				BasicNameValuePair pair = new BasicNameValuePair(name, values[r]);	//多个值
        				parameters.add(pair);
        			}
        		}
        	}
        	
        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, CHARSET);
            
            httpost.setEntity(entity);
            
            addHeader(httpost, header);
            
            LOG.info("Request >> url : {}, parameters : {}" , httpost.getURI().toString(), parameters.toString());
            
            result = executeRequest(httpClient, httpost);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }finally{
        	closeHttpClient(httpClient);
        }
        
        return result;
	}

	public static HttpResponseEntity postByte(final String url,final byte[] body,Map<String, String> header) {
		HttpResponseEntity result = null;
	    CloseableHttpClient httpClient = null;
	    try {
	    	httpClient = createCloseableHttpClient();
	    	HttpPost httpost = httpPost(url);
	    	
	    	ByteArrayEntity entity = new ByteArrayEntity(body);
	        
	        httpost.setEntity(entity);
	        
	        addHeader(httpost, header);
	        
	        LOG.info("Request >> url : {}" , httpost.getURI().toString());
	        
	        result = executeRequest(httpClient, httpost);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }finally{
	    	closeHttpClient(httpClient);
	    }
	    
	    return result;
	}

	private static HttpResponseEntity postString(final String url,final String body,Map<String, String> header) {
		HttpResponseEntity result = null;
        CloseableHttpClient httpClient = null;
        try {
        	httpClient = createCloseableHttpClient();
        	HttpPost httpost = httpPost(url);
        	
            StringEntity entity = new StringEntity(body, CHARSET);
            
            httpost.setEntity(entity);
            
            addHeader(httpost, header);
            
            LOG.info("Request >> url : {}, body : {}" , httpost.getURI().toString(), body);
            
            result = executeRequest(httpClient, httpost);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }finally{
        	closeHttpClient(httpClient);
        }
        
        return result;
    }
	
	
	/**
	 * json格式的post请求
	 * @author yangwk
	 */
	public static String postJson(final String url,final String body, Map<String, String> header){
		return postJson2(url, body, header).getResult();
	}
	
	
	/**
	 * json格式的post请求
	 * @author yangwk
	 */
	public static HttpResponseEntity postJson2(final String url,final String body, Map<String, String> header){
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json;charset="+CHARSET);
		
		if(header != null){
			headerMap.putAll(header);
		}
		
		return postString(url, body, headerMap);
	}
	
	
	private static void closeHttpClient(CloseableHttpClient client) {
//		closeQuietly( client );	//not close
	}

	private static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
	
	
	private synchronized static CloseableHttpClient createCloseableHttpClient() throws Exception{
		if(_HttpClient == null) {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
	        	
	        	@Override
	            public boolean isTrusted(X509Certificate[] chain,
	                                     String authType) throws CertificateException {
	                return true;	//信任所有
	            }
	        }).build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        
	        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
	        		.setConnectionManagerShared(true)
	        		.setMaxConnTotal(100)
	        		.setMaxConnPerRoute(50)
	        		.build();
	        
	        _HttpClient = httpClient;
        }
        return _HttpClient;
    }
	
	
	private static HttpResponseEntity executeRequest(CloseableHttpClient httpClient, HttpUriRequest request) throws Exception{
		HttpResponseEntity retval = new HttpResponseEntity();
		
		CloseableHttpResponse response = null;
        try{
        	response = httpClient.execute(request);
        	
        	HttpEntity entity = response.getEntity();
            
        	String result = entity != null ? EntityUtils.toString(entity, CHARSET) : null;
            
            EntityUtils.consume(entity);
            
            //封装响应
            retval.setResult(result);
            retval.setStatusCode( response.getStatusLine().getStatusCode() );
            {
            	Header[] headers = response.getAllHeaders();
            	if(headers != null) {
            		Map<String, String> header = new HashMap<String, String>();
            		for(Header h : headers) {
            			header.put(h.getName(), h.getValue());
            		}
            		retval.setHeader(header);
            	}
            }
            
            LOG.info("Response << {}" , retval.toString());
        }finally{
        	closeQuietly(response);
        }
        return retval;
	}
	
	private static void addHeader(HttpMessage httpMessage, Map<String, String> header){
		if(header != null){
			// XXX 移除掉一些header，因为org.apache.http.protocol.RequestContent.process(HttpRequest, HttpContext)会额外添加header
			header.remove("Transfer-Encoding");
			header.remove("transfer-encoding");
			header.remove("Content-Length");
			header.remove("content-length");
			
			// XXX 不能自定义host请求头，否则会有问题
			header.remove("host");
			header.remove("Host");
			
			// XXX 忽略connection请求头，由http组件决定
			header.remove("connection");
			header.remove("Connection");
			
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
	
	
	private static HttpDelete httpDelete(String url) {
		HttpDelete httpDelete = new HttpDelete(url);
		httpDelete.setConfig(DEFAULT_CONFIG);
		return httpDelete;
	}
	
	
	
	
}
