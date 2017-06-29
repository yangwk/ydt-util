package com.github.yangwk.ydtutil.tree;

/**
 * 将对象转成json字符串
 * @author yangwk
 *
 * @param <T>
 */
public interface JsonConvert<T> {
	
	/**
	 * 
	 * @author yangwk
	 *
	 * @param t 被转成json字符串的对象
	 * @return json字符串
	 */
	String convert(T t);
}
