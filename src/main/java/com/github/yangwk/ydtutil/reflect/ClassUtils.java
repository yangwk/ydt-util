package com.github.yangwk.ydtutil.reflect;

import org.apache.commons.beanutils.ConvertUtils;

public class ClassUtils {

	/**
	 * 判断是否为日期时间的class
	 * @author yangwk
	 *
	 * @param clazz
	 * @return
	 */
	public static boolean isDateTimeClass(Class<?> clazz){
		//日期
		if(java.util.Date.class.equals(clazz) ||
				java.sql.Date.class.equals(clazz) ||
				java.sql.Time.class.equals(clazz) ||
				java.sql.Timestamp.class.equals(clazz) ||
				java.util.Calendar.class.equals(clazz)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为整型的class
	 * @author yangwk
	 * @param clazz
	 * @return
	 */
	public static boolean isIntClass(Class<?> type){
		Class<?> clazz = ConvertUtils.primitiveToWrapper(type);
		
		if(java.lang.Byte.class.equals(clazz) ||
				java.lang.Short.class.equals(clazz) ||
				java.lang.Integer.class.equals(clazz) ||
				java.lang.Long.class.equals(clazz) ||
				java.math.BigInteger.class.equals(clazz)){
			return true;
		}
		return false;
	}
	
}
