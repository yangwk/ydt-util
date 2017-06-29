package com.github.yangwk.ydtutil.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * 反射帮助类（不支持动态编译）
 * @author yangwk
 *
 */
public final class Reflector<T> {

	private Class<T> type;
	private Map<String, Method> setterMethods = new HashMap<String, Method>();
	private Map<String, Method> getterMethods = new HashMap<String, Method>();
	private Map<String, Field> fieldMap = new HashMap<String, Field>();
	private Method[] methods;
	private List<Field> fields;
	
	/**
	 * 反射帮助类
	 * @param clazz 
	 */
	public Reflector(Class<T> clazz) {
		this.type = clazz;
		init();
	}

	private void init() {
		methods = type.getMethods();
		fields = FieldUtils.getAllFieldsList(type);
	}

	/**
	 * 获取字段对应的setter方法
	 * @author yangwk
	 *
	 * @param fieldName 字段名
	 * @return
	 */
	public Method getSetter(String fieldName){
		Method setter = setterMethods.get(fieldName);
		if(setter == null){
			setter = findSetter(fieldName);
			setterMethods.put(fieldName, setter);
		}
		return setter;
	}
	
	private Method findSetter(String fieldName){
		for ( Method method : methods ){
			if(isSetter(method, fieldName)){
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 获取字段对应的getter方法
	 * @author yangwk
	 *
	 * @param fieldName 字段名
	 * @return
	 */
	public Method getGetter(String fieldName){
		Method getter = getterMethods.get(fieldName);
		if(getter == null){
			getter = findGetter(fieldName);
			getterMethods.put(fieldName, getter);
		}
		return getter;
	}
	
	private Method findGetter(String fieldName){
		for ( Method method : methods ){
			if(isGetter(method, fieldName)){
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 等同于invokeMethod(method, target, new Object[0]);
	 *@see #invokeMethod(Method, Object, Object...)
	 */
	public Object invokeMethod(Method method, Object target){
		return this.invokeMethod(method, target, ArrayUtils.EMPTY_OBJECT_ARRAY);
	}
	
	/**
	 * 调用方法
	 * <br>等同于method.invoke(target, args);
	 * @author yangwk
	 *
	 * @param method
	 * @param target
	 * @param args
	 * @return
	 */
	public Object invokeMethod(Method method, Object target, Object... args){
		Object result = null;
		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
     * 根据字段名判断方法是否为标准的getter方法
     */
	private boolean isGetter(Method method, String fieldName) {
		return method != null &&
				Modifier.isPublic(method.getModifiers()) && 
				!Modifier.isStatic(method.getModifiers()) && 
				!method.getReturnType().equals(Void.TYPE) && 
				method.getParameterTypes().length == 0 && 
				method.getName().equals("get" + upperCaseFirstLetter(fieldName));
	}
	
	/**
     * 根据字段名判断方法是否为标准的setter方法
     */
	private boolean isSetter(Method method, String fieldName) {
		return method != null &&
				Modifier.isPublic(method.getModifiers()) &&
				!Modifier.isStatic(method.getModifiers()) && 
				method.getReturnType().equals(Void.TYPE) && 
				method.getParameterTypes().length == 1 && 
				method.getName().equals("set" + upperCaseFirstLetter(fieldName));
	}
	
    /**
     * 首字母变大写
     */
    private String upperCaseFirstLetter(String name){
    	return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    /**
     * 实例化
     * @author yangwk
     *
     * @param clazz
     * @return
     */
    public T invokeConstructor(){
    	T t = null;
        try {
			t = type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return t;
    }

    /**
     * 当前操作的class
     * @author yangwk
     *
     * @return
     */
	public Class<T> getType() {
		return type;
	}
	
	/**
	 * 返回字段的class
	 * @author yangwk
	 *
	 * @param fieldName 字段名
	 * @return
	 */
	public Class<?> getFieldType(String fieldName){
		return getField(fieldName).getType();
	}
	
	/**
	 * 返回对应的字段
	 * @author yangwk
	 *
	 * @param fieldName 字段名
	 * @return
	 */
	public Field getField(String fieldName){
		Field field = fieldMap.get(fieldName);
		if(field == null){
			field = findField(fieldName);
			fieldMap.put(fieldName, field);
		}
		return field;
	}
	
	private Field findField(String fieldName){
		for(Field field : fields){
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	
}
