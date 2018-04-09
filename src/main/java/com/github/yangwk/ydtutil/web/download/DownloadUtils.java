package com.github.yangwk.ydtutil.web.download;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.github.yangwk.ydtutil.web.WebUtils;

public class DownloadUtils {
	/**
	 * 默认编码
	 */
	public static final String CHARSET = "UTF-8";
	/**
	 * 默认content type
	 * <br>
	 * application/octet-stream
	 */
	public static final String CONTENTTYPE_OCTET_STREAM = "application/octet-stream";
	
	/**
	 * 文件下载，使用utf-8编码
	 * @author yangwk
	 * @param request
	 * @param response
	 * @param contentType 去掉charset子声明的contentType。如果为null，将使用默认 {@link #CONTENTTYPE_OCTET_STREAM}
	 * @param absolutePath 文件绝对路径
	 * @param fileName 下载的文件名。将添加响应头Content-Disposition。如果为null，下载的文件名为absolutePath的最后一个名称
	 * @return the number of bytes downloaded
	 */
	public static long download(HttpServletRequest request,HttpServletResponse response,String contentType, String absolutePath, String fileName) {
		OutputStream output = null;
		long count = 0;
		try {
			File file = new File(absolutePath);
			response.setCharacterEncoding(CHARSET);
			response.setContentLengthLong(file.length());
			response.setContentType( contentType == null ? CONTENTTYPE_OCTET_STREAM : contentType );
			String name = file.getName();
			if(fileName != null){
				name = fileName;
			}
			//文件下载乱码问题
			if(WebUtils.getBrowser(request) == WebUtils.BROWSER_IE){	//ie
				name = URLEncoder.encode(name, CHARSET); 
			}
			else{
				name = new String(name.getBytes(CHARSET), "ISO8859-1");
			}
			response.addHeader("Content-Disposition", "attachment; filename="+name);
			
			output = response.getOutputStream();
			count = FileUtils.copyFile(file, output);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			IOUtils.closeQuietly(output);
		}
		
		return count;
	}
}
