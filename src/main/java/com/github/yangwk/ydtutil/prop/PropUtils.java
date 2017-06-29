package com.github.yangwk.ydtutil.prop;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

public final class PropUtils extends Properties{
	
	private static final long serialVersionUID = 1L;
	
	//Properties数据库
	private static final ConcurrentHashMap<String, PropUtils> propMap = new ConcurrentHashMap<String, PropUtils>();
	
	private PropUtils(){
	}
	
	//弱线程安全
	private static PropUtils getInstance(String fileName){
		PropUtils prop = propMap.get(fileName);
		if(prop == null){
			prop = loadProp(fileName);	//弱线程安全，如果是相同值，可能会被重复调用
			propMap.putIfAbsent(fileName, prop);	//保存
		}
		
		return prop;
	}
	
	//加载Properties
	private static PropUtils loadProp(String fileName){
		InputStream input = null;
		PropUtils prop = new PropUtils();
		try {
			input = PropUtils.class.getClassLoader().getResourceAsStream(fileName);
			prop.load(input);
		} catch (Exception e) {
			throw new RuntimeException("can't load properties for "+fileName, e);
		}finally{
			IOUtils.closeQuietly(input);
		}
		
		return prop;
	}
	
	public static PropUtils use(String fileName){
		return getInstance(fileName);
	}
	
	/**
	 * 
	 * <p><b>Description: </b>获取Properties的值
	 * @param key
	 * @return
	 */
	public String getStr(String key){
		return getProperty(key);
	}
	
	/**
	 * 
	 * <p><b>Description: </b>获取Properties的值，并转换为boolean
	 * @param key
	 * @return
	 */
	public boolean getBool(String key){
		return Boolean.parseBoolean( getStr(key) );
	}
	
	/**
	 * 
	 * <p><b>Description: </b>获取Properties的值，并转换为int
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		return Integer.parseInt( getStr(key) );
	}
	
	/**
	 * 
	 * <p><b>Description: </b>获取Properties的值，并转换为long
	 * @param key
	 * @return
	 */
	public long getLong(String key){
		return Long.parseLong( getStr(key) );
	}
	
}
