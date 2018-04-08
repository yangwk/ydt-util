package com.github.yangwk.ydtutil.http;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class HttpResponseEntity {

	private int statusCode;
	
	private Map<String, String> header;
	
	private String result;

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public String getResult() {
		return result;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	/**
	 * 断言响应必须为ok
	 */
	public void assertOk(){
		boolean ok = (200 <= statusCode && statusCode < 300);
		if (! ok)
    		throw new IllegalStateException("response status code " + statusCode + " is not ok");
	}
	

	@Override
	public String toString() {
		return new ToStringBuilder(this).
			       append("statusCode", statusCode).
			       append("header", header).
			       append("result", result).
			       toString();
	}
	
	
}
