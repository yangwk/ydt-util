package com.github.yangwk.ydtutil.range;

/**
 * 判断两个值是否相等的接口
 * @author yangwk
 */
public interface RangeEqual<T> {
	
	/**
	 * 判断两个值是否相等
	 * <br>普遍的做法：
	 * <code>return prev == null ? prev == curr : prev.equals(curr);</code>
	 * @author yangwk
	 * @param prev 上一批的数据
	 * @param curr 当前批的数据
	 * @return 相等则返回true
	 */
	boolean isEqual(T prev, T curr);
}
