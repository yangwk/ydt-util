package com.github.yangwk.ydtutil.excel.write;

import java.text.DecimalFormat;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.github.yangwk.ydtutil.reflect.ClassUtils;

/**
 * 
 * @author yangwk
 *
 */
public class DefaultWriterConvert implements WriterConvert{
	private DateConverter dateConverter;
	private DecimalFormat numberFormat;
	
	/**
	 * 等同于DefaultWriterConvert("yyyy-MM-dd")
	 * @see #DefaultWriterConvert(String)
	 */
	public DefaultWriterConvert(){
		this("yyyy-MM-dd");
	}
	
	/**
	 * 
	 * @param datePattern 日期模式
	 */
	public DefaultWriterConvert(String datePattern){
		dateConverter = new DateConverter();
		dateConverter.setPattern(datePattern);
		
		numberFormat = new DecimalFormat();
		numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
		numberFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
		numberFormat.setGroupingUsed(false);
	}
	
	@Override
	public String convert(Object object,String fieldName,Object fieldValue) {
		if(fieldValue == null){
			return null;
		}
		
		if(ClassUtils.isDateTimeClass( fieldValue.getClass() )){
			//日期转字符串
			return dateConverter.convert(java.lang.String.class, fieldValue);
		}
		
		if(fieldValue instanceof Number){	//数字
			if(ClassUtils.isIntClass(fieldValue.getClass())){	//整型
				numberFormat.setMinimumFractionDigits(0);
			}else{
				numberFormat.setMinimumFractionDigits(1);
			}
			return numberFormat.format(fieldValue);
		}
		
		return ConvertUtils.convert(fieldValue);
	}
}
