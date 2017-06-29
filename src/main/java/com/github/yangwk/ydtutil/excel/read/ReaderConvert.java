package com.github.yangwk.ydtutil.excel.read;

/**
 * 
 * @author yangwk
 *
 */
public interface ReaderConvert {
	
	/**
	 * 
	 * @author yangwk
	 *
	 * @param value Value to be converted (may be null), is Cell value
	 * @param targetType Class of the value to be converted to (must not be null)
	 * @return The converted value
	 */
	Object convert(final Object value, final Class<?> targetType) ;
}
