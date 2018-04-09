package com.github.yangwk.ydtutil.web.filter.extract;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * json提取过滤器
 * @author yangwk
 */
public class JsonExtractFilter implements Filter{
	
	/**
	 * http请求的json内容
	 */
	public static final String REQUEST_JSON_CONTENT_ATTRIBUTE = JsonExtractFilter.class.getName() + "_REQUEST_JSON_CONTENT_ATTRIBUTE";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		JsonExtractRequestWrapper requestWrapper = new JsonExtractRequestWrapper((HttpServletRequest)request);  
        chain.doFilter(requestWrapper, response);
	}

	@Override
	public void destroy() {
		
	}

}
