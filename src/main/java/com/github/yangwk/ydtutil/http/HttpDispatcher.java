package com.github.yangwk.ydtutil.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.yangwk.ydtutil.web.WebUtils;

/**
 * http 分发
 * @author yangwk
 *
 */
public class HttpDispatcher {
	private final String CHARSET = "UTF-8";
	
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	
	public HttpDispatcher(HttpServletRequest request, HttpServletResponse response) {
		this._request = request;
		this._response = response;
	}
	
	private Map<String, String> extractHeader(Map<String, String> extraHeader) {
		Map<String, String> header = new HashMap<String, String>();
		
		Enumeration<String> em = _request.getHeaderNames();
		while(em != null && em.hasMoreElements()) {
			String name = em.nextElement();
			String value = _request.getHeader( name );	//only first header
			header.put(name, value);
		}
		
		if(extraHeader != null) {
			header.putAll(extraHeader);
		}
		
		return header;
	}
	
	private Map<String, String[]> extractParameterMap() {
		return _request.getParameterMap();
	}
	
	
	private String wrapQueryString() {
		StringBuilder queryString = new StringBuilder();
		
		Map<String, String[]> parameterMap = extractParameterMap();
		if(parameterMap == null || parameterMap.isEmpty()) {
			return null;
		}
		
		queryString.append("?");
		for(Entry<String, String[]> entry : parameterMap.entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			for(int r=0; values != null && r < values.length; r ++) {
				queryString.append(name).append("=");
				try {
					queryString.append( StringUtils.isBlank(values[r]) ? "" :  URLEncoder.encode(values[r], CHARSET) );
				} catch (UnsupportedEncodingException e) {
					throw new IllegalStateException(e);
				}
				if(r != values.length - 1) {
					queryString.append("&");
				}
			}
		}
		
		return queryString.toString();
	}
	
	
	private byte[] extractBody() {
		byte[] body = null;
		
		ServletInputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			input = _request.getInputStream();
			
			output = new ByteArrayOutputStream();
			IOUtils.copy(input, output);
			body = output.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}finally {
			IOUtils.closeQuietly(input, output);
		}
		
		return body;
	}
	
	/**
	 * 开始分发。如果Content-Type是application/json格式，会对http request的InputStream进行读取，影响后续操作。
	 * @author yangwk
	 * @param url 不包含query string
	 */
	public void dispatch(final String url, Map<String, String> extraHeader) {
		HttpResponseEntity responseEntity = null;
		
		String method = _request.getMethod().toLowerCase();
		if(method.equals("delete")) {
			String queryString = wrapQueryString();
			String targeturl = StringUtils.isBlank(queryString) ? url : url + queryString;
			
			responseEntity = HttpUtils.delete(targeturl, extractHeader(extraHeader) );
		}else if(method.equals("get")) {
			String queryString = wrapQueryString();
			String targeturl = StringUtils.isBlank(queryString) ? url : url + queryString;
			
			responseEntity = HttpUtils.get(targeturl, extractHeader(extraHeader) );
		}else if(method.equals("post")) {
			String contentType = _request.getContentType().toLowerCase();
			
			if(contentType.contains("application/x-www-form-urlencoded") ) {	//form
				
				responseEntity = HttpUtils.postForm(url, extractParameterMap(), extractHeader(extraHeader) );
			}else {
				byte[] body = extractBody();
				responseEntity = HttpUtils.postByte(url, body, extractHeader(extraHeader) );
			}
		}else {
			throw new IllegalStateException("http request不支持的method: "+ method);
		}
		
		handleResponse(responseEntity);
	}
	
	
	private void handleResponse(HttpResponseEntity responseEntity) {
		_response.setStatus( responseEntity.getStatusCode() );
		_response.setCharacterEncoding( CHARSET );
		if(responseEntity.getHeader() != null) {
			for(Entry<String, String> entry : responseEntity.getHeader().entrySet()) {
				_response.setHeader(entry.getKey(), entry.getValue());
			}
		}
		if(responseEntity.getResult() != null) {
			//only for string
			WebUtils.writeToResponse(_response, responseEntity.getResult());
		}
	}
	
}
