package com.github.yangwk.ydtutil.excel.read;

import java.text.DecimalFormat;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.github.yangwk.ydtutil.reflect.ClassUtils;

/**
 * @see #DefaultReaderConvert()
 * @author yangwk
 *
 */
public class DefaultReaderConvert implements ReaderConvert{
	private DateConverter dateConverter;
	private DecimalFormat numberFormat;
	
	/**
	 * 等同于DefaultReaderConvert("yyyy-MM-dd")
	 * @see #DefaultReaderConvert(String)
	 */
	public DefaultReaderConvert(){
		this("yyyy-MM-dd");
	}
	
	/**
	 * 默认转换器
	 * @param datePattern 日期模式
	 */
	public DefaultReaderConvert(String datePattern){
		dateConverter = new DateConverter();
		dateConverter.setPattern(datePattern);
		
		numberFormat = new DecimalFormat();
		numberFormat.setMinimumFractionDigits(0);
		numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
		numberFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
		numberFormat.setGroupingUsed(false);
	}

	@Override
	public Object convert(final Object value, final Class<?> targetType)  {
		if(ClassUtils.isDateTimeClass(targetType)){
			return dateConverter.convert(targetType, value);
		}
		
		if(value instanceof Number){	//数字
			if(java.lang.String.class.equals(targetType)){	//字符串
				return numberFormat.format(value);
			}
		}
		
		return ConvertUtils.convert(value, targetType);
	}
	
}