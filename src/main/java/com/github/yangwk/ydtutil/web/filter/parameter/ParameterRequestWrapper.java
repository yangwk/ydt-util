package com.github.yangwk.ydtutil.web.filter.parameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @see #ParameterRequestWrapper(HttpServletRequest)
 * @author yangwk
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper{
	
	private Map<String , String[]> paramsMap = new HashMap<String, String[]>();
	
	/**
	 * 参数处理request封装类
	 * <br>作用是对请求参数进行trim()处理
	 * @author yangwk
	 * @param request
	 */
	public ParameterRequestWrapper(HttpServletRequest request) {
		super(request);
		
		this.paramsMap.putAll(request.getParameterMap()); //获得父类的map
		this.trimParameterValues();
	}
	
	/**
	 * 对请求参数的值进行trim()处理
	 */
	private void trimParameterValues(){
		Set<Entry<String, String[]>> set = paramsMap.entrySet();
		Iterator<Entry<String, String[]>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, String[]> entry = iter.next();
			String[] values = entry.getValue();	//只对value处理
			for(int i=0; values != null && i < values.length; i ++){
				if(values[i] != null){
					values[i] = values[i].trim();	//trim()处理
				}
			}
		}
	}

	@Override
	public String getParameter(String name) {	
		String[] values = paramsMap.get(name);  
        if(values == null || values.length == 0) {  
            return null;  
        }  
        return values[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.paramsMap;
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.paramsMap.get(name);
	}

	
}
