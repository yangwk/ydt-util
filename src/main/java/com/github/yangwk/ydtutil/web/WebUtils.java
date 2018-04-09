package com.github.yangwk.ydtutil.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * web工具类
 * @author yangwk
 *
 */
public class WebUtils {
	
	/**
	 * ie浏览器
	 */
	public static final int BROWSER_IE = 0;
	/**
	 * 谷歌浏览器
	 */
	public static final int BROWSER_CHROME = 1;
	/**
	 * 火狐浏览器
	 */
	public static final int BROWSER_FIREFOX = 2;
	/**
	 * safari浏览器
	 */
	public static final int BROWSER_SAFARI = 3;
	
	
	/**
	 * 根据User-Agent判断浏览器类型
	 * @author yangwk
	 * @param request 
	 * @return 未知浏览器返回-1
	 * @see #BROWSER_IE
	 * @see #BROWSER_CHROME
	 * @see #BROWSER_FIREFOX
	 * @see #BROWSER_SAFARI
	 */
	public static int getBrowser(HttpServletRequest request){
		String ua = request.getHeader("User-Agent").toLowerCase();
		//IE6~IE10版本的User-Agent
		//Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.0; Trident/6.0)
		//IE 11的User-Agent
		//Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko
		if(ua.indexOf("msie") != -1 || ua.indexOf("rv:11.0") != -1){
			return BROWSER_IE;
		}
		else if(ua.indexOf("chrome") != -1){	//safari浏览器没有该标识
			return BROWSER_CHROME;
		}
		else if(ua.indexOf("firefox") != -1){
			return BROWSER_FIREFOX;
		}
		else if(ua.indexOf("safari") != -1){	//谷歌浏览器有该标识
			return BROWSER_SAFARI;
		}
		return -1;
	}
	
	
	/**
	 * 输出内容到HttpServletResponse
	 * @author yangwk
	 * @param content 输出的内容
	 */
	public static void writeToResponse(HttpServletResponse response, String content) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding( "UTF-8" );
			
			writer = response.getWriter();
			writer.write( content );
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
}
