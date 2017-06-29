package com.github.yangwk.ydtutil.web.filter.parameter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 参数过滤器
 * @author yangwk
 */
public class ParameterFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper((HttpServletRequest)request);  
        chain.doFilter(requestWrapper, response);  
	}

	@Override
	public void destroy() {
		
	}

}
