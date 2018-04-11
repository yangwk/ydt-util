package com.github.yangwk.ydtutil.reflect;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class Bean2Utils {
	private Bean2Utils(){}
	
	/**
	 * 参考{@link org.apache.commons.beanutils.BeanUtilsBean#describe(Object)}。
	 * 对describe方法进行了优化，结果没有破坏bean各属性的class类型
	 * @author yangwk
	 */
	public static Map<String, Object> describe(final Object bean) throws Exception {

        if (bean == null) {
            return (new java.util.HashMap<String, Object>());
        }

        Map<String, Object> description = PropertyUtils.describe(bean);
        
        //移除class
        Object classValue = description.get("class");
        if(classValue != null && (classValue instanceof java.lang.Class)){
        	description.remove("class");
        }
        
        return (description);

	}
}
