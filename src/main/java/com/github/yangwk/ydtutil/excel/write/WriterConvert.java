package com.github.yangwk.ydtutil.excel.write;

/**
 * 
 * @author yangwk
 *
 */
public interface WriterConvert {

	/**
	 * 
	 * @author yangwk
	 * @param object 当前对象
	 * @param fieldName object字段名
	 * @param fieldValue object字段值
	 * @return 作为当前单元格的值
	 */
	public String convert(Object object,String fieldName,Object fieldValue);
	
}
