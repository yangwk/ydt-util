package com.github.yangwk.ydtutil.web.filter.extract;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

/**
 * 提取json
 * @author yangwk
 */
public class JsonExtractRequestWrapper extends ContentExtractRequestWrapper{
	
	public JsonExtractRequestWrapper(HttpServletRequest request) {
		super(request);
		
		if(isCanExtract()) {
			setAttribute(JsonExtractFilter.REQUEST_JSON_CONTENT_ATTRIBUTE, new String(getContent(), Charset.forName("UTF-8")) );
		}
	}

	@Override
	protected boolean canExtractContent(String contentType) {
		return contentType != null && contentType.toLowerCase().indexOf("application/json") != -1;
	}
	
	
}
